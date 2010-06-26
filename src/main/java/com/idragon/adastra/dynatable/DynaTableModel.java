package com.idragon.adastra.dynatable;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;

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

    /** Table bean class (columns) */
    private LazyDynaClass beanClass = new LazyDynaClass();

    /** Table content (rows) */
    private Vector<LazyDynaBean> beans = new Vector<LazyDynaBean>();

    /**
     * Dynamic table model.
     */
    public DynaTableModel() {
    }

    /**
     * Adds a new bean at the specified index.
     *
     * @param   index  Index at which the specified element is to be inserted.
     *
     * @return  The created {@code DynaBean}.
     */
    public DynaBean addBean(int index) {

        LazyDynaBean bean = new LazyDynaBean(beanClass);
        beans.add(index, bean);

        // TODO: fire model change event

        return bean;
    }

    @Override public int getColumnCount() {
        return beanClass.getDynaProperties().length;
    }

    @Override public int getRowCount() {
        return beans.size();
    }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        // TODO Auto-generated method stub
        super.setValueAt(aValue, rowIndex, columnIndex);
    }
}
