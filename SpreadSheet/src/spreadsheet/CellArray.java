/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spreadsheet;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.AbstractListModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
/**
 *
 * @author 130594b
 */

public class CellArray extends JPanel  implements TableModelListener  {
    private final JTable table;
    private final JScrollPane scroll;
    private final Object backup[][];
    private boolean tableLoked;
    public CellArray()
    {
        super();
        this.setLayout(new BorderLayout());
        final int  nRows = 50;
        final int nCols = 26;
        
        
        ListModel lm = new AbstractListModel() {
        String headers[] = generateRowNames(nRows);

        @Override
            public int getSize() {
              return headers.length;
            }

        @Override
            public Object getElementAt(int index) {
              return headers[index];
            }
          };
        

        
       table = new JTable(generateData(nCols,nRows),generateColNames(nCols));
       backup = generateData(nCols,nRows);
       table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       

        
        JList rowHeader = new JList(lm);
        rowHeader.setFixedCellWidth(50);
         rowHeader.setFixedCellHeight(table.getRowHeight()
             );// + table.getIntercellSpacing().height);
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        
        scroll = new JScrollPane(table);
        scroll.setRowHeaderView(rowHeader);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
       
        add(scroll,BorderLayout.CENTER);
        
        //For Evaluation Process
        tableLoked= false;
        table.getModel().addTableModelListener(this);
        javax.swing.JTextField editor = (javax.swing.JTextField)((DefaultCellEditor)table.getDefaultEditor(String.class)).getComponent();


    }
    
     @Override
    public void tableChanged(TableModelEvent e)
    {
        //Evaluation Calls from Here
        if(tableLoked)
        {  
            tableLoked = false;
            return;
        }
        
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        Object data = model.getValueAt(row, column);
        backup[row][column] = data;
        
        if(data==null ) 
            return;
        
        String cellValue = (String)data;
        if(cellValue.length()>1 && cellValue.charAt(0)=='=')
        {
              tableLoked = true;
              table.setValueAt(Evaluator.evaluateToString(cellValue), row, column);
        }
    }
    


    
    public JTable getTable()
    {
        return table;
    }
    private static String[] generateRowNames(int size)
    {
      String rowNames[] = new String[size];
      for(int j=0;j<size;j++)
        {
                rowNames[j] = String.valueOf(j+1); 
        }
      return rowNames;
    }
    
    private static Object[][] generateData(int nCols,int nRows)
    {
        Object[][] data = new Object[nRows][nCols];
        return data;
    }            
            
    private static String[] generateColNames(int nCols)
    {
        String[] columnNames = new String[nCols];

        for(int i=0; i<nCols;i++) columnNames[i] = ""+ (char)(i+65);
        return columnNames;
    }
  
    //
    class RowHeaderRenderer extends JLabel implements ListCellRenderer {

    RowHeaderRenderer(JTable table) {
      JTableHeader header = table.getTableHeader();
      setOpaque(true);
      setBorder(UIManager.getBorder("TableHeader.cellBorder"));
      setHorizontalAlignment(CENTER);
      setForeground(header.getForeground());
      setBackground(header.getBackground());
      setFont(header.getFont());
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
        setText((value == null) ? "" : value.toString());
        return this;
  }
}
}
