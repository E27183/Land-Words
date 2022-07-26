# Land-Words

Basic 2 player word game played in the terminal.

To play, cd into the Code folder from the terminal and run '''java IO'''

Moves are formatted as [c][r][d] [word]. c and r are single characters corresponding to the co-ordinates on the board of the first character of the word intending to be placed. d is a single character corresponding to the direction of the intended word, and is 'd' to indicate the word should be placed in one column or 'a' to indicate it should fit in one row. 'skip' is also a valid move and skips the players turn. Moves are inputted to the terminal while the game is running. All characters in the move should be lower case.

Example move: gha hello

For a (non skip) move to be legal, the following conditions must be true:
* The word is a valid word according to the collins scrabble dictionary
* The word places at least one new tile
* The word uses at least one preexisting tile owned by the current player
* The word doesn't touch any tiles owned by the opposing player
* Any additional words formed by adjacency are also valid words

At the start of game, each player owns one letter 't' on their side of the board to begin producing new words. The game continues until the bag of tiles is empty or 2 players skip consecutively. At this time, the winner is the player with the highest score. Each tile owned by a player earns points based on the distance from their side; a tile on their edge will be worth 1 point while a tile 4 spaces away from their edge will be worth 5 points.
