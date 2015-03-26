/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spreadsheet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
/**
 *
 * @author Janaka
 */

public class DataManager {
    private String path;
    
    public DataManager(String path)
    {
        this.path = path;
    }
    public DataManager()
    {
        this.path = "default.txt";
    }
    public void save(JTable table)
    {
        if(table.isEditing())
        {
            table.getCellEditor().stopCellEditing();
        }
        String content = encode(table.getModel());
        File logFile=new File(path);
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            writer.write (content);
            writer.close();
            System.out.println("Calcultion completed! \nDone!");

        }
        catch(IOException e)
        {
            System.out.println("Wrting faild");
        }
    }
    
    public void load(JTable table)
    {
        try
        {
            FileReader fr = new FileReader(path) ;
            BufferedReader textReader = new BufferedReader (fr);
            
            String firstLine[] = textReader.readLine().split(" ");
            int rows = Integer.parseInt(firstLine[0]);
            int cols = Integer.parseInt(firstLine[1]);
            
            for(int i=0;i<rows;i++)
                for(int j=0;j<cols;j++)
                    table.setValueAt(textReader.readLine(), i, j);
        }
        catch (Exception e)
        {
            System.out.println("Reading failed");
        }
    }
    
    private String encode(TableModel tm)
    {
        String str;
        int rows = tm.getRowCount();
        int cols = tm.getColumnCount();
        str = String.valueOf(rows)+" "+String.valueOf(cols)+"\n";
        for(int i=0; i<rows;i++)
            for(int j=0;j<cols;j++)
            {
                if(tm.getValueAt(i,j)!=null)
                    str+=(String)tm.getValueAt(i,j);
                str+="\n";
            }
        return str;
    }
}
