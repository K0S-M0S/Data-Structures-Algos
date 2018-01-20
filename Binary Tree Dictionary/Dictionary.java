import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class Dictionary {
  private BinarySearchTree<String> wordTree;

  public Dictionary() {
    this.wordTree = new BinarySearchTree<>();
  }

  // inserts words into dictionary from file
  public void readFile(String fileName) {
    File dictionary = new File(fileName);
    Scanner in = null;
    try {
      in = new Scanner(dictionary);
    } catch (FileNotFoundException e) {
      System.out.println("FILE NOT FOUND");
      in.close();
      System.exit(1);
    }
    while (in.hasNextLine()) {
      wordTree.insert(in.nextLine());
    }
    in.close();
    // the tree is established, good place to assign node depths
    wordTree.setDepths();
  }

  // looks up words in dictionary
  public void commandLoop() {
    System.out.println("This is a dictionary program for looking up word spelling.");
    System.out.println("If a word is not found, a list of similar words will be offered.");
    Scanner userInput = new Scanner(System.in);

    while(true) {
      System.out.print("Search word ('q' to exit): ");
      String word = userInput.nextLine();
      word = word.toLowerCase();

      if (word.equals("q")) {
        userInput.close();
        exitLoop();
      }

      if (wordTree.search(word)) {
        System.out.println("The word " + word + " is present in the dictionary.\n");
      } else {
        System.out.printf("\nThe word \"%s\" is not present in the dictionary.\n", word);
        Set<String> generatedWords = similarWords(word);
        if (generatedWords.size() > 0) {
          System.out.println("Similar words: ");
          for (String s : generatedWords)
            System.out.println(s);
          System.out.print("\n");
        } else {
          System.out.println("No similar words are present either.\n");
        }
      }
    }
  }

  // called when one attempts to exit the program with 'q'
  public void exitLoop() {
    System.out.println("\nTerminating program with the following results: \n");
    System.out.println("Depth of the tree: " + wordTree.getMaxDepth() + "\n");
    System.out.println("The nodes were allocated as follows: ");
    int[] depths = wordTree.getDepthCounts();
    for (int i = 0; i < depths.length; i++) {
      System.out.println("Depth " + i + ": " + depths[i] + " node(s).");
    }
    System.out.printf("\nThe average depth of all the nodes in the tree: %.2f.\n", wordTree.getAvgDepth());
    System.out.printf("\nThe first and the last words alphabetically were: \"%s\" and \"%s\" respectively.\n",
                       wordTree.findMin(), wordTree.findMax());
    System.exit(0);
  }

  // helper method, generates similar words
  private Set<String> similarWords(String word) {
    Set<String> wordList = new HashSet<>();
    char[] letters = word.toCharArray();
    switchLetter(letters, wordList);
    replaceLetter(letters, wordList);
    addLetter(letters, wordList);
    removeLetter(word, wordList);
    return wordList;
  }

  // helper method, letter switch algorithm for word generation
  private void switchLetter(char[] letters, Set<String> outputList) {
    for (int i = 1; i < letters.length; i++) {
      char[] lettersCopy = Arrays.copyOf(letters, letters.length);
      char temp = lettersCopy[i];
      lettersCopy[i] = lettersCopy[i-1];
      lettersCopy[i-1] = temp;
      String switched = new String(lettersCopy);
      if (wordTree.search(switched))
        outputList.add(switched);
    }
  }

  // helper method, letter replacement algorithm for word generation
  private void replaceLetter(char[] letters, Set<String> outputList) {
    for (int i = 0; i < letters.length; i++) {
      char[] lettersCopy = Arrays.copyOf(letters, letters.length);
      for (char letter = 'a'; letter <= 'z'; letter++) {
        lettersCopy[i] = letter;
        String replaced = new String(lettersCopy);
        if (wordTree.search(replaced))
          outputList.add(replaced);
      }
    }
  }

  // helper method, letter addition algorithm for word generation
  private void addLetter(char[] letters, Set<String> outputList) {
    for (int i = 0; i <= letters.length; i++) {
      char[] lettersCopy = Arrays.copyOf(letters, letters.length+1);
      for (int j = letters.length-1; j >= i; j--) {
        lettersCopy[j+1] = lettersCopy[j];
      }
      for (char letter = 'a'; letter <= 'z'; letter++) {
        lettersCopy[i] = letter;
        String added = new String(lettersCopy);
        if (wordTree.search(added))
          outputList.add(added);
      }
    }
  }

  // helper method, letter removal algorithm for word generation
  private void removeLetter(String word, Set<String> outputList) {
    for (int i = 0; i < word.length(); i++) {
      String removed = word;
      removed = removed.substring(0, i) + removed.substring(i+1);
      if (wordTree.search(removed))
        outputList.add(removed);
    }
  }


}
