/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Save;

import Game.MarupekeGrid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author 164645
 * @version 04/29/2018
 * This class handles saved games and the saved color option.
 * Only the game names are loaded on runtime. The actual games are loaded
 * on request.
 */
public class SaveHandler {
    
    private String saveFileNames;
    private int colorChoice;
    private ObservableList<Save> observableList;
    private Save currentSave;
    private static List<Save> saves;
    private static FileIO file = new FileIO();// file write/read class
    private final static String SAVED_GAMES_NAMES = "src/SavedGames.txt";
    private final static String COLOR_CHOICE = "src/ColorOptions.txt";
    
    /**
     *Loads previously saved game names+moves and creates an 
     * observable list to display on the GUI tableView.
     * Also loads saved color preference.
     */
    public SaveHandler(){
        saves = new ArrayList<Save>();
        saveFileNames = "";
        loadColorChoice();
        
        try {
            String[] gameTokens = file.read(SAVED_GAMES_NAMES);
            for(int i = 0 ; i < gameTokens.length ; i += 2){
                updateFileNames(Integer.parseInt(gameTokens[i]), gameTokens[i+1]);
                saves.add(new Save(Integer.parseInt(gameTokens[i]), gameTokens[i+1]));
            }
        }
        catch(IOException ex){}
        catch(NullPointerException ex2){}
        
        observableList = observableArrayList(saves);
    }
    
    /**
     * reads and returns saved color choice. returns 1 as default and
     * creates color file if non-existent.
     */
    private void loadColorChoice(){
        String[] colorChoices;
        try {
            colorChoices = file.read(COLOR_CHOICE);
            colorChoice = Integer.parseInt(colorChoices[0]);
        } catch (IOException ex) {
            colorChoice = 1;
            String color = "1;";
            try {
                file.write(COLOR_CHOICE, color);
            } catch(IOException ex1){}
        }
    }
    
    /**
     *changes saved color preference to passed param.
     * @param colorChoice 
     */
    public void saveColor(int colorChoice){
        String color = "" + colorChoice;
        try {
            file.write(COLOR_CHOICE, color);
        } catch(IOException ex1){}
    }
    
    /**
     * Creates a new save.
     * @param moveCount
     * @param fileName
     * @param puzzle current marupeke grid
     */
    public void addSave(int moveCount, String fileName, MarupekeGrid puzzle) {
        
        for(int i = 0; i < saves.size() ; i++){
            if(saves.get(i).getName().equals(fileName)){
                deleteSave(i);
            }
        }
        addSaveName(moveCount,fileName);
        addSaveGame(puzzle, fileName);  
    }
    
    /**
     * Updates saved game names file and observableList
     * @param moveCount
     * @param fileName
     */
    public void addSaveName(int moveCount, String fileName){
        updateFileNames(moveCount, fileName);
        try {
            file.write(SAVED_GAMES_NAMES, saveFileNames);
        }catch(IOException e){}
        
        saves.add(new Save(moveCount, fileName));
        observableList.add(saves.get(saves.size()-1));
    }
    
    /**
     * Creates new saved game file with current symbols and fileName
     * @param puzzle
     * @param fileName
     */
    public void addSaveGame(MarupekeGrid puzzle, String fileName){
        String output = puzzle.toString();
        try{
            file.write("SavedGames/" + fileName + ".txt", output);
        } catch (IOException e){}
    }
    
    /**
     * Attempts to delete saved game file, removes fileName from
     * observable list and updates fileNames file.
     * @param index save to be deleted.
     */
    public void deleteSave(int index){
        try {
            file.delete(saves.get(index).getName());
        }catch(IOException ex){}
        saveFileNames = "";
        for(int i = 0 ; i < saves.size() ; i++){
            if(saves.get(i).getName() != saves.get(index).getName()){
                updateFileNames(saves.get(i).getMoves(),saves.get(i).getName());
            }else{
                saves.remove(i);
                observableList.remove(i);
            }
        }
        try {
            file.write(SAVED_GAMES_NAMES, saveFileNames);
        }catch(IOException e){}
    }
    
    /**
     * Template for file names String
     * @param moves
     * @param name
     */
    public void updateFileNames(int moves, String name){
        if(saveFileNames == ""){
            saveFileNames = moves + ";" + name;
        }else{
            saveFileNames += ";" + moves + ";" + name;
        }
    }
    
    /**
     * @return names of current saves as String array
     */
    public String[] getSaves(){
        String[] names = new String[saves.size()];
        for(int i = 0 ; i < saves.size() ; i++){
            names[i] = saves.get(i).getName();
        }
        return names;
    }
        
    /**
     * Used by GUI for tableView
     * @return saves observable List
     */
    public ObservableList<Save> getVisibleList() {
        return observableList;
    }
    
    /**
     * @return current saved color preference.
     */
    public int getColorChoice(){
        return colorChoice;
    }    
    //============================================================//
    // single game functionality

    /**
     * Reads and loads saved game file.
     * @param index save to load
     * @throws IOException
     */
    
    public void loadGame(int index) throws IOException{
        String fileName = "SavedGames/" + saves.get(index).getName() + ".txt";
        String tokens[] = file.read(fileName);
        currentSave = new Save(saves.get(index).getMoves(),saves.get(index).getName());
        currentSave.setSave(Integer.parseInt(tokens[0]), tokens[1]);
    }
    
    /**
     * @return loaded game symbols
     */
    public char[][] getSaveSymbols(){
        int i = 0;
        int size = currentSave.getSize();
        char[][] output = new char[size][size];
        char[] sSymbols = currentSave.getSymbols().toCharArray();
        for (int row = 0 ; row < output.length ; row++) {
            for (int column = 0; column < output.length ; column++) {
                output[row][column] = sSymbols[i];
                i++;
            }
        }
        return output;
    }
 
    /**
     * @return loaded game size
     */
    public int getSaveSize(){
        return currentSave.getSize();
    }    

    /**
     * @return loaded game # of moves
     */
    public int getSaveMoves(){
        return currentSave.getMoves();
    }

    /**
     * @return loaded game name
     */
    public String getSaveName(){
        return currentSave.getName();
    }

}

