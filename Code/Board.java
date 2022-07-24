import java.util.Random;
import java.lang.StringBuilder;

public class Board {
  char[][] board = new char[13][20];
  char[][] ownership = new char[13][20];
  char[] pieces = new char[7];
  char[] bag = new char[98];
  int position_in_bag;
  boolean left_player_turn;
  boolean start_of_game;
  boolean last_turn_skipped;
  boolean end_of_game;
  int[] character_counts = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};

  public Board() {
    this.start_of_game = true;
    this.end_of_game = false;
    this.last_turn_skipped = false;
    Random r = new Random();
    this.left_player_turn = r.nextBoolean();
    for (int i = 0; i < 13; i++) {
      for (int j = 0; j < 20; j++) {
        this.board[i][j] = ' ';
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

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (!end_of_game) {
      if (left_player_turn) {
        builder.append("Left");
      } else {
        builder.append("Right");
      }
      builder.append(" player to play.\nAvailable letters: ");
      for (int i = 0; i < 7; i++) {
        builder.append(this.pieces[i]);
        builder.append("  ");
      }
      builder.append("\nLetters remaining: ");
      builder.append(98 - this.position_in_bag);
      builder.append("\n");
    } else {
      builder.append("Game is over.\n");
    }
    builder.append("Score: ");
    //TODO
    builder.append("\n\n   ");
    for (int i = 0; i < 20; i++) {
      builder.append(" ");
      builder.append((char) (97 + i));
    }
    for (int i = 0; i < 13; i++) {
      builder.append("\n   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
      builder.append((char) (97 + i));
      builder.append("  |");
      for (int j = 0; j < 20; j++) {
        builder.append(this.board[i][j]);
        builder.append("|");
      }
    }
    builder.append("\n   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
    return builder.toString();
  }

}
