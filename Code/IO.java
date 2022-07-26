import java.util.Scanner;

//Main runnable class. Run java IO to play game.
//Initialises the board to start the game, then reads the terminal for incoming moves as terminal inputs. Prints the board after each valid move.

public class IO {
  public static void main(String[] args) {
    Scanner io = new Scanner(System.in);
    Board b = new Board();
    System.out.println(b);
    while (!b.over()) {
      String read = io.nextLine();
      if (b.makeMove(read)) {
        System.out.println(b);
      } else {
        System.out.println("Badly formatted or illegal move.");
      }
    }

  }
}
