/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Save;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author 164645
 * @version 04/29/2018
 * This class facilitates file reading and writing.
 */
public class FileIO{

    /**
     * Default class constructor.
     */
    public FileIO(){}
    
    /**
     * This class reads a file and splits the input into String tokens.
     * @param fileName file to be read
     * @return String array of tokens
     * @throws IOException if can't read
     */
    public String[] read(String fileName) throws IOException{
        String[] tokens = null;
        String line = "";
        
            BufferedReader br = new BufferedReader(new FileReader(fileName));
        
            while ((line = br.readLine()) != null) {
                tokens = line.split(";");
            }
            br.close();
        
        return tokens;
    }
    
    /**
     * This class writes a passed String output to a file called fileName.
     * @param fileName file to be saved
     * @param output info to be saved in file 
     * @throws IOException if can't write
     */
    public void write(String fileName, String output) throws IOException{
        FileWriter fw = new FileWriter(fileName);
        fw.write(output);
        fw.close();
    }
    
    /**
     * This class deletes a saved game file.
     * @param fileName name of file to delete
     * @throws IOException if can't delete
     */
    public void delete(String fileName) throws IOException{
        File file = new File("SavedGames/" + fileName + ".txt");
        file.delete();
    }
}
