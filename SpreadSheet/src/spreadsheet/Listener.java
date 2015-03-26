/*
   * This Class operates table functions which are common to all worksheets
 */

package spreadsheet;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.text.BadLocationException;
/**
 *
 * @author Janaka
 */
public class Listener implements TableModelListener,DocumentListener{
    private CellArray cellArray;
    private javax.swing.JLabel lableCellName;
    private javax.swing.JLabel lableStates;
    private javax.swing.JTextField textCellValue;
    private TableModel backup;
    
     public Listener(CellArray cellArray) {
         setCellArray(cellArray);
    }
    
     public void setCellArray(CellArray cellArray)
     {
         cellArray.getTable().getModel().removeTableModelListener(this);
         this.cellArray = cellArray;
         cellArray.getTable().getModel().addTableModelListener(this);
         
         JTable table = cellArray.getTable();
         table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                    updateContents();            }
            });
            table.addKeyListener(new java.awt.event.KeyAdapter(){
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                    updateContents();
                }
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                   updateContents();
                }
            });
     }
     
    public void setLableCellName(javax.swing.JLabel lableCellName)
    {
        this.lableCellName = lableCellName;
    }
    public void setTextCellValue(javax.swing.JTextField text)
    {
        this.textCellValue = text;
        javax.swing.JTextField editor = (javax.swing.JTextField)((DefaultCellEditor)cellArray.getTable().getDefaultEditor(String.class)).getComponent();
        editor.getDocument().addDocumentListener(this);
        editor.addActionListener(null);
    }
    
    @Override
    public void tableChanged(TableModelEvent e)
    {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        
        if(data==null ) return;
        textCellValue.setText(data.toString());
    }
    
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        try
        {
            textCellValue.setText( e.getDocument().getText(0, e.getDocument().getLength() ));
        }
        catch (BadLocationException eb)
        {
            System.out.println("Something is wrong");
        }
    }
    
    public void updateContents()
    {
        JTable table = cellArray.getTable();
        int column = table.getSelectedColumn();
        int row = table.getSelectedRow();
        if(row<0||column<0) return;
        if(table.isEditing()) return;
        try
        {
            lableCellName.setText(String.valueOf(row+1)+table.getColumnName(column));
            textCellValue.setText((String)table.getValueAt(row, column));
        }
        catch(Exception e)
        {
            
        }
    }
    
    @Override
    public void insertUpdate(DocumentEvent e)
    {
        changedUpdate(e);
        
    }
    @Override
    public void removeUpdate(DocumentEvent e)
    {
        changedUpdate(e);
    }
    
}
