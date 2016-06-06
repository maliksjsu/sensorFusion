/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jme3test.helloworld;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.*;


/**
 *
 * @author leejona1
 */
public class CSV_Input {
    
    public static void main(String [] args)
    {
        CSV_Input.SetUpCSVArray();
    }
    
    public static String xStrPath;
    public static Float[][] CSVArray;

    static int row;
    //Constructor
    public CSV_Input(){
    }
   
   
    static Float[][] SetUpCSVArray()
    {
        String csvFileLocation = "/Users/kanwarmalik/Documents/MonkeyJava/HelloWorldTutorial/src/jme3test/helloworld/sensordata.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int x = 1;
        CSVArray = new Float[2000][4];
        row = 0;
        try {
            
            br = new BufferedReader(new FileReader(csvFileLocation)); //Create the buffered reader
            while ((line = br.readLine()) != null){                   //Reads for all lines of the CSV input
                
                //use comma as separator
                String [] RawInput = line.split(csvSplitBy);          //use comma as separator
                
                System.out.println("Line "+x+" read");              //output what line has been read
                
                if (x>1)                                              //skip the header input when converting to float array
                {
                    for (int i= 0; i < 4; i++)                        //Fills in q_0, q_1, q_2, q_3 in w,x,y,z format
                    {
                        CSVArray[row][i] = Float.parseFloat(RawInput[12+i]);    //q_0 starts on column 12 of the CSV file
                    }   
                }
                x++;                                                  //iterate the row 
                row++;
                //System.out.println("\n"+RawInput[12]+", "+RawInput[13]+", "+RawInput[14]+", "+RawInput[15]); //uncomment if you want to see row data
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            System.out.println("Done");
    
                    
        }
        
    return CSVArray;
    }
}
