package Control;

/**
 * @author ianw
 * @author 164645
 * A representation of what the user wants the game to do next.   
 */
public class Command{
    
    private CommandWord command;
    private int row = 0;
    private int column = 0;
    private int gridSize;
    
    /**
     * New game command
     * @param command difficulty
     * @param size grid size
     */
    public Command(CommandWord command, int size){
        this.command = command;
        this.gridSize = size;
    }
    
    /**
     * New mark command
     * @param command command to execute
     * @param row coordinate
     * @param column coordinate
     */
    public Command(CommandWord command, int row, int column) {
        this.command = command;
        this.row = row;
        this.column = column;
    }
    
    /**
     *
     * @return grid size
     */
    public int getSize(){
        return gridSize;
    }
    
    /**
     *
     * @return command word
     */
    public CommandWord getCommand() {
        return command;
    }

    /**
     * @return selected row
     */
    public int getRow() {
        return row;
    }

    /**
     *
     * @return selected column
     */
    public int getColumn() {
        return column;
    }	
    
}
