package com.idragon.adastra.component;

import com.idragon.adastra.context.ResourceCachingImageSource;
import com.idragon.adastra.lang.Severity;

import org.springframework.core.io.DefaultResourceLoader;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


/**
 * Form panel demo application.
 *
 * @author  hp
 */
public class FormPanelDemo implements Runnable {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        new FormPanelDemo().run();
    }

    @Override public void run() {

        ResourceCachingImageSource imageSource = new ResourceCachingImageSource();
        imageSource.setResourceLoader(new DefaultResourceLoader());
        imageSource.setLocation("adastra-images");

        MyFormPanel p = new MyFormPanel();
        p.setImageSource(imageSource);
        p.init();
        p.setBorder(new EmptyBorder(4, 4, 4, 4));

        JFrame f = new JFrame("MyForm");

        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(p, BorderLayout.CENTER);
        f.pack();

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private static class MyFormPanel extends AbstractFormPanel {

        // serial
        private static final long serialVersionUID = 3545868134974309853L;

        public MyFormPanel() {
            super(true, true);
        }

        @Override public void init() {

            setLabelWidth(120);
            setEditorWidth(240);

            addRow("prop1", "Property #1", new JTextField(), true);
            addRow("prop2", "Property #2", new JTextField(), true);
            addRow("prop3", "Property #3", new JComboBox(), true);
            addRow("prop4", "Property #4", new JCheckBox(), false);

            setupDetails("prop1", "Details for property #1");
            setupValidation("prop3", Severity.INFORMATION, "All ok");
        }
    }
}
