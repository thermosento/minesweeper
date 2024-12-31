=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: 43269270
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays - The game board is composed of a 2D array of Cells, which is a
  class I made to store the information that each Minesweeper cell needs to, like
  if it has a mine, if it has been flagged or uncovered, and how many mines are
  surrounding it. I iterate through the 2D array many times in the Minesweeper
  class, to initiate the cells, to place mines, and to set the number of
  surrounding mines for each tile.

  2. File I/O - I created two functions called saveToFile() and loadSavedFile(),
  where saveToFile converts the information in the 2D array and some of the
  variables in the Minesweeper class into comma separated values, which retain
  the structure of the 2D array and are then put into a CSV file. loadSavedFile()
  reads this CSV file and sets the game state to that of the one stored in the
  file, and allows the player to continue from the exact point that they saved from.
  The player is not allowed to save when there is no game in progress, and if
  they save while there is already a saved file, it will overwrite the saved file.

  3. Testable Component - I used JUnit testing to test the methods in my Minesweeper
  class. This worked out well because I designed my code to be unit-testable, with
  the methods all being separately testable. My feedback suggested doing Testable
  Component and File I/O rather than the other things I selected, so I went with them.

  4. Recursion - I used recursion in order to implement the logic in minesweeper
  where, when you click on an empty tile, it will reveal all the tiles without
  bombs around it. To do this, I called floodFillCells on one cell and made it
  call floodFillCells to all its surrounding cells.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  The classes used in my code were Cell, Minesweeper, RunMinesweeper, and Game.
  Cell is the class that stores information for a singular cell on the
  minesweeper board, and it makes up the 2D array board. Minesweeper is my main
  class where the logic of the game lies, like the mouseclick listener, or the
  functions to uncover or flag a cell. RunMinesweeper sets up the game using
  Swing methods, and puts the game board and reset, save, load, and help buttons
  on the screen. Game is where I run the game.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  The biggest stumbling block I faced was getting started. When I was faced with
  empty file, there were too many things that I had to implement, and I wasn't
  sure which features I should implement first. Once I got started, it went much
  smoother.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  My 2D array and variables in the Minesweeper class are encapsulated, so that
  you can't change the board except through methods in the class. Additionally,
  all the variables in my Cell class are encapsulated, and can only be changed
  or received through get methods or methods like uncover or placeMine. I
  separated the functionality pretty well, so that you can test the functions
  pretty well using the unit tests. If given the chance, I would refactor my
  paint method into more methods, because my paint method is currently handling
  a large amount of responsibilities.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  For the tiles: https://itch.io/e/11353410/uchimama-published-minesweeper-tileset

