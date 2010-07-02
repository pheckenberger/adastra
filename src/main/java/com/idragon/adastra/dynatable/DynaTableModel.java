package com.idragon.adastra.dynatable;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;


/**
 * Dynamic table model uses {@code LazyDynaBean} instances as backing storage. Table model structure
 * and content can be modified on demand.
 *
 * @author  iDragon
 */
public class DynaTableModel extends AbstractTableModel {

    // Serial number
    private static final long serialVersionUID = -5883539238844139403L;

    /** Table bean class (column container) */
    private LazyDynaClass beanClass = new LazyDynaClass(
            "com.idragon.adastra.dynatable.DynaTableBean", LazyDynaBean.class);

    /** Table content (row container) */
    private Vector<LazyDynaBean> beans = new Vector<LazyDynaBean>();

    /** Property instructions map */
    private HashMap<String, PropertyInstructions> propertyInstructionsMap =
        new HashMap<String, PropertyInstructions>();

    /** Editable evaluators */
    private ArrayList<DynaTableModelEvaluator<Boolean>> cellEditableEvaluators =
        new ArrayList<DynaTableModelEvaluator<Boolean>>();

    /**
     * Dynamic table model.
     */
    public DynaTableModel() {
    }

    /**
     * Adds a new bean at the specified index and fires the appropriate change events.
     *
     * @param   rowIndex  Row index at which the specified element is to be inserted.
     *
     * @throws  IndexOutOfBoundsException  If the given index is out of the valid range.
     */
    public void addBean(int rowIndex) {

        if ((0 < rowIndex) || (beans.size() < rowIndex)) {
            throw new IndexOutOfBoundsException("invalid index: " + rowIndex);
        }

        LazyDynaBean bean = new LazyDynaBean(beanClass);
        beans.add(rowIndex, bean);

        // fire event
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    /**
     * Add a bean property to the model.If the property is already present with the same type,
     * nothing happens. The property will be editable and updateable by default. The method fires
     * the appropriate change events, when needed.
     *
     * @throws  IllegalArgumentException  If the property name is blank or the property type is
     *                                    {@code null}, or the property is already present, but with
     *                                    a different type.
     */
    public void addBeanProperty(String propertyName, Class<?> propertyType) {

        Assert.hasText(propertyName, "property name is blank");
        Assert.notNull(propertyType, "property type is null");

        if (beanClass.isDynaProperty(propertyName)) {

            Assert.isTrue(beanClass.getDynaProperty(propertyName).getType().equals(propertyType),
                "property is already present, but with a different type");

            // if the types are equal, the property is already present with the matching type

        } else {

            beanClass.add(propertyName, propertyType);
            // it is not necessary to initialize the added property on the beans

            // set up default property instructions for the property
            PropertyInstructions propertyInstructions = new PropertyInstructions();
            setupPropertyInstructionsDefault(propertyInstructions);

            propertyInstructionsMap.put(propertyName, propertyInstructions);

            // fire event
            fireTableStructureChanged();
        }
    }

    /**
     * Add evaluator for the cells' editable attribute.
     *
     * @param  evaluator  Evaluator, which evaluates the cells' editable attribute.
     */
    public void addCellEditableEvaluator(DynaTableModelEvaluator<Boolean> evaluator) {

        if (evaluator != null) {
            cellEditableEvaluators.add(evaluator);
        }
    }

    /**
     * Check column index.
     *
     * @param   columnIndex  Column index to check.
     *
     * @throws  IndexOutOfBoundsException  If the given column index is outside the range.
     */
    protected void checkColumnIndex(int columnIndex) {

        if ((columnIndex < 0) || (beanClass.getDynaProperties().length <= columnIndex)) {
            throw new IndexOutOfBoundsException("invalid column index: " + columnIndex);
        }
    }

    /**
     * Check row index.
     *
     * @param   rowIndex  Row index to check.
     *
     * @throws  IndexOutOfBoundsException  If the given row index is outside the range.
     */
    protected void checkRowIndex(int rowIndex) {

        if ((rowIndex < 0) || (beans.size() <= rowIndex)) {
            throw new IndexOutOfBoundsException("invalid row index: " + rowIndex);
        }
    }

    @Override public Class<?> getColumnClass(int columnIndex) {

        checkColumnIndex(columnIndex);

        return beanClass.getDynaProperties()[columnIndex].getType();
    }

    @Override public int getColumnCount() {
        return beanClass.getDynaProperties().length;
    }

    @Override public String getColumnName(int columnIndex) {

        checkColumnIndex(columnIndex);

        return beanClass.getDynaProperties()[columnIndex].getName();
    }

    /**
     * This method returns the column name without checking the column index.
     *
     * @param   columnIndex  A previously checked column index.
     *
     * @return  Return the column name.
     */
    protected String getColumnNameUnchecked(int columnIndex) {
        return beanClass.getDynaProperties()[columnIndex].getName();
    }

    @Override public int getRowCount() {
        return beans.size();
    }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {

        checkRowIndex(rowIndex);
        checkColumnIndex(columnIndex);

        DynaProperty dynaProperty = beanClass.getDynaProperties()[columnIndex];
        DynaBean dynaBean = beans.get(rowIndex);

        return dynaBean.get(dynaProperty.getName());
    }

    /**
     * @param   propertyName  Property name to check.
     *
     * @return  Whether the given property name is present in the model.
     */
    public boolean hasProperty(String propertyName) {
        return beanClass.isDynaProperty(propertyName);
    }

    @Override public boolean isCellEditable(int rowIndex, int columnIndex) {

        checkRowIndex(rowIndex);
        checkColumnIndex(columnIndex);

        return isCellEditableUnchecked(rowIndex, columnIndex);
    }

    /**
     * This method returns, whether a given cell is editable without checking the row and column
     * index.
     *
     * @param   rowIndex     A previously checked row index.
     * @param   columnIndex  A previously checked column index.
     *
     * @return  Whether a given cell is editable.
     */
    protected boolean isCellEditableUnchecked(int rowIndex, int columnIndex) {

        boolean columnEditable = isColumnEditableUnchecked(columnIndex);
        boolean evaluatedEditable = true;

        for (DynaTableModelEvaluator<Boolean> editableEvaluator : cellEditableEvaluators) {

            if (Boolean.FALSE.equals(editableEvaluator.evaluate(this))) {

                evaluatedEditable = false;

                // stop on first veto
                break;
            }
        }

        return (columnEditable && evaluatedEditable);
    }

    /**
     * @param   columnIndex  Column index to check.
     *
     * @return  Whether the given column is editable.
     *
     * @throws  IndexOutOfBoundsException  If the given column index is outside the range.
     */
    public boolean isColumnEditable(int columnIndex) {

        checkColumnIndex(columnIndex);

        return isColumnEditableUnchecked(columnIndex);
    }

    /**
     * This method returns, whether the given column is editable without checking the column index.
     *
     * @param   columnIndex  A previously checked column index.
     *
     * @return  Whether the given column is editable.
     */
    protected boolean isColumnEditableUnchecked(int columnIndex) {

        String propertyName = getColumnNameUnchecked(columnIndex);
        PropertyInstructions propertyInstructions = propertyInstructionsMap.get(propertyName);

        return propertyInstructions.editable;
    }

    /**
     * @param   columnIndex  Column index to check.
     *
     * @return  Whether the given column is updateable.
     *
     * @throws  IndexOutOfBoundsException  If the given column index is outside the range.
     */
    public boolean isColumnUpdateable(int columnIndex) {

        checkColumnIndex(columnIndex);

        return isColumnUpdateableUnchecked(columnIndex);
    }

    /**
     * This method returns, whether the given column is updateable without checking the column
     * index.
     *
     * @param   columnIndex  A previously checked column index.
     *
     * @return  Whether the given column is updateable.
     */
    protected boolean isColumnUpdateableUnchecked(int columnIndex) {

        String propertyName = getColumnNameUnchecked(columnIndex);
        PropertyInstructions propertyInstructions = propertyInstructionsMap.get(propertyName);

        return propertyInstructions.updateable;
    }

    /**
     * Removes an existing bean form the model and fires the appropriate change events.
     *
     * @param   rowIndex  Row index, from which the existing bean is to be removed.
     *
     * @throws  IndexOutOfBoundsException  If the given index is out of the valid range.
     */
    public void removeBean(int rowIndex) {

        checkRowIndex(rowIndex);

        beans.remove(rowIndex);

        // fire event
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    /**
     * Removes a bean property from the model. If the property is not present in the model, nothing
     * happens. The method fires the appropriate change events, when needed.
     *
     * @param   propertyName  Name of the bean property to remove.
     *
     * @throws  IllegalArgumentException  If the given property name is blank.
     */
    public void removeBeanProperty(String propertyName) {

        Assert.hasText(propertyName, "property name is blank");

        if (beanClass.isDynaProperty(propertyName)) {

            beanClass.remove(propertyName);
            // it is not necessary to remove the obsolete property from the beans

            // remove obsolete property instructions
            propertyInstructionsMap.remove(propertyName);

            // fire event
            fireTableStructureChanged();
        }
    }

    /**
     * Remove evaluator for the cells' editable attribute.
     *
     * @param  evaluator  Evaluator, which evaluates the cells' editable attribute.
     */
    public void removeCellEditableEvaluator(DynaTableModelEvaluator<Boolean> evaluator) {

        if (evaluator != null) {
            cellEditableEvaluators.remove(evaluator);
        }
    }

    /**
     * Sets column editable attribute. The method fires a table structure change event, when needed.
     *
     * @param   columnIndex  Column index.
     * @param   editable     Whether the property is editable.
     *
     * @throws  IndexOutOfBoundsException  If the given column index is outside the range.
     */
    public void setColumnEditable(int columnIndex, boolean editable) {

        checkColumnIndex(columnIndex);

        String propertyName = getColumnNameUnchecked(columnIndex);
        PropertyInstructions propertyInstructions = propertyInstructionsMap.get(propertyName);

        if (propertyInstructions.editable != editable) {

            propertyInstructions.editable = editable;

            // fire event
            fireTableStructureChanged();
        }
    }

    /**
     * Sets column updateable attribute.
     *
     * @param   columnIndex  Column index.
     * @param   updateable   Whether the property is updateable.
     *
     * @throws  IndexOutOfBoundsException  If the given column index is outside the range.
     */
    public void setColumnUpdateable(int columnIndex, boolean updateable) {

        checkColumnIndex(columnIndex);

        String propertyName = getColumnNameUnchecked(columnIndex);
        PropertyInstructions propertyInstructions = propertyInstructionsMap.get(propertyName);

        propertyInstructions.updateable = updateable;
    }

    /**
     * Setup property instructions with default values.
     *
     * @param  propertyInstructions  Property instructions instance to set up.
     */
    protected void setupPropertyInstructionsDefault(PropertyInstructions propertyInstructions) {
    }

    @Override public void setValueAt(Object value, int rowIndex, int columnIndex) {
        updateValue(rowIndex, columnIndex, value);
    }

    /**
     * Updates a bean property value and fires the appropriate change events, when needed. When the
     * property is not updateable, nothing happens.
     *
     * @param   rowIndex     Row index.
     * @param   columnIndex  Column index.
     * @param   value        Value.
     *
     * @throws  IndexOutOfBoundsException  If the given row or column inde is out of the valid
     *                                     range.
     */
    public void updateValue(int rowIndex, int columnIndex, Object value) {

        checkColumnIndex(columnIndex);
        checkRowIndex(rowIndex);

        String propertyName = getColumnNameUnchecked(columnIndex);
        PropertyInstructions propertyInstructions = propertyInstructionsMap.get(propertyName);

        if (propertyInstructions.updateable) {

            DynaProperty dynaProperty = beanClass.getDynaProperties()[columnIndex];
            DynaBean dynaBean = beans.get(rowIndex);

            dynaBean.set(dynaProperty.getName(), value);

            // fire event
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    // TODO: Historize

    /**
     * Property instructions.
     *
     * @author  hp
     */
    protected static class PropertyInstructions {

        /** Editable? */
        public boolean editable = true;

        /** Updateable? */
        public boolean updateable = true;
    }
}
