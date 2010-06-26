package com.idragon.adastra.component;

import com.idragon.adastra.context.ImageSource;
import com.idragon.adastra.lang.Severity;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * <p>The abstract form panel displays form elements in rows. Each row may contain a label, a
 * validation image, a component and a detail image. The panel supports displaying validation marks,
 * but doesn't perform any validation.</p>
 *
 * @author  hp
 */
public abstract class AbstractFormPanel extends JPanel {

    // Sorozatszám
    private static final long serialVersionUID = -3187703107506684501L;

    private static final int CI_LABEL = 0;
    private static final int CI_VALIDATION = 1;
    private static final int CI_EDITOR = 2;
    private static final int CI_DETAIL = 3;

    private static final int OFFSET = 1;
    private static final int VERTICAL_MARGIN = 1;

    private static final String DETAIL_COMPONENT_NAME = "detailComponent";
    private static final String EDITOR_COMPONENT_NAME = "editorComponent";
    private static final String FILL_NAME = "fill";
    private static final String LABEL_COMPONENT_NAME = "labelComponent";
    private static final String PROPERTY_NAME_NAME = "propertyName";
    private static final String VALIDATION_COMPONENT_NAME = "validationComponent";

    private int labelWidth = 160;
    private int editorWidth = 240;

    private Component labelStrut = Box.createHorizontalStrut(labelWidth);
    private Component validationStrut = Box.createHorizontalStrut(24);
    private Component editorStrut = Box.createHorizontalStrut(editorWidth);
    private Component detailStrut = Box.createHorizontalStrut(24);

    private boolean validated;
    private boolean detailed;

    private final ArrayList<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

    @Autowired protected ImageSource imageSource;

    /**
     * Abstract form panel.
     */
    public AbstractFormPanel() {
        this(true, false);
    }

    /**
     * Abstract form panel.
     */
    public AbstractFormPanel(boolean validated, boolean detailed) {

        super(new GridBagLayout());

        this.validated = validated;
        this.detailed = detailed;

        attachLabelStrut();
        attachEditorStrut();

        restructure();
    }

    /**
     * Add a row to the form.
     *
     * @param  index         row index
     * @param  propertyName  property name
     * @param  labelText     label text, which may be {@code null}, if there's no label
     * @param  editor        editor component, which may be {@code null}
     * @param  fill          whether the editor should fill the available horizontal space
     */
    public void addRow(int index, String propertyName, String labelText, Component editor,
        boolean fill) {

        HashMap<String, Object> row = new HashMap<String, Object>();

        row.put(PROPERTY_NAME_NAME, propertyName);
        row.put(LABEL_COMPONENT_NAME, (labelText == null) ? null : new JLabel(labelText));
        row.put(EDITOR_COMPONENT_NAME, editor);
        row.put(FILL_NAME, fill);

        rows.add(index, row);

        rebuildRows(index);
    }

    /**
     * Add a row to the end of the form.
     *
     * @param  propertyName  property name
     * @param  labelText     label text, which may be {@code null}, if there's no label
     * @param  editor        editor component, which may be {@code null}
     * @param  fill          whether the editor should fill the available horizontal space
     */
    public void addRow(String propertyName, String labelText, Component editor, boolean fill) {
        addRow(getRowCount(), propertyName, labelText, editor, fill);
    }

    /**
     * Attach label strut component to the form.
     */
    private void attachEditorStrut() {

        add(editorStrut,
            new GridBagConstraints(CI_EDITOR, 0, 1, 1, 0.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * Attach label strut component to the form.
     */
    private void attachLabelStrut() {

        add(labelStrut,
            new GridBagConstraints(CI_LABEL, 0, 1, 1, 0.0d, 0.0d, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * @param   severity  severity, which may not be {@code null}
     *
     * @return  image for the given severity
     */
    private Image fetchImageFor(Severity severity) {

        switch (severity) {

        case ERROR:
            return imageSource.getImage("adastra-images/validation-error.png");

        case INFORMATION:
            return imageSource.getImage("adastra-images/validation-information.png");

        case WARNING:
            return imageSource.getImage("adastra-images/validation-warning.png");

        default:
            throw new IllegalArgumentException("invalid severity: " + severity);
        }

    }

    /**
     * @param   index  the form row index
     *
     * @return  the editor component at a given index, or {@code null}, if no editor is present
     */
    public Component getEditorComponentAt(int index) {
        return (Component) rows.get(index).get(EDITOR_COMPONENT_NAME);
    }

    /**
     * @param   propertyName  the property name
     *
     * @return  the editor component of a given property, or {@code null}, if the property or the
     *          editor component was not found
     */
    public Component getEditorComponentOf(String propertyName) {

        int index = rowIndexOf(propertyName);

        return (index == -1) ? null : (Component) rows.get(index).get(EDITOR_COMPONENT_NAME);
    }

    /**
     * @return  the editor width
     */
    public int getEditorWidth() {
        return editorWidth;
    }

    /**
     * @return  the grid index of a form row index.
     */
    private int getGridIndex(int i) {
        return i + OFFSET;
    }

    /**
     * @return  the label width
     */
    public int getLabelWidth() {
        return labelWidth;
    }

    /**
     * @return  the row count
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Initialize component (after dependency injection).
     */
    public abstract void init();

    /**
     * @return  {@code true}, if the panel contains a detail image column
     */
    public boolean isDetailed() {
        return detailed;
    }

    /**
     * @return  {@code true}, if the panel contains a validation image column
     */
    public boolean isValidated() {
        return validated;
    }

    /**
     * Remove all components and rebuild panel from scratch.
     */
    private void rebuildRows(int fromIndex) {

        for (int i = fromIndex; i < rows.size(); i++) {

            Map<String, Object> row = rows.get(i);
            int gridIndex = getGridIndex(i);

            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // attach static components

            Component labelComponent = (Component) row.get(LABEL_COMPONENT_NAME);
            Component editorComponent = (Component) row.get(EDITOR_COMPONENT_NAME);
            Component validationComponent = (Component) row.get(VALIDATION_COMPONENT_NAME);
            Component detailComponent = (Component) row.get(DETAIL_COMPONENT_NAME);

            boolean fill = Boolean.TRUE.equals(row.get(FILL_NAME));

            if (labelComponent != null) {

                add(labelComponent,
                    new GridBagConstraints(CI_LABEL, gridIndex, 1, 1, 0.0d, 0.0d,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(VERTICAL_MARGIN, 0, VERTICAL_MARGIN, 0), 0, 0));
            }

            if (editorComponent != null) {

                add(editorComponent,
                    new GridBagConstraints(CI_EDITOR, gridIndex, 1, 1, 1.0d, 0.0d,
                        GridBagConstraints.WEST,
                        fill ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE,
                        new Insets(VERTICAL_MARGIN, 0, VERTICAL_MARGIN, 0), 0, 0));
            }

            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // detach or attach dynamic components

            if (validationComponent != null) {

                if (validated) {

                    add(validationComponent,
                        new GridBagConstraints(CI_VALIDATION, gridIndex, 1, 1, 0.0d, 0.0d,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(VERTICAL_MARGIN, 0, VERTICAL_MARGIN, 0), 0, 0));

                } else {

                    remove(validationComponent);
                }
            }

            if (detailComponent != null) {

                if (detailed) {

                    add(detailComponent,
                        new GridBagConstraints(CI_DETAIL, gridIndex, 1, 1, 0.0d, 0.0d,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(VERTICAL_MARGIN, 0, VERTICAL_MARGIN, 0), 0, 0));

                } else {

                    remove(detailComponent);
                }
            }
        }

        repaint();
    }

    /**
     * Remove all form rows.
     */
    public void removeAllRows() {

        for (Map<String, Object> row : rows) {

            Component labelComponent = (Component) row.get(LABEL_COMPONENT_NAME);
            Component editorComponent = (Component) row.get(EDITOR_COMPONENT_NAME);
            Component validationComponent = (Component) row.get(VALIDATION_COMPONENT_NAME);
            Component detailComponent = (Component) row.get(DETAIL_COMPONENT_NAME);

            if (labelComponent != null) {
                remove(labelComponent);
            }

            if (editorComponent != null) {
                remove(editorComponent);
            }

            if (validationComponent != null) {
                remove(validationComponent);
            }

            if (detailComponent != null) {
                remove(detailComponent);
            }
        }

        rows.clear();
        rebuildRows(0);
    }

    /**
     * Restructure panel.
     */
    private void restructure() {

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Layout strut components

        if (validated) {

            add(validationStrut,
                new GridBagConstraints(CI_VALIDATION, 0, 1, 1, 0.0d, 0.0d, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        } else {

            remove(validationStrut);
        }

        if (detailed) {

            add(detailStrut,
                new GridBagConstraints(CI_DETAIL, 0, 1, 1, 0.0d, 0.0d, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        } else {

            remove(detailStrut);
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // layout form row components

        rebuildRows(0);
    }

    /**
     * @return  the row index of a property, or {@code -1}, if the property is not available
     */
    private int rowIndexOf(String propertyName) {

        if (propertyName != null) {

            for (int i = 0; i < rows.size(); i++) {

                if (propertyName.equals(rows.get(i).get(PROPERTY_NAME_NAME))) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * @param  detailed  whether the panel should display detail images
     */
    public void setDetailed(boolean detailed) {

        if (this.detailed != detailed) {
            this.detailed = detailed;

            restructure();
        }
    }

    /**
     * @param  editorWidth  the editor width to set
     */
    public void setEditorWidth(int editorWidth) {

        if (editorWidth < 1) {
            throw new IllegalArgumentException("invalid editor width: " + editorWidth);
        }

        if (this.editorWidth != editorWidth) {
            this.editorWidth = editorWidth;

            remove(editorStrut);
            editorStrut = Box.createHorizontalStrut(editorWidth);
            attachEditorStrut();
        }
    }

    /**
     * @param  imageSource  the image source to set
     */
    public void setImageSource(ImageSource imageSource) {
        this.imageSource = imageSource;
    }

    /**
     * @param  labelWidth  the label width to set
     */
    public void setLabelWidth(int labelWidth) {

        if (labelWidth < 1) {
            throw new IllegalArgumentException("invalid label width: " + labelWidth);
        }

        if (this.labelWidth != labelWidth) {
            this.labelWidth = labelWidth;

            remove(labelStrut);
            labelStrut = Box.createHorizontalStrut(labelWidth);
            attachLabelStrut();
        }
    }

    /**
     * Setup details text.
     *
     * @param  index        form row to setup details text for
     * @param  detailsText  details text
     */
    public void setupDetails(int index, String detailsText) {

        Component oldComponent = (Component) rows.get(index).get(DETAIL_COMPONENT_NAME);

        if (oldComponent != null) {

            remove(oldComponent);
            rows.get(index).remove(DETAIL_COMPONENT_NAME);
        }

        if (StringUtils.isNotBlank(detailsText)) {

            JLabel detailsComponent = new JLabel(new ImageIcon(
                        imageSource.getImage("adastra-images/question-tiny.png")));

            detailsComponent.setToolTipText(detailsText);

            rows.get(index).put(DETAIL_COMPONENT_NAME, detailsComponent);

            if (detailed) {

                add(detailsComponent,
                    new GridBagConstraints(CI_DETAIL, getGridIndex(index), 1, 1, 0.0d, 0.0d,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(VERTICAL_MARGIN, 0, VERTICAL_MARGIN, 0), 0, 0));
            }
        }
    }

    /**
     * Setup details text for a property.
     *
     * @param  propertyName  the property to setup details text for
     * @param  detailsText   details text
     */
    public void setupDetails(String propertyName, String detailsText) {

        int index = rowIndexOf(propertyName);

        if (index != -1) {
            setupDetails(index, detailsText);
        }
    }

    /**
     * Setup validation.
     *
     * @param  index     form row to setup validation for
     * @param  severity  message severity, or {@code null} to remove validation
     * @param  message   validation message, may be {@code null}
     */
    public void setupValidation(int index, Severity severity, String message) {

        Component oldComponent = (Component) rows.get(index).get(VALIDATION_COMPONENT_NAME);

        if (oldComponent != null) {

            remove(oldComponent);
            rows.get(index).remove(VALIDATION_COMPONENT_NAME);
        }

        if (severity != null) {

            JLabel validationComponent = new JLabel(new ImageIcon(fetchImageFor(severity)));
            rows.get(index).put(VALIDATION_COMPONENT_NAME, validationComponent);

            if (StringUtils.isNotBlank(message)) {
                validationComponent.setToolTipText(message);
            }

            if (validated) {

                add(validationComponent,
                    new GridBagConstraints(CI_VALIDATION, getGridIndex(index), 1, 1, 0.0d, 0.0d,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(VERTICAL_MARGIN, 0, VERTICAL_MARGIN, 0), 0, 0));
            }
        }
    }

    /**
     * Setup validation for a property.
     *
     * @param  propertyName  the property to setup validation for
     * @param  severity      message severity, or {@code null} to remove validation
     * @param  message       validation message, may be {@code null}
     */
    public void setupValidation(String propertyName, Severity severity, String message) {

        int index = rowIndexOf(propertyName);

        if (index != -1) {
            setupValidation(index, severity, message);
        }
    }

    /**
     * @param  validated  whether the panel should display validation images
     */
    public void setValidated(boolean validated) {

        if (this.validated != validated) {
            this.validated = validated;

            restructure();
        }
    }
}
