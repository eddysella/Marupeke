package Game;

import Save.SaveHandler;
import Control.Command;
import Control.CommandWord;
import Save.Save;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * The Marupeke Class creates an interactive game using the MarupekeGrid
 * class. It also supports various methods for the MarupekeGUI and 
 * SaveHandler, the class which handles saved games.
 * @author 164645
 * @version 05/10/2018
 */
public class Marupeke {
    
    private Random random = new Random();
    private static MarupekeGrid puzzle = new MarupekeGrid(); // grid initializer
    private int totalMoves;
    private static SaveHandler saveHandler = new SaveHandler(); // handles saved games
    private static ArrayList<Command> moves = new ArrayList<>();// list of moves made
    private boolean counterLimBool = true;
    private int counterLimit;
    
    /**
     * Default constructor.
     */
    public void Marupeke(){}
    
    /**
     * Starts a game.
     * Uses isPuzzleFinishable in a loop to get a finishable game.
     * Displays a dialog after 1000 iterations.
     * @param size grid size
     * @param difficulty chosen difficulty
     */
    public void Start(int size, CommandWord difficulty) {
        totalMoves = 0;
        moves.clear();
        Command launchCommand  = new Command(difficulty, size);
        execute(launchCommand);// marupeke game initialized
    }
    
    /**
     * Executes a passed Command object.
     * @param command command to execute
     */
    public void execute(Command command) {
        switch(command.getCommand()) {
            //Difficulties:
            case EASY:
                easyGame(command.getSize());
                break;
            case MEDIUM:
                mediumGame(command.getSize());
                break;
            case HARD:
                hardGame(command.getSize());
                break;
            //Mark Commands:
            case MARKX:
                puzzle.markX(command.getRow(), command.getColumn());
                break;
            case MARKO:
                puzzle.markO(command.getRow(), command.getColumn());
                break;
            case CLEAR:
                puzzle.tileClear(command.getRow(), command.getColumn());
                break;
        }
    }
        
    /**
     * Generates the parameters for an easy game.
     * Passes them to the creator function.
     * @param gridSize int size of the grid to be created
     */
    private void easyGame(int gridSize) {
        double totalTiles = (gridSize*gridSize);
        totalTiles = (totalTiles/10);
        creator(gridSize, (int) totalTiles);
    }
    
    /**
     * Generates the parameters for a medium-difficulty game.
     * Passes them to the creator function.
     * @param gridSize int size of the grid to be created
     */
    private void mediumGame(int gridSize) {
        double totalTiles = (gridSize*gridSize);
        totalTiles = (totalTiles/10)*2;
        creator(gridSize, (int) totalTiles);
    }

    /**
     * Generates the parameters for a hard game.
     * Passes them to the creator function.
     * @param gridSize int size of the grid to be created
     */
    public void hardGame(int gridSize){
        double totalTiles = (gridSize*gridSize);
        totalTiles = (totalTiles/10)*2.5;
        creator(gridSize, (int) totalTiles);
    }
       
    /**
     * Creates a new game using passed params.
     * Returns null if size is out of bounds.
     * Handles params which are invalid.
     * @param size gridSize
     * @param fill number of solid tiles to fill
     */
    private void creator(int size, int fill){
        try{
            userParamGridInit(size, fill);
        }catch(NullPointerException e){
            defaultGridInit();
        }
    }
    
    public void overrideCounterLimit(){
        counterLimBool = !counterLimBool;
    }
    
    public boolean isDFSLimOn(){
        return counterLimBool;
    }
    
    /**
     * Creates a grid using the passed user parameters.
     * @param size grid size
     * @param fill number of solid tiles to fill
     */
    private void userParamGridInit(int size, int fill){
        
        int counter = 0;
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Sorry for the wait");
        alert.setHeaderText("The game will load I promise");
        alert.setContentText("Give it a minute");
        puzzle = puzzle.randomPuzzle(size, fill/2, fill, fill);
        boolean breaker = true;
        if(counterLimBool){
            counterLimit = 6000;
        }else{
            counterLimit = (100000000);
        }
        while(!isPuzzleFinishable() && breaker){
            counter++;
            puzzle = puzzle.randomPuzzle(size, fill/2, fill, fill);
            if(counter == 2000){
            alert.show();
            }
            if(puzzle.getSize() > 13 && counter == counterLimit){
                puzzle = puzzle.randomPuzzle(10, fill/2, fill, fill);
                while(!isPuzzleFinishable()){
                    puzzle = puzzle.randomPuzzle(10, fill/2, fill, fill);
                }
                alert.close();
                Alert alertTwo = new Alert(Alert.AlertType.CONFIRMATION);
                alertTwo.setTitle("The algorithm took too long sorry :(");
                alertTwo.setHeaderText("Turn off the limit in options if you really want!");
                alertTwo.setContentText("Please try some a new difficulty/size");
                Optional<ButtonType> result = alertTwo.showAndWait();
                break;
            }
        }
        Window window = alert.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide()); 
    }
    
    /**
     * Creates a grid using the default parameters.
     */
    public void defaultGridInit(){
        puzzle = puzzle.randomPuzzle(4,1,1,1);
        while(!isPuzzleFinishable()){
            puzzle = puzzle.randomPuzzle(4,1,1,1);
        }
    }
    
    /**
     * Adds 1 to totalMoves field and adds passed params to
     * saves List.
     * @param row row to be saved
     * @param column column to be saved
     * @param symbol symbol to be saved
     */
    public void addMove(int row, int column, char symbol){
        totalMoves++;
        if(symbol == '-'){
            moves.add(new Command(CommandWord.CLEAR, row, column));
        }
        if(symbol == 'X'){
            moves.add(new Command(CommandWord.MARKX, row, column));
        }
        if(symbol == 'O'){
            moves.add(new Command(CommandWord.MARKO, row, column));
        }
    }
    
    /**
     * This function uses the passed parameters to retrieve a tile,
     * add its current symbol to the moves List, then using a 
     * pre-determined pattern, switches the symbol.
     * @param coordinates ArrayList of pairs
     */
    public void switchTile(ArrayList<Integer> coordinates){
        addMove(coordinates.get(0),coordinates.get(1),puzzle.getTileSymbol(coordinates.get(0), coordinates.get(1)));
        switch (puzzle.getTileSymbol(coordinates.get(0), coordinates.get(1))){
            case '-':
                execute(new Command(CommandWord.MARKO, coordinates.get(0), coordinates.get(1)));
                break;
            case 'O':
                execute(new Command(CommandWord.MARKX, coordinates.get(0), coordinates.get(1)));
                break;
            case 'X':
                execute(new Command(CommandWord.CLEAR, coordinates.get(0), coordinates.get(1)));
                break;
        }
    }

    /**
     * Retrieves previous moves from moves List and executes it.
     * If list is empty throws exception.
     */
    public void undoMove(){
        if(moves.size() > 0){
            Command command = moves.get(moves.size()-1);
            execute(command);
            moves.remove(moves.size()-1);
        }else{
            throw new NullPointerException();
        }
    }
    
    /**
     * @return true if current grid layout is legal, false otherwise.
     */
    public boolean isGridLegal(){
        return puzzle.isLegal(0);
    }
    
    /**
     * @return true if there are no empty tiles and the current grid 
     * layout is legal false otherwise.
     */
    public boolean isGameDone(){
        return puzzle.isPuzzleComplete() && puzzle.isLegal(0);
    }
    
    /**
     * This function creates a stack of X and O commands to execute on
     * a generated grid. It then executes the commands and checks whether
     * each move is legal. If it is not, evaluateGrid is called to
     * evaluate potential solutions.
     * @return true if there is a solution, false otherwise.
     */
    public boolean isPuzzleFinishable(){
        
        Stack<Command> commandStack = new Stack<>();
        MPTile[][] grid = puzzle.getGrid().clone();
        String before = puzzle.toString();
        
        forAllEmpty(commandStack, grid);
        
        while(!commandStack.isEmpty()){
            Command current = commandStack.pop();
            execute(current);
            boolean gridLegal = puzzle.isLegal(current.getRow());
            //called here to avoid having ^ algorithm run twice
            if(puzzle.isPuzzleComplete() && gridLegal){
                System.out.print("After  :" + puzzle.toString() + "\n");
                puzzle.clearMarkedTiles();
                System.out.print("Before :" + before + "\n");
                System.out.print("After  :" + puzzle.toString() + "\n");
                return true;
            }else if(!gridLegal){
                if(!puzzle.evaluateGrid(current.getRow()) && current.getCommand() == CommandWord.MARKX){
                    System.out.print("Before :" + before + "\n");
                    System.out.print("After  :" + puzzle.toString() + "\n");
                    return false;
                }
            }
        }      
        return false;
    }
    
    /**
     * Creates a stack of commands ready to be used.
     * @param commandStack stack to generate
     * @param grid grid to push commands on
     */
    public void forAllEmpty(Stack commandStack, MPTile[][] grid){
        for (int row = 0 ; row < grid.length ; row++) {
            for (int column = 0; column < grid.length ; column++){
                if(grid[row][column].isEditable()){
                    commandStack.push(new Command(CommandWord.MARKX, row, column));
                    commandStack.push(new Command(CommandWord.MARKO, row, column));
                }
            }
        }
    }

    /**
     * @return char[][] symbols of current grid
     */
    public char[][] getSymbols(){
        
        int size = puzzle.getSize();
        char[][] output = new char[size][size];
        for (int row = 0 ; row < size ; row++) {
            for (int column = 0; column < size ; column++) {
                output[row][column] = puzzle.getTileSymbol(row, column);
            }
        }
        return output;
    }
    
    /**
     * @return ArrayList of tile coordinates
     */
    public ArrayList<Integer> getIllegalTiles(){
        return puzzle.illegalities();
    }
    
    /**
     * @return int number of moves
     */
    public int getMoves(){
        return totalMoves;
    }
    
    /**
     * @return int current grid size
     */
    public int getGridSize(){
        return puzzle.getSize();
    }
    
    //=============================================================//
    // Save Game functionality
    
    /**
     * Connection between GUI and load game functionality.
     * This function loads the game into the SaveHandler class.
     * @param selectedIndex selected game to load.
     * @return true if game can be loaded, false otherwise.
     */
    public boolean loadGame(int selectedIndex) {
        try {
            saveHandler.loadGame(selectedIndex);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    
    /**
     * This function retrieves the loaded game from the SaveHandler
     * class.
     */
    public void startSavedGame(){
        totalMoves = saveHandler.getSaveMoves();
        char[][] symbols = saveHandler.getSaveSymbols();
        puzzle = new MarupekeGrid(saveHandler.getSaveSize());
        int i = 0;
        moves.clear();
        for (int row = 0 ; row < symbols.length ; row++) {
            for (int column = 0; column < symbols.length ; column++) {
                puzzle.setSavedTile(row, column, symbols[row][column]);
            }
        }
    }
        
    /**
     *
     * @param fileName name of file to save
     */
    public void saveGrid(String fileName){
        saveHandler.addSave(totalMoves, fileName, puzzle);
    }
    
    /**
     * Connection between GUI and save game functionality
     * @param index save to delete
     */
    
    public void deleteGame(int index) {
        saveHandler.deleteSave(index);
    }
    
    /**
     * Updates saved color file.
     * Used by GUI.
     * @param current current color
     * @return int opposite color choice
     */
    public int switchColor(int current){
        if(current == 1){
            saveHandler.saveColor(2);
            return 2;
        }else{
            saveHandler.saveColor(1);
            return 1;
        }
    }
    
    /**
     * @return int saved color choice: 1 or 0
     */
    public int getColor(){
        return saveHandler.getColorChoice();
    }
    
    /**
     * @return String[] of the names of current saved games
     */
    public String[] getSaveNames() {
        return saveHandler.getSaves();
    }

    /**
     * Used by GUI to display current saved games in TableView.
     * @return ObservableList of save objects.
     */
    public ObservableList<Save> getVisibleSaveList() {
        return saveHandler.getVisibleList();
    }
}
