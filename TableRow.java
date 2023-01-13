/*
 * 
 */
package Incognito;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author adenugad
 */
public class TableRow {
    int rowNumber; //number of row in the table
    //int noOfColumns;
    ArrayList<String> data = new ArrayList<>();
    public TableRow(){
        
    }
    public TableRow(String data, int rowNumber){
        String[] stringArray = data.split(",");
        Collections.addAll(this.data, stringArray);
        this.rowNumber = rowNumber;
    }
    public TableRow(ArrayList<String> data, int rowNumber){
        this.data.addAll(data);
    }
    //assuming row is represented as ... , ... , ... , ... ,
    public void addData(String data){
        String[] stringArray = data.split(",");
        Collections.addAll(this.data, stringArray);
    }
    public void addData(ArrayList<String> data){
        this.data.addAll(data);
    }
    
    public ArrayList<String> getData(){
        return data;
    }
    /**use this to check equality of table
     * @param row - row, the calling row wants to be compared to
     * @return boolean */
    public boolean checkEquality(TableRow row){
        if(data.size() != row.data.size())
            return false;
        else
        {
            for(int i = 0; i < data.size(); i++){
                if(!(data.get(i).equalsIgnoreCase(row.data.get(i))))
                    return false;
            }
            return true;
        }
        
    }
    
    public TableRow copy(){
        TableRow newTableRow = new TableRow();
        newTableRow.rowNumber = rowNumber;
        newTableRow.data.addAll(data);
        return newTableRow;
    }
    
    public void rowPrint(){
        for (String datum : data) {
            System.out.printf("%15s", datum);
        }
        System.out.println();
    }
    
    /*public boolean seeIfSequenceIsInRow(ArrayList<String> array){
        
    }*/
  
}
