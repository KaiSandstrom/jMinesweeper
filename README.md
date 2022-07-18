# jMinesweeper

## Overview

jMinesweeper is a clone of the original Windows Minesweeper, written in Java 
using Swing. 

It comes with three default difficulties:

* Easy, a 9x9 board with 10 mines
* Intermediate, a 16x16 board with 40 mines
* Expert, a 16x30 board with 99 mines

Custom boards can also be created, with user-defined numbers of rows, columns,
and mines.

JMinesweeper uses objects to represent the game state. Mine-containing cells
and non-mine-containing cells are represented by classes that extend the
abstract Cell class, with polymorphic methods for click operations and state
variable access. These Cells are stored in a Board object, which provides all
the methods a Game object needs to process the game logic for each input. The
user interface is also split into encapsulated objects, each handling a
particular part of the UI.

## Rules of the Game

The goal in a game of jMinesweeper is simple: Flag all mines, and reveal every
cell that isn't a mine. Left click to reveal a cell, and right click to flag a
cell as a mine. If you left-click on a mine, you lose the game.

Each non-mine cell that's revealed displays how many mines are adjacent to it.
If the revealed cell is blank, there are no mines adjacent to it. Otherwise,
the number displayed on the cell is the number of adjacent mines. Use these
numbers to determine which cells are mines and which cells are safe. In some
situations, it isn't possible to find out which cells are mines without
guessing. The game is won when every mine is marked as flagged and every
non-mine is revealed.

## Implementation-Specific Features

The mines are placed on the board randomly after the first click is made. If
the first click is a left click, no mines are placed in the region immediately 
surrounding the first click, ensuring that the first click reveals more than 
one cell. The minimum number of cells revealed by the first click is 4, which
occurs when the click is made in a corner, and each of the three cells adjacent
to this click is itself adjacent to a mine.

If a revealed cell is adjacent to the same number of flags as its displayed
number, its adjacent non-flagged cells can all be revealed at once by clicking
on the revealed cell in question. If the appropriate number of flags have not
been set adjacent to the cell, the click will have no effect (and won't even
give visual feedback on-click), and if the flags are set incorrectly, a mine
will be clicked resulting in a loss.

## Planned Features

A high score feature, where players can log their fastest times for each board
type, will be added in the near future.

## Known Bugs

On some platforms (bug found on macOS), board updates that change many cells'
graphics may initially leave a seemingly random number and distribution of
cells not updated, and update after a few seconds.

On some default look and feels (particularly macOS), a redundant click
animation is shown where the button, including the icon that takes up the
button's entire footprint, is darkened. 

jMinesweeper was also tested on linux and Windows, and neither of these
problems was present.
