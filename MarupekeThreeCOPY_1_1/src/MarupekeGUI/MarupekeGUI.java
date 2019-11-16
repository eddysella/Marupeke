package MarupekeGUI;

import Save.Save;
import Control.CommandWord;
import Game.Marupeke;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Creates an interface with which the user can interact with the game.
 * @author 164645
 * @version 04/29/2018
 */
public class MarupekeGUI extends Application {

    private static BorderPane root = new BorderPane();
    private static Pane gameStatus = new Pane();
    private static TabPane controlsTabPane = new TabPane();
    private static Tab newGameTab = new Tab("Current");
    private static Tab savedGameTab = new Tab("Saved");
    private static Tab optionsTab = new Tab("Options");
    private static TextField newSizeField = new TextField();
    private static TextField newSaveName = new TextField();
    private static ComboBox chosenSaveGame = new ComboBox();
    private static Marupeke marupeke = new Marupeke();
    private static GUITileGrid buttonGrid = new GUITileGrid();
    private static int colorChoice;
    private final int DEF_SIZE = 10;
    private final CommandWord DEF_DIFFICULTY = CommandWord.EASY;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }   
    
    /**
     * Initializes GUI and all GUI elements.
     */
    @Override
    public void start(Stage primaryStage){
        
        marupeke.Start(DEF_SIZE, DEF_DIFFICULTY);
        colorChoice = marupeke.getColor();
        buttonGrid.setGrid(DEF_SIZE, marupeke.getSymbols(), colorChoice);
        
        newGameTab.setContent(setNewGameTab());
        savedGameTab.setContent(setSavedGamesTab());
        optionsTab.setContent(setOptionsTab());
        controlsTabPane.getTabs().addAll(newGameTab, savedGameTab, optionsTab);
        
        root.setLeft(controlsTabPane);
        root.setTop(setGameStatus());
        root.setCenter(buttonGrid.getGrid());
        
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    /**
     * Creates a new BorderPane and adds a size text field for the user to 
     * enter a new game size. Also add three pre-built difficulty buttons.
     * for easy creation.
     * @return created Pane
     */
    private Pane setNewGameTab() {
        
        newSizeField.setPrefColumnCount(2);
        
        BorderPane controlPane = new BorderPane();
        controlPane.setPadding( new Insets(30,30,30,30));
        
        GridPane formPane = new GridPane();
        formPane.add(new Label ("Game Difficulty:"), 0, 0);
        formPane.add(new Label("Size:"), 0, 1);
        formPane.add(newSizeField, 1, 1);
        formPane.add(setDifficultyButtons(), 1, 0);
        Text t = new Text();
        t.setText("8*8 and below should all be instant. \n" +
                "Above that it can take up to 30 secs. \n" +
                "Size Boundaries: 4-infinite\n" +
                "Only try 15+ if you have a beefy cpu ;).");
        t.setStyle("-fx-padding: 30");
        
        formPane.add(t, 0, 2);
        
        controlPane.setLeft(formPane); 
        return controlPane;
    }
        
    /**
     * Creates a table to display saved game and facilitate delete
     * game functionality. Also adds a load/save game button.
     * @return generated Pane
     */
    private Pane setSavedGamesTab() {
        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        bp.setTop(tableOfSaves());
        
        gp.add(new Label("Game: "), 0, 1);
        chosenSaveGame.getItems().addAll((Object[]) marupeke.getSaveNames());
        gp.add(chosenSaveGame, 1, 1);        
        gp.add(loadGameBtn(), 2, 1);
        gp.add(new Label("New Save:"), 0, 2);
        gp.add(newSaveName, 1, 2);
        gp.add(saveGameBtn(), 2, 2);
        
        bp.setCenter(gp);
        return bp;
    }
    
    /**
     * Generate a new table which holds displays currently saved games.
     * @return generated table of saves
     */
    private TableView tableOfSaves(){
    
        TableView tv = new TableView();
        tv.setItems(marupeke.getVisibleSaveList());
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Save, String> movesCol = new TableColumn("# of Moves");
        movesCol.setCellValueFactory(new PropertyValueFactory("moves"));
        tv.getColumns().addAll(nameCol,movesCol);
        ContextMenu menu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to delete this game?");
            alert.setContentText("It will be gone forever.");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                
                marupeke.deleteGame(tv.getSelectionModel().getSelectedIndex());
            }
        });
        tv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(t.getButton() == MouseButton.SECONDARY) {
                    menu.show(tv, t.getScreenX(), t.getScreenY());
                }
            }
        });
        menu.getItems().add(delete);
        return tv;
    }
    
    /**
     * Generates a button which loads a saved game.
     * @return start game button
     */
    private Button loadGameBtn(){
        Button startGame = new Button("Load Game");
        startGame.setOnAction(e -> {
            if (startAlert()){    
                if(marupeke.loadGame(chosenSaveGame.getSelectionModel().getSelectedIndex())){
                    marupeke.startSavedGame();
                    resetVisibleGrid();
                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Sorry");
                    alert.setHeaderText("Your game did not load");
                    alert.setContentText("Please use the left panel to load a different one");
                    alert.showAndWait();
                }
            }else{}
        });
        
        return startGame;
    }
    
    /**
     * Generates a button which saves a game
     * @return save game button
     */ 
    private Button saveGameBtn(){
        Button saveBtn = new Button("Save Game");
        saveBtn.setOnAction(e -> {
            marupeke.saveGrid(getNewFileName());
            chosenSaveGame.getItems().clear();
            chosenSaveGame.getItems().addAll((Object[]) marupeke.getSaveNames());
            newSaveName.setText("");
        });
        
        return saveBtn;
    }
    
    /**
     * @return new save name
     */
    private String getNewFileName(){
        return newSaveName.getText();
    }
    
    /**
     * Creates a BorderPane with a button to invert game colors.
     * @return generated Pane
     */
    private Pane setOptionsTab(){
        BorderPane options = new BorderPane();
        GridPane gp = new GridPane();
        
        Button turnOffDFSLim = new Button("Turn on/off auto-shut down DFS after 6000 iterations");
        turnOffDFSLim.setOnAction(e -> {
            marupeke.overrideCounterLimit();
            root.setTop(setGameStatus());
        });
        
        Button switchColor = new Button("Invert Game Colors");
        switchColor.setOnAction(e -> {
            colorChoice = marupeke.switchColor(colorChoice);
            resetVisibleGrid();
        });
        
        gp.add(turnOffDFSLim, 0, 1);
        gp.add(switchColor,0, 2);
        
        options.setCenter(gp);
        return options;
    }

    /**
     * Creates tab which warns the user if the grid is illegal, shows 
     * the number of moves made and has an undo button.
     * @return generate Pane
     */
    private static Pane setGameStatus() {
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10, 10, 10, 10));
        gp.setHgap(10);
        gp.setVgap(10);
        
        Button DFSLim = new Button("DFSLimit");
        if(marupeke.isDFSLimOn()){
            DFSLim.setStyle("-fx-background-color: green;" +
                            "-fx-text-fill: white;");
        }
        else{
            DFSLim.setStyle("-fx-background-color: red;" +
                            "-fx-text-fill: black;");
        }
        
        Button legal = new Button("Legal");
        if(marupeke.isGridLegal()){
            legal.setStyle("-fx-background-color: white");
        }
        else{
            legal.setStyle("-fx-background-color: red");
        }
        
        Label movesCounter = new Label ("Move Counter: " + marupeke.getMoves());
        
        Button undo = new Button("Undo");
        if(!marupeke.isGameDone()){
            undo.setOnAction(e -> {
                undoMove();
            });
        }
        
        gp.add(legal, 0, 0);
        gp.add(movesCounter, 1, 0);
        gp.add(undo, 2, 0);
        gp.add(DFSLim, 3, 0);
        
        gameStatus.getChildren().clear();
        gameStatus.getChildren().add(gp);
        return gameStatus;
    }
    
    /**
     * Allows user to undo a move. Does not count as a move.
     */
    public static void undoMove(){
        try{
            marupeke.undoMove();
            resetVisibleGrid();
        } catch (NullPointerException e) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("You have no more moves to undo");
            alert.setContentText("Please start the game");
            alert.showAndWait();
        }
    }
    
    /**
     * Create gridpane with difficulty buttons.
     * @return gridpane
     */
    private GridPane setDifficultyButtons(){
        GridPane gridPane = new GridPane();
        
        Button easy = new Button("Easy");
        easy.setOnAction(e -> {
            if(startAlert()){
                startGame(CommandWord.EASY);
            }
        });
        Button medium = new Button("Medium");
        medium.setOnAction(e -> {
            if(startAlert()){
                startGame(CommandWord.MEDIUM);
            }
        });
        Button hard = new Button("Hard");
        hard.setOnAction(e -> {
            if(startAlert()){
                startGame(CommandWord.HARD);
            }
        });
        
        gridPane.add(easy, 0, 0);
        gridPane.add(medium,1,0);
        gridPane.add(hard, 2, 0);
        
        return gridPane;
    }
    
    /**
     * Collects new game size and difficulty. 
     * Resets grid.
     * Resets text field.
     * @param difficulty difficulty chosen
     */
    private void startGame(CommandWord difficulty){
        int gameSize = Integer.parseInt(newSizeField.getText());
        marupeke.Start(gameSize, difficulty);
        resetVisibleGrid();
        newSizeField.setText("");
    }
    
    private boolean startAlert(){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You are about to quit your current game");
        alert.setContentText("You will lose all your current moves");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    /**
     * Sets visible grid to passed parameters. Checks game status
     */
    private static void resetVisibleGrid(){
        buttonGrid.setGrid(marupeke.getGridSize(), marupeke.getSymbols(), colorChoice);
        updateBoard();
        root.setCenter(buttonGrid.getGrid());
    }
    
    /**
     * Updates board status: isLegal or gameDone.
     * If its not legal, displays illegal tiles.
     */
    public static void updateBoard(){
        if(marupeke.isGameDone()){
            buttonGrid.gameDone();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setHeaderText("You have won a game!");
            alert.setContentText("Please use the left panel to begin a new one");
            alert.showAndWait();
        }else if(!marupeke.isGridLegal()){
            buttonGrid.setIllegalTiles(marupeke.getIllegalTiles());
        }
        root.setTop(setGameStatus());
    }
    
    //=============================================================
    //GUITile event handler
    
    /**
     * 'Event Handler' for GUITile clicks
     * @param coordinates coordinates of tile to change
     */
    public static void updateGame(ArrayList<Integer> coordinates){
        marupeke.switchTile(coordinates);
        buttonGrid.clearIllegalTiles();
        updateBoard();
    }
}
