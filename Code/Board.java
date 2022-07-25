import java.util.Random;
import java.lang.StringBuilder;

public class Board {
  char[][] board = new char[13][20];
  boolean[][] left_player_owns = new boolean[13][20];
  boolean[][] right_player_owns = new boolean[13][20];
  char[] pieces = new char[7];
  char[] bag = new char[98];
  int position_in_bag;
  boolean left_player_turn;
  boolean last_turn_skipped;
  boolean end_of_game;
  int[] character_counts = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
  WordCheck checker;

  public Board() {
    this.checker = new WordCheck();
    this.end_of_game = false;
    this.last_turn_skipped = false;
    Random r = new Random();
    this.left_player_turn = r.nextBoolean();
    for (int i = 0; i < 13; i++) {
      for (int j = 0; j < 20; j++) {
        this.board[i][j] = ' ';
      }
    }
    this.board[6][0] = 't';
    this.left_player_owns[6][0] = true;
    this.board[6][19] = 't';
    this.right_player_owns[6][19] = true;
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

  public boolean makeMove(String move) {
    if (this.end_of_game) {
      return false;
    }
    if (move.trim().equals("skip")) {
      if (this.last_turn_skipped) {
        this.end_of_game = true;
      } else {
        this.last_turn_skipped = true;
        this.left_player_turn = !this.left_player_turn;
      }
      return true;
    }
    if (move.length() < 4) {
      System.out.println("Fail 1");
      return false;
    }
    char column = move.charAt(0);
    char row = move.charAt(1);
    char direction = move.charAt(2);
    if (!this.tileInBounds(column, row) || (direction != 'a' && direction != 'd')) {
      System.out.println("Fail 2");
      return false;
    }
    String word = move.substring(3).trim();
    if (!this.checker.check(word)) {
      System.out.println("Fail 3");
      return false;
    }
    if (!this.inBounds(word.length(), column, row, direction)) {
      System.out.println("Fail 4");
      return false;
    }
    boolean[] replacements = this.buildable(word, column, row, direction);
    boolean flag = false;
    for (int i = 0; i < 7; i++) {
      if (replacements[i]) {
        flag = true;
        break;
      }
    }
    if (!flag || !replacements[7]) {
      for (int i = 0; i < 8; i++) {
        System.out.println(replacements[i]);
      }
      System.out.println("Fail 5");
      return false;
    }
    if (!this.perimeterSafe(word, column, row, direction)) {
      System.out.println("Fail 6");
      return false;
    }
    if (direction == 'd') {
      for (int i = 0; i < word.length(); i++) {
        this.board[row + i - 'a'][column - 'a'] = word.charAt(i);
        this.left_player_owns[row + i - 'a'][column - 'a'] = this.left_player_turn;
        this.right_player_owns[row + i - 'a'][column - 'a'] = !this.left_player_turn;
      }
    } else {
      for (int i = 0; i < word.length(); i++) {
        this.board[row - 'a'][column + i - 'a'] = word.charAt(i);
        this.left_player_owns[row - 'a'][column + i - 'a'] = this.left_player_turn;
        this.right_player_owns[row - 'a'][column + i - 'a'] = !this.left_player_turn;
      }
    }
    this.last_turn_skipped = false;
    for (int i = 0; i < 7; i++) {
      if (replacements[i]) {
        this.pieces[i] = this.bag[this.position_in_bag];
        this.position_in_bag++;
        if (this.position_in_bag == 98) {
          this.end_of_game = true;
          return true;
        }
      }
    }
    this.left_player_turn = !this.left_player_turn;
    return true;
  }

  public boolean over() {
    return this.end_of_game;
  }

  public boolean perimeterSafe(String word, char column, char row, char direction) {
    int length = word.length();
    if (direction == 'd') {
      if (this.tileInBounds(column, (char) (row - 1))) {
        if (this.board[row - 1 - 'a'][column - 'a'] != ' ') {
          System.out.print("perim 7");
          return false;
        }
      }
      if (this.tileInBounds(column, (char) (row + length))) {
        if (this.board[row + length - 'a'][column - 'a'] != ' ') {
          System.out.print("perim 8");
          return false;
        }
      }
      for (int i = 0; i < length; i++) {
        boolean flag = false;
        for (int j = -1; j < 2; j += 2) {
          if (this.tileInBounds((char) (column + j), (char) (row + i))) {
            if (this.board[row + i - 'a'][column + j - 'a'] != ' ') {
              if ((this.left_player_owns[row + i - 'a'][column + j - 'a'] && !this.left_player_turn) || (this.right_player_owns[row + i - 'a'][column + j - 'a'] && this.left_player_turn)) {
                System.out.print("perim 1");
                return false;
              }
              flag = true;
            }
          }
        }
        if (flag) {
          StringBuilder b = new StringBuilder();
          char c = column;
          while (c > 'a') {
            if ((this.board[row + i - 'a'][c - 'a'] == ' ') && c != column) {
              c++;
              break;
            }
            c--;
          }
          while (c <= 't') {
            if (c != column) {
              b.append(this.board[row + i - 'a'][c - 'a']);
              if (this.board[row + i - 'a'][c + 1 - 'a'] == ' ' && c + 1 != column) {
                break;
              }
            } else {
              b.append(word.charAt(i));
            }
            if (c == 't') {
              break;
            }
            c++;
          }
          if (!this.checker.check(b.toString().trim())) {
            System.out.print("perim 2");
            System.out.print(b);
            return false;
          }
        }
      }
    } else {
      if (this.tileInBounds((char) (column - 1), row)) {
        if (this.board[row - 'a'][column - 1 - 'a'] != ' ') {
          System.out.print("perim 3");
          return false;
        }
      }
      if (this.tileInBounds((char) (column + length), row)) {
        if (this.board[row - 'a'][column + length - 'a'] != ' ') {
          System.out.print("perim 4");
          return false;
        }
      }
      for (int i = 0; i < length; i++) {
        boolean flag = false;
        for (int j = -1; j < 2; j += 2) {
          if (this.tileInBounds((char) (column + i), (char) (row + j))) {
            if (this.board[row + j - 'a'][column + i - 'a'] != ' ') {
              if ((this.left_player_owns[row + j - 'a'][column + i - 'a'] && !this.left_player_turn) || (this.right_player_owns[row + j - 'a'][column + i - 'a'] && this.left_player_turn)) {
                System.out.print("perim 5");
                return false;
              }
              flag = true;
            }
          }
        }
        if (flag) {
          StringBuilder b = new StringBuilder();
          char r = row;
          while (r > 'a') {
            if ((this.board[r - 'a'][column + i - 'a'] == ' ') && r != row) {
              r++;
              break;
            }
            r--;
          }
          while (r <= 'm') {
            if (r != row) {
              b.append(this.board[r - 'a'][column + i - 'a']);
              if (this.board[r + 1 - 'a'][column + i - 'a'] == ' ' && r + 1 != row) {
                break;
              }
            } else {
              b.append(word.charAt(i));
            }
            if (r == 'm') {
              break;
            }
            r++;
          }
          if (!this.checker.check(b.toString().trim())) {
            System.out.print("perim 6");
            System.out.print(b);
            return false;
          }
        }
      }
    }
    return true;
  }

  public boolean tileInBounds(char column, char row) {
    return 'a' <= column && column <= 't' && 'a' <= row && row <= 'm';
  }

  public boolean[] buildable(String word, char column, char row, char direction) {
    boolean[] used = new boolean[8]; //8th bool indicates if any old tiles are used
    for (int i = 0; i < word.length(); i++) {
      char target;
      char letter = word.charAt(i);
      if (direction == 'd') {
        target = this.board[row + i - 'a'][column - 'a'];
        if ((this.left_player_turn && this.right_player_owns[row + i - 'a'][column - 'a']) || (!this.left_player_turn && this.left_player_owns[row + i - 'a'][column - 'a'])) {
          return new boolean[8];
        }
      } else {
        target = this.board[row - 'a'][column + i - 'a'];
        if ((this.left_player_turn && this.right_player_owns[row - 'a'][column + i - 'a']) || (!this.left_player_turn && this.left_player_owns[row - 'a'][column + i - 'a'])) {
          return new boolean[8];
        }
      }
      if (target == ' ') {
        boolean flag = false;
        for (int j = 0; j < 7; j++) {
          if (!used[j] && this.pieces[j] == letter) {
            used[j] = true;
            flag = true;
            break;
          }
        }
        if (!flag) {
          return new boolean[8];
        }
      } else if (target != letter) {
        return new boolean[8];
      } else {
        used[7] = true;
      }
    }
    return used;
  }

  public boolean inBounds(int length, char column, char row, char direction) {
    if (row < 'a' || column < 'a') {
      return false;
    }
    if (direction == 'd') {
      return row + length - 1 <= 'm';
    } else {
      return column + length - 1 <= 't';
    }
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
