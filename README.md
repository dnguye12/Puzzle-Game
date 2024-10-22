# Puzzle Game Project
###### Developpers : Duc-Huy NGUYEN, Martinn GADET

#### Work seperation
Huy work on the puzzle panel and game

Martin work on the menu and the window.

## Main window
### MenuBar
The menu bar contains 2 buttons : 
- "Quit" that close and window and stop the game
- "New Game" that open a form to set up the game

### Options Panel
The options panel show :
- the player name
- the move counter
- the timer
- a play/pause button that stop the timer
- a help button that show on the puzzle if pieces are well placed

### Game Panel
The Game Panel show the different pieces of the puzzle that you can rotate or drag and drop.
To rotate or drag and drop you need to select the piece bi clicking on it.
Rotation interactions :
- wheel click to show a circle menu and click on the rotation that you want
- drag wheel to turn the piece
- press 'R' ro rotate the piece

### Popup form
When "New Game" button is pressed a file chooser and a popup form will appear to setup different variables of the game :
- PlayerName
- Image Path
- Difficulty
- Custom difficulty number

Popup show a "Start Game" button to start the game with the current variables values and image.
You can also use a "fun popup" to setup those variables.

### Fun Popup Form
This popup show new ways to setup variables :
- Letter slider to write the player name
- Little mario like game to choose the difficulty
- Counter to choose the custom difficulty

It is possible to go from one popup to another and keep the current variables content.

### Controllers
2 main control mode: mouse control and keyboard control.
    - Mouse control:
        + Left click:
            * If no cell is selected, select the cell being clicked on.
            * Else deselect the current selected cell, then select the new one instead.
        + Scroll Button CLICK: Open the rotation menu for the selected cell.
        + Right click: swapping cells or deselect current cell.
        + Drag and drop selected cell to swap it with another cell.
        + Scroll: Change selected cell rotation.
    - Keyboard control:
        + Spacebar: Toggle on/off keyboard controller mode, this will draw the cursor on the screen.
        + Arrow keys: Move the cursor around.
        + Enter:
            * If nothing is selected, select the current cell
            * If something is already selected, swap with another cell
            * If you hit enter on an already selected cell, unselect this cell.
        + A/E (or Q/E for non French keyboard) to rotate the selected cell.