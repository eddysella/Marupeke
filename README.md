# Marupeke
Marupeke the game

Displaying the data: 

How does the GUI display data?

Control Tabs:
On the left of the grid there is a saved games tab. This tab displays a List retrieved from SaveHandler which contains the currently saved games.
On start up, SaveHandler reads the saved names file and processes them into an ObservableList format for a Table View.

Grid:
The MarupekeGUI class creates and stores a static instance of the GUITileGrid class.
GUITileGrid allows for the generation of the GUITiles to happen outside of the MarupekeGUI class.
Once the GUITileGrid class has generated the GUITiles, they are retrieved one by one from the array and stored in a gridPanel.
The finished panel is returned to MarupekeGUI and displayed.

Each GUITile is a button which displays a text symbol and has styled colors according to the current status of that tile, i.e. legal/not

Top Status Bar:
The status bar displays the current game status, i.e. number of moves and whether the grid is legal.
It uses a gridPane for format, a red/white button for legal status and a Label for the number of moves.

Window:
The Window uses a BorderPane format to display everything.
Left: Control Tabs
Middle: Game Grid
Top: Game Status

How is data fetched?
All data from the puzzle is retrieved at runtime using get functions to ensure correct/updated values.
This implies that there are no fields which hold marupeke data, the get function is called directly instead.

How is it updated?
GUITile has an event handler connected to mouse-right-click.
On click, the symbol is switched and a static update method is called which is connected to the GUI and in turn connected to the marupeke game.
In the GUI, this call updates the top status board and checks whether the game is finished.
In marupeke this call, using passed coordinates, updates a symbol in the grid.

Extras:

Undo Move:
Using an ArrayList of previous moves, the last move in the list is executed to undo the current move.
Stops when it gets to 0 previous moves
Does not count undo as a move.

Saved Games:
A table view displays the currently saved games with their corresponding move number.
Below, a dropdown menu allows a game to be selected and launched.
Right clicking on the table allows the user to delete a game.
At the bottom there is a field for the name of a new save game.
DUPLICATE NAMES WILL BE OVERWRITTEN.
The Save package contains the files for this feature. Several supporting functions are in Marupeke

Invert Colors:
In the options tab a button allows the user to invert colors.
The color option is saved using SaveHandler. - A color options file is created for accessibility in the future, i.e. integration for 
other color-related features.
