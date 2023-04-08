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

High scores for each difficulty (default and custom) are saved in a file for
persistence between sessions, along with the most recently-played board and
most recently-entered custom board parameters. The location where this file is
stored is by default the directory where the .jar file is located, but may be
different depending on the system. For example, on the Linux machine used for
development, the save file is placed in the home directory.

JMinesweeper uses objects to represent the game state. Mine-containing cells
and non-mine-containing cells are represented by classes that extend the
abstract Cell class, with polymorphic methods for click operations and state
variable access. These Cells are stored in a Board object, which provides all
the methods a Game object needs to process the game logic for each input. The
user interface is also split into encapsulated objects, each handling a
particular part of the UI.

## Rules of the Game

A Minesweeper board consists of an array of squares. Each cell in the grid is
either safe (clear, empty) or is a mine. All cells start out hidden. 
Left-clicking on a cell reveals it, and if a mine is revealed, the game is
lost. A left-click to start the game is always guaranteed to not lose the game,
and if the "First click always blank" option is enabled, it is guaranteed that
there is no mine adjacent to the first click. If the first click is a right
click, any subsequent left click may reveal a mine.

Revealed non-mine cells may display a number. The number on the cell tells you
how many mines are adjacent to the cell (horizontally, vertically, and 
diagonally). This number can range from 1 to 8. If there are no mines
adjacent to a cell, no number is displayed. Clicking on a cell with no mines
next to it will also reveal all of its neighbors, and if any of those neighbors
are not next to any mines, the process repeats.

The goal of the game is to reveal every safe cell, leaving only mines on the
board. Determine which cells are mines and which cells are safe using the
numbers on the revealed cells. You can mark cells that you're sure are mines
by right-clicking on them. This will change the cells' icons to flags. Flagged
cells cannot be clicked until the flag is removed. Optionally, you can also
mark cells with question marks to indicate that the cell may be a mine, but you
aren't certain. Question marks are purely a visual aid. Cells marked with
question marks behave exactly the same as blank hidden cells. When question
marks are enabled, right-clicking a cell cycles between flag, question mark,
and blank. When they are disabled, right-clicking simply toggles the flag on
and off.

When a revealed cell has the same number of flags next to it as the number
shown on the cell, you may quickly reveal all other non-flagged adjacent cells
by performing a chord click. The chord click is performed by pressing the left
and right mouse buttons at the same time, or by clicking the middle mouse
button if present. If your flags were placed incorrectly, the chord click may
result in a mine being clicked, losing the game. There is an option in the game
to enable chord clicking by simply left-clicking on revealed cells instead of
requiring multiple buttons at once or a third button, useful for devices with
only one mouse button.

Optionally, the chord click can also be used to set flags by clicking on a
revealed cell with the same number of hidden neighbors as the number shown
on the cell. This is toggled in the options menu. If left-click chord clicking
is enabled, the left click works with chord flagging as well.

## Implementation-Specific Features

While jMinesweeper is designed to behave almost identically to the original
version bundled with Windows by default, it has several options that were not
present in the original. These options can be toggled mid-game in the options
menu.

In the original minesweeper, while the player's first click was guaranteed to
not be a mine, the first click could still only reveal one cell. Most cases,
guessing was required after only one click. jMinesweeper has the option
"First click always blank". When this option is enabled, the first click is 
guaranteed to reveal a blank cell with no number, revealing all of its neighbor
cells along with it. The minimum number of cells revealed by the first click is
4, which occurs when the click is made in a corner, and each of the three cells 
adjacent to this click is itself adjacent to a mine.

jMinesweeper also has an option to perform the chord-click operation by 
left-clicking an already-revealed cell. Present in many modern online and
mobile versions of the game, this option is off by default in jMinesweeper, 
in order to match the behavior of the original Windows version.

jMinesweeper also optionally supports placing flags using the chord click
operation when the number of hidden cells adjacent to a revealed cell matches
the number of adjacent mines. Like left-click chording, this feature is present
in some modern versions of the game but absent in the original, so it's
disabled by default.

A feature present in the original Windows Minesweeper but uncommonly used by
players and absent from some modern versions, jMinesweeper supports marking
cells with question marks. Question marks can be disabled in the options menu,
and disabling question marks while there are question marks on the board turns
all existing question marks into blank hidden cells. To match the behavior of
the original, question marks are on by default.

Finally, the auto-flag that happens when the player reveals the last non-mine
cell can be turned off by unchecking the "Auto-flag last cells" option,
forcing the player to flag every cell before the game is won. By default, 
auto-flagging is enabled, as nearly every version of the game displays this
endgame behavior.

## Known Bugs

On some platforms (bug found on macOS), board updates that change many cells'
graphics may initially leave a seemingly random number and distribution of
cells not updated, and update after a few seconds.

On some default look and feels (particularly macOS), a redundant click
animation is shown where the button, including the icon that takes up the
button's entire footprint, is darkened. 

On some Linux windowing systems (Issue was found on GNOME Desktop), the frame
sometimes doesn't resize and reposition correctly. This only happens when the
custom board option is selected, even though they use the same code to reset
the frame, and it still happens when a default difficulty value is hardcoded
for testing purposes, so it must have something to do with the windowing 
system's processing of the closing of the dialogue box, then the resizing and
repositioning of the frame in rapid succession. Previously, this bug could
result in a broken-looking window of the wrong size, but in the right position.
In the most recent update, an extra pack() call was added, which makes the
frame the right size but in the wrong location. I have no idea how a call to
pack() could retroactively change the position of a JFrame to where it was
previously, but that seems to be what's happening.

Please open an issue if you encounter a new bug!