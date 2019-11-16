package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The MarupekeGrid class stores a randomly generated marupeke grid
 * in accordance to the passed parameters. It also allows
 * other classes to interact with the grid by using the 
 * supported functions.
 * @author 1646456
 * @version 05/10/2018
 */
public class MarupekeGrid 
{
    private MPTile[][] grid;// Grid with editable + symbol
    private List<Integer> illegalitiesList = new ArrayList<Integer>();// list with 
    
    /**
     * Default constructor.
     */
    public MarupekeGrid(){}
    
    
    //-----------------------------------------------------------------
    // Game Creation
    
    /**
     * class constructor initializes the MPTile grid using width param.
     * @param width is the width, i.e width * width, of the grid
     */               
    public MarupekeGrid(int width){
        grid = new MPTile[width][width];
        for (int row = 0 ; row < grid.length ; row++) {
            for (int column = 0; column < grid.length ; column++) {
                grid[row][column] = new MPTile();
            }
        }
    }
    
    /**
     * randomPuzzle first checks if the number of tiles filled is within
     * the specified constraint, else null is returned. 
     * The setTileLoop function is called using input params with
     * their respective symbol parameter
     * @param size is a double// width: width * width of the grid.
     * @param numFill solid squares in the grid.
     * @param numX naughts in the grid
     * @param numO crosses in the grid
     * @return either puzzle or null(check constraint)
     */
    public static MarupekeGrid randomPuzzle ( double size , int numFill , int numX, int numO ){
        
        MarupekeGrid puzzle = new MarupekeGrid((int) size);
        puzzle.setTileLoop(Symbol.SOLID, numFill);
        puzzle.setTileLoop(Symbol.x, numX);
        puzzle.setTileLoop(Symbol.o, numO);
        return puzzle;
    }
    
    /**
     * setTileLoop is used to set the number of tiles
     * specified by param SymbolCount. The symbol is chosen
     * through the symbol param.
     * @param symbolCount is the number of times the loop will iterate
     * @param symbol is the symbol that is trying to be set
     */
    public void setTileLoop(Symbol symbol, int symbolCount){
        
        Random random = new Random();
        int counter = 0;
        while(counter < symbolCount){
            
            int row = random.nextInt(grid.length);
            int column = random.nextInt(grid.length);    
            if(setTile(grid[row][column], symbol)){
                counter++;
            }
        }
    }
    
    /**
     *setTile temporarily stores the passed tile's boolean state,
     *and checks whether it is editable. If true, it will set
     *the symbol and boolean value to the passed parameters.
     * @param tile is the tile being accessed
     * @param symbol is the symbol that is trying to be assigned
     * @return the previously saved boolean state of the tile
     */
    public boolean setTile(MPTile tile, Symbol symbol){
        
        boolean editable = tile.isEditable();
        if(editable){
            tile.setSymbol(symbol);
        }
        return editable;
    }
    
    /**
     * Used by saveHandler to set a saved game tile.
     * @param row coordinate
     * @param column coordinate
     * @param symbol symbol to be set
     */
    public void setSavedTile(int row, int column, char symbol){
        switch(symbol){
            case 'X':
                setTile(grid[row][column], Symbol.X);
                break;
            case 'O':
                setTile(grid[row][column], Symbol.O);
                break;
            case 'x':
                setTile(grid[row][column], Symbol.x);
                break;
            case 'o':
                setTile(grid[row][column], Symbol.o);
                break;
            case '#':
                setTile(grid[row][column], Symbol.SOLID);
                break;
        }
    }
    
    //-----------------------------------------------------------------
    // Game Manipulation
    
    /**
     * Marks X in specified tile
     * @param row coordinate
     * @param column coordinate
     */
    public void markX(int row, int column){
        grid[row][column].setSymbol(Symbol.X);
    }
    
    /**
     * Marks O in specified tile
     * @param row coordinate
     * @param column coordinate
     */
    public void markO(int row, int column){
        grid[row][column].setSymbol(Symbol.O);
    }
    
    /**
     * Attempts to clear specified tile
     * @param row coordinate
     * @param column coordinate
     */
    public void tileClear(int row, int column){
        grid[row][column].setSymbol(Symbol.BLANK);
    }
    
    /**
     * Uses tileClear function in loop to clear marked tiles. 
     */
    public void clearMarkedTiles(){
        for (int row = 0 ; row < grid.length ; row++) {
            for (int column = 0; column < grid.length ; column++){
                if(grid[row][column].getSymbol() == 'X' ||
                   grid[row][column].getSymbol() == 'O'){
                    tileClear(row,column);
                }
            }
        }   
    }
    
    //-----------------------------------------------------------------
    // Game Status
    
    /**
     * isPuzzleComplete determines whether there are any blanks.
     * @return true if puzzle has no empty tiles and is legal / false otherwise
     */
    public boolean isPuzzleComplete(){
        for(int row = 0 ; row <= grid.length-1 ; row++){
            for(int column = 0 ; column <= grid.length-1 ; column++){
                if(grid[row][column].getSymbol() == '-'){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * isLegal determines grid legality using isThreeInRow(). 
     * If there are three in a row it returns false. 
     * *RESETS ILLEGALITIES LIST EVERY TIME*
     * @return true if the current grid is legal, false otherwise.
     */
    public boolean isLegal(int rowBound){
       
        illegalitiesList.clear();
        boolean isLegal = true;
        
        //checks grid-2 blocks down, right, and diagonal top left to bottom right
        for(int row = rowBound; row < grid.length-2 ; row++){
            for(int column = 0 ; column < grid.length-2 ; column++){
                if(isThreeInRow(oneToArray(row), threeToArray(column,column+1,column+2))){isLegal = false;}
                if(isThreeInRow(threeToArray(row,row+1,row+2), oneToArray(column))){isLegal = false;}
                if(isThreeInRow(threeToArray(row,row+1,row+2), threeToArray(column,column+1,column+2))){isLegal = false;}
             // if all three are true technically doesnt need diagonal check left    
            }
            // checks two furthest right columns going down
            if(isThreeInRow(threeToArray(row,row+1,row+2),oneToArray(grid.length-1))){isLegal = false;}
            if(isThreeInRow(threeToArray(row,row+1,row+2),oneToArray(grid.length-2))){isLegal = false;}
        }
        //checks two bottom rows left to right
        for(int column = 0 ; column < grid.length-2 ; column++){
            if(isThreeInRow(oneToArray(grid.length-1),threeToArray(column,column+1,column+2))){isLegal = false;}
            if(isThreeInRow(oneToArray(grid.length-2),threeToArray(column,column+1,column+2))){isLegal = false;}
        }
        //checks diagonal from top right to bottom left (backwards)
        for(int row = rowBound ; row < grid.length-2 ; row++){
            for(int column = grid.length-1 ; column > 1 ; column--){
                if(isThreeInRow(threeToArray(row,row+1,row+2),threeToArray(column,column-1,column-2))){isLegal = false;}
            }
        }
        return isLegal;
    }
    
    /**
     * isThreeInRow checks if the symbols in the three
     * tiles passed are the same. if there are three same symbols in a row
     * it passes the illegality to the addIllegality function for handling.
     * false otherwise
     * @param row coordinate
     * @param column coordinate
     * @return true if the same, false otherwise
     */
    public boolean isThreeInRow(ArrayList<Integer> row, ArrayList<Integer> column){
        if(grid[row.get(0)][column.get(0)].isSymbol()){
            if(grid[row.get(0)][column.get(0)].tileCheck() == grid[row.get(1)][column.get(1)].tileCheck() &&
               grid[row.get(1)][column.get(1)].tileCheck() == grid[row.get(2)][column.get(2)].tileCheck()){
                for(int i = 0 ; i < 3 ; i++){
                    illegalitiesList.add(row.get(i));
                    illegalitiesList.add(column.get(i));
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return a string array containing the illegalities of the current grid
     */
    public ArrayList<Integer> illegalities(){
        return new ArrayList<Integer> (illegalitiesList);
    }
                
    /**
     * Checks all possible combinations of adjacent illegal tiles.
     * There are three loops because the isLegal function works
     * by checking three tiles at once.
     * Most inner loop is allowed to edit solid tiles too to find an 
     * optimal solution rather than force one. This speeds
     * up DFS by a lot.
     * @return true if current layout is legal, false otherwise.
     */
    public boolean evaluateGrid(int currentRow){
        ArrayList<Integer> illegalities = new ArrayList<>(illegalitiesList);
        for(int i = 0; i < illegalities.size() ; i+=2){
            MPTile tileOne = grid[illegalities.get(i)][illegalities.get(i+1)];
            if(tileOne.isEditable()){
                nextMove(tileOne);
                if(isLegal(currentRow)){
                    return true;
                }else{
                    for(int j = 0; j < illegalities.size() ; j+=2){
                        MPTile tileTwo = grid[illegalities.get(j)][illegalities.get(j+1)];
                        if(tileTwo.isEditable()){
                            nextMove(tileTwo);
                            if(isLegal(currentRow)){
                                return true;
                            }else{
                                for(int k = 0; k < illegalities.size() ; k+=2){
                                    MPTile tileThree = grid[illegalities.get(k)][illegalities.get(k+1)];
                                    if(grid.length < 7 ? tileThree.isEditable():true){
                                        nextMove(tileThree);
                                        if(isLegal(currentRow)){
                                            return true;
                                        }else{
                                            nextMove(tileThree);
                                        }
                                    }
                                }
                                nextMove(tileTwo);
                            }
                        }
                    }
                    nextMove(tileOne);
                }
            }
        }
        return false;
    }

    /**
     * Template for next mark command to execute.
     * @param tile tile to mark
     * @param row row coordinate
     * @param column column coordinate
     */
    public void nextMove(MPTile tile){
        if(tile.tileCheck() == 'X'){
            tile.setSymbol(Symbol.O);
        }else{
            tile.setSymbol(Symbol.X);
        }
    }
    
    /**
    * @return returns grid as String
    */
    @Override
    public String toString(){
        
        String output = "";
        
        if(grid.length == 10){
            output += grid.length + ";"; 
        }else{
            output += 0 + grid.length + ";";
        }
        
        for(int row = 0 ; row < grid.length ; row++){
            for(int column = 0 ; column < grid.length ; column++){
                output = output + grid[row][column].getSymbol();
            }
        }
        return output;
    }
    
    /**
     * Takes three numbers returns an arrayList with them inside.
     * @param one int to add to array
     * @param two int to add to array
     * @param three int to add to array
     * @return ArrayList which contains params
     */
    public ArrayList<Integer> threeToArray(int one, int two, int three){
        ArrayList<Integer> threeNums = new ArrayList<>();
        threeNums.add(one);
        threeNums.add(two);
        threeNums.add(three);
        return threeNums;
    }
    
    /**
     * Takes one number returns an arrayList with it copied thrice.
     * @param one int to copy
     * @return ArrayList which contains param three times.
     */
    public ArrayList<Integer> oneToArray(int one){
        ArrayList<Integer> threeNums = new ArrayList<>();
        threeNums.add(one);
        threeNums.add(one);
        threeNums.add(one);
        return threeNums;
    }
    
    /**
     * Returns a tile symbol
     * @param row coordinate
     * @param column coordinate
     * @return char symbol
     */
    public char getTileSymbol(int row, int column){
        return grid[row][column].getSymbol();
    }
    
    /**
     * @return int grid size
     */
    public int getSize(){
        return grid.length;
    }
    
    /**
     * @return MPTile[][] current grid
     */
    public MPTile[][] getGrid(){
        return grid;
    }
}

