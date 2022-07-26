import java.util.*;
import java.io.File;

//Object which loads the collins dictionary into a set and checks strings against the dictionary.

public class WordCheck {
  Set<String> words;
  public WordCheck() {
    this.words = new HashSet<String>();
    try {
      Scanner s = new Scanner(new File("../Data/Collins Scrabble Words (2019).txt"));
      s.nextLine();
      s.nextLine();
      while (s.hasNextLine()) {
        this.words.add(s.nextLine().toLowerCase());
      }
    } catch (Exception e) {
      System.out.println("Error: Cannot load legal words file.");
      System.out.println(e);
    }
  }

  //Input: String
  //Output: A boolean which is true if thw string is a word found in the loaded dictionary

  public boolean check(String word) {
    return this.words.contains(word);
  }
}
