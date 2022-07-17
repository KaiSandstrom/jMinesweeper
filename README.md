# jMinesweeper

## Overview

jMinesweeper is a clone of the original Windows Minesweeper, written in Java 
using Swing. 

It currently supports three difficulties:

* Easy, a 9x9 board with 10 mines
* Intermediate, a 16x16 board with 40 mines
* Expert, a 16x30 board with 99 mines

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

jMinesweeper currently only supports three hardcoded board sizes, each with
a hardcoded number of mines. A custom board option is planned for the near
future.

Also coming soon is a high score feature, where players can log their fastest
times for each board type.

## Known Bugs

Occasionally, when a full click (including press and release of the mouse
button) is made on a cell, only the press is registered, and the "blank 
revealed" icon from the click animation remains on the cell until it is
clicked again. In this state, no game logic is invoked until the cell is
finally clicked again. As far as I can tell, this is caused by the
mouseClicked MouseEvent not firing, and I don't know what I could do to fix
it at this time.

Missed MouseEvents seem to be particularly prevalent on Windows, or at least on
the one Windows machine I used to test v1.0.0. On this machine, in addition to
left clicks, right clicks were frequently not detected as well. I could not
determine the conditions under which this occurs. The application ran much more
smoothly on the Linux machine where development occurred.

Another much more rare bug is that occasionally, the game window doesn't
resize properly, resulting in either board with a broken border and
oddly-spaced-out buttons, or a window with missing content. I previously
encountered this issue when I was trying to resize the JFrame while it was not
visible. Since fixing that particular cause, I have only seen it once, and that
was while changing difficulty with the window dragged near the edge of the
screen.
