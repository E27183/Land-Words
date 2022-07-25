import java.util.Scanner;

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
