package Game;

/**
 * This class holds a symbol and a boolean value for each grid tile.
 * It also includes function to access and set these private values.
 * Every function is very explicit in its inputs and outputs.
 * @author 164645
 * @version 04/29/2018
 */
public class MPTile {

    private Symbol symbol;

    /**
     * Default constructor.
     */
    public MPTile(){
        this.symbol = symbol.BLANK;
    }  
    
    /**
     * Sets tile symbol to passed param.
     * @param symbol symbol to set
     */
    public void setSymbol(Symbol symbol){
        this.symbol = symbol;
    }
    
    /**
     * @return tile symbol as char
     */
    public char getSymbol() {
        switch (symbol){
            case BLANK :
                return '-';
            case SOLID :
                return '#';
            case X :
                return 'X';
            case O :
                return 'O';
            case o :
                return 'o';
            case x :
                return 'x';
            default:
                return '-';
        }
    }

    /**
     * Used by isThreeInArow function in conjuction with isSymbol.
     * Returns X or O or empty.
     * @return char symbol
     */
    public char tileCheck(){
        switch (symbol){
            case x :
            case X :
                return 'X';
            case o :
            case O :
                return 'O';
            default:
                return '-';
        }
    }
    
    /**
     * @return true if the tile has a symbol, false otherwise
     */
    public boolean isSymbol(){
        return symbol != symbol.SOLID &&
               symbol != symbol.BLANK;
    }
    
    /**
     * @return true if the tile has a symbol which is editable
     */
    public boolean isEditable() {
        return this.symbol == symbol.O ||
               this.symbol == symbol.X ||
               this.symbol == symbol.BLANK;
    }
}