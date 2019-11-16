package MarupekeGUI;

import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;

/**
 *
 * @author 164645
 * @version 04/29/2018
 * This class creates a tile/button which can be
 * manipulated by the GUITileGrid class.
 * It also holds an event handler for user clicks.
 */
public class GUITile{
    
    private char symbol;
    private boolean editable;
    private boolean legal = true;
    private ArrayList<Integer> coordinates = new ArrayList<>();
    private String[] editableColors = new String[3];
    private String[] unEditableColors = new String[3];
    private Button tile = new Button();
    private final int TILE_SIZE = 50;
    
    /**
     * Class constructor:
     * Sets coordinates,
     * Sets symbol,
     * Sets tile colors,
     * Sets event handler.
     * @param symbol symbol to set
     * @param row coordinate
     * @param column coordinate
     * @param color color to set
     */
    public GUITile(char symbol, int row, int column, int color){
        coordinates.add(row);
        coordinates.add(column);
        tile.setMinSize(TILE_SIZE, TILE_SIZE);
        if(color == 1){
            setBlack(editableColors);
            setWhite(unEditableColors);
        }else{
            setWhite(editableColors);
            setBlack(unEditableColors);
        }
        setSymbol(symbol);
        tile.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                switchSymbol();
            }
        });
    }
    
    /**
     * Sets button colors to white.
     * @param colors array
     */
    public void setWhite(String[] colors){
        colors[0] = "-fx-background-color: white;";
        colors[1] = "-fx-text-fill: black;";
        colors[2] = "-fx-border-color: white;";
    }
    
    /**
     * Sets button colors to black.
     * @param colors array
     */
    public void setBlack(String[] colors){
        colors[0] = "-fx-background-color: black;";
        colors[1] = "-fx-text-fill: white;";
        colors[2] = "-fx-border-color: white;";
    }
    
    /**
     * Sets the symbol displayed on the button.
     * @param symbol char to set
     */
    public void setSymbol(char symbol){
        this.symbol = symbol;
        switch (symbol){
            case '#':
                tile.setText("#");
                setUneditable();
                break;
            case 'x':
                tile.setText("X");
                setUneditable();
                break;
            case 'o':
                tile.setText("O");
                setUneditable();
                break;
            case 'X':
                tile.setText("X");
                setEditable();
                break;
            case 'O':
                tile.setText("O");
                setEditable();
                break;
            case '-':
                tile.setText("");
                setEditable();
                break;
        }
    }
    
    /**
     * Sets tile uneditable.
     */
    private void setUneditable(){
        editable = false;
        tile.setStyle(unEditableColors[0] +
                      unEditableColors[1] +
                      "-fx-padding: 10;" + 
                      "-fx-border-width: 1;" +
                      unEditableColors[2] );
    } 
    
    /**
     * Sets tile editable.
     */
    public void setEditable(){
        editable = true;
        tile.setStyle(editableColors[0] +
                      editableColors[1] +
                      "-fx-padding: 10;" + 
                      "-fx-border-width: 1;" +
                      editableColors[2] );
    }
    
    /**
     * Sets tile illegal.
     */
    public void setIllegal(){
        if(editable){
            legal = false;
            tile.setStyle(editableColors[0] +
                          "-fx-text-fill: red;" +
                          "-fx-padding: 10;" + 
                          "-fx-border-width: 2;" +
                          "-fx-border-color: red;");
        }
    }
    
    /**
     * Sets tile legal.
     */
    public void setLegal(){
        if(editable){
            setEditable();
        }
    }
    
    /**
     * Switches symbol according to specified pattern.
     * Updates corresponding tile in marupeke game through MarupekeGUI.
     */
    private void switchSymbol() {
        if(editable){
            switch (this.symbol){
                case '-':
                    this.symbol = 'O';
                    tile.setText("O");
                    break;
                case 'O':
                    this.symbol = 'X';
                    tile.setText("X");
                     break;
                case 'X':
                    this.symbol = '-';
                    this.tile.setText("");
                    break;
            }
            MarupekeGUI.updateGame(coordinates);
        }
    }
    
    /**
     * Sets tile uneditable.
     */
    public void gameDone(){
        editable = false;
    }
    
    /**
     * @return styled tile.
     */
    public Button button(){
        return tile;
    }
}
