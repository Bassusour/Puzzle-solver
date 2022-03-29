# Puzzle-solver

In this project, my team and I created an graphical user interface to load and display a puzzle using javaFX. The project follows the Model-View-Controller design pattern. The puzzle files have to be in `.json` format, which can be found in the `Puzzles` folder, and the puzzles consists of different difficulties.  
  
We also created an automatic puzzle solver, which solves any puzzle automatically in `O(n^2m^2)`, where `n` is the number of pieces, and `m` the number of points of every piece. Keep in mind that not all of the puzzles are actually solvable!  
The program supports features such as hint, which highlights two pieces, which have not been put together yet, and if some pieces are identical, a feature will display identical pieces with the same color. 
