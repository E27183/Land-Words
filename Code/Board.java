import java.util.Random;

public class Board {
  char[][] board = new char[13][20];
  char[][] ownership = new char[13][20];
  char[] pieces = new char[7];
  char[] bag = new char[98];
  int position_in_bag;
  boolean left_player_turn;
  boolean start_of_game;
  boolean last_turn_skipped;
  int[] character_counts = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};

  public Board() {
    this.start_of_game = true;
    this.last_turn_skipped = false;
    Random r = new Random();
    this.left_player_turn = r.nextBoolean();
    for (int i = 0; i < 13; i++) {
      for (int j = 0; j < 20; j++) {
        this.board[i][j] = '0';
        this.ownership[i][j] = '0';
      }
    }
    int pointer = 0;
    for (int i = 0; i < 26; i++) {
      for (int j = 0; j < this.character_counts[i]; j++) {
        this.bag[pointer] = (char) (97 + i);
        pointer++;
      }
    }
    for (int i = 0; i < 97; i++) {
      int position = r.nextInt(98 - i);
      char save = this.bag[i];
      this.bag[i] = this.bag[i + position];
      this.bag[i + position] = save;
    }
    for (int i = 0; i < 7; i++) {
      this.pieces[i] = this.bag[i];
    }
    this.position_in_bag = 7;
  }
}
