/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Save;

/**
 *
 * @author 164645
 * @version 04/29/2018
 * This class holds information about a saved game. When not set
 * it only contains a name and number of moves.
 */

public class Save{
        
    private int moves;
    private String name;
    private String symbols;
    private int size;
    
    /**
     * Default constructor.
     */
    public Save(){}

    /**
     * Default save constructor.
     * @param numMoves number of moves previously executed
     * @param name name of save
     */
    public Save(int numMoves, String name) {
        this.moves = numMoves;
        this.name = name;
    }
        
    /**
     * Loads saved symbols and grid size.
     * @param size grid size
     * @param symbols grid symbols
     */
    public void setSave(int size, String symbols){
        this.size = size;
        this.symbols = symbols;
    }
        
    /**
     * @return string of symbols.
     */
    public String getSymbols(){
        return symbols;
    }
        
    /**
     * @return grid size
     */
    public int getSize(){
        return size;
    }
        
    /**
     * @return number of moves.
     */
    public int getMoves(){
        return moves;
    }
        
    /**
     * @return save name.
     */
    public String getName(){
        return name;
    }
}
