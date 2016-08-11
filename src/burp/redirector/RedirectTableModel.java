/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burp.redirector;


import javax.swing.table.AbstractTableModel;

/**
 *
 * @author vagrant
 */
public class RedirectTableModel extends AbstractTableModel implements RedirectorObserver {

    private static final String[] columnNames = new String[]{"Enabled", "Redirect Pattern"};

    
    
    @Override
    public int getRowCount() {
        return Redirector.getInstance().getRules().size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        RedirectRule rule = Redirector.getInstance().getRules().get(row);
        switch (column) {
            case 0:
                return rule.enabled;
            case 1:
                return rule.describe();
            default:
                return null;
        }
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return Boolean.class;
            case 1:
            default:
                return String.class;
            
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (column == 0) {
            Redirector.getInstance().getRules().get(row).enabled = ((Boolean) value);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return (column == 0);
    }

    @Override
    public void update() {
        this.fireTableDataChanged();
    }
 
}
