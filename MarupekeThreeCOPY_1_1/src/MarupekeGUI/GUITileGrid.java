package MarupekeGUI;

import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 *
 * @author 165645
 * @version 04/29/2018
 * This class allows for there to be a static GUITile grid
 * that is manipulated by MarupekeGUI.
 */
public class GUITileGrid{
    
    private GUITile[][] buttonGrid;
    private ArrayList<Integer> currentIllegalTiles = new ArrayList<>();
    
    /**
     * Default constructor.
     */
    GUITileGrid(){}
    
    /**
     * Sets grid according to passed params.
     * @param size grid size
     * @param symbols grid symbols
     * @param color grid color
     */
    public void setGrid(int size, char[][] symbols, int color){
        buttonGrid = new GUITile[size][size];
        currentIllegalTiles.clear();
        for(int row = 0 ; row < size ; row++){
            for(int column = 0 ; column < size ; column++){
                buttonGrid[row][column] = new GUITile(symbols[row][column], row, column, color);
            }
        }
    }
    
    /**
     * GUITileGrid to gridPane conversion
     * @return gridPane buttonGrid[][] as gridPane
     */
    public GridPane getGrid(){
        GridPane gridPane = new GridPane();
        for(int row = 0 ; row < buttonGrid.length ; row++){
            for(int column = 0 ; column < buttonGrid.length ; column++){
                gridPane.add(buttonGrid[row][column].button(), column, row);
            }
        }
        return gridPane;
    }
    
    /**
     * Sets illegal tiles using passed list
     * @param illegalTiles int coordinate pairs i.e. i,i+1
     */
    public void setIllegalTiles(ArrayList<Integer> illegalTiles) {
        this.currentIllegalTiles = illegalTiles;
        for(int i = 0 ; i < illegalTiles.size() ; i+=2){
            buttonGrid[illegalTiles.get(i)][illegalTiles.get(i+1)].setIllegal();
        }
    }
    
    /**
     * Resets illegal tiles using local list.
     */
    public void clearIllegalTiles(){
        for(int i = 0 ; i < currentIllegalTiles.size() ; i+=2){
            buttonGrid[currentIllegalTiles.get(i)][currentIllegalTiles.get(i+1)].setLegal();
        }
    }
    
    /**
     * Sets whole grid uneditable.
     */
    public void gameDone(){
        for(int row = 0 ; row < buttonGrid.length ; row++){
            for(int column = 0 ; column < buttonGrid.length ; column++){  
                buttonGrid[row][column].gameDone();
            }
        }
    }
}
