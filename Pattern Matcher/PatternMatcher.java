import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class PatternMatcher {
  final static int RADIX = 256; // assume 256 characters in alphabet
  private char[] textArr; // store text
  private char[] patternArr; // store pattern
  private boolean validFiles = true; // stop program execution in case of bad data

  // scan text and pattern files, store the content
  public void readFiles(String textFile, String patternFile) {
    File text = new File(textFile);
    Scanner textScanner = null;
    try {
      textScanner = new Scanner(text);
    } catch (FileNotFoundException e) {
      System.out.println("TEXT FILE NOT FOUND.");
      System.exit(1);
    }
    String textString = "";
    while (textScanner.hasNextLine()) {
      textString += textScanner.nextLine();
    }
    this.textArr = textString.toCharArray();
    textScanner.close();

    File pattern = new File(patternFile);
    Scanner patternScanner = null;
    try {
      patternScanner = new Scanner(pattern);
    } catch (FileNotFoundException e) {
      System.out.println("PATTERN FILE NOT FOUND.");
      System.exit(2);
    }
    String patternString = "";
    while (patternScanner.hasNextLine()) {
      patternString += patternScanner.nextLine();
    }
    this.patternArr = patternString.toCharArray();
    patternScanner.close();
  }

  // check text and pattern lengths and abort execution if necessary
  public void checkConditions(String textFile, String patternFile) {
    // handle empty text
    System.out.printf("Loaded text file \"%s\"", textFile);
    if (textArr.length == 0) {
      System.out.print(". But it is empty.\n");
      System.out.println("Aborting program.");
      this.validFiles = false;
      return;
    } else {
      System.out.printf(" (%d symbols).\n", textArr.length);
    }

    // handle empty pattern
    System.out.printf("Loaded pattern file \"%s\"", patternFile);
    if (patternArr.length == 0) {
      System.out.print(". But it is empty.\n");
      System.out.println("Aborting program.");
      this.validFiles = false;
      return;
    } else {
      int wildcards = 0;
      for (char c : patternArr)
        if (c == '_') wildcards++;
      System.out.printf(" (%d symbols, %d wildcards).\n", patternArr.length, wildcards);
    }

    // handle invalid pattern length
    if (patternArr.length > textArr.length) {
      System.out.println("Pattern cannot be longer than the scanned text.");
      System.out.println("Aborting program.");
      this.validFiles = false;
      return;
    }
  }

  // find pattern in text, return indices of occurences
  public List<Integer> findPattern() {
    if (this.validFiles) {
      List<Integer> matchIndices = new ArrayList<Integer>();
      int[] badShift = new int[RADIX];

      int offset = 0, scan = 0, maxshift = patternArr.length;
      int maxoffset = textArr.length - patternArr.length;
      int last = patternArr.length - 1;

      // compute maximum shift (influenced by wildcard position(s))
      for (int i = 0; i < last; i++)
        if (patternArr[i] == '_') maxshift = last - i;

      // compute bad character shift for all characters
      for (int i = 0; i < RADIX; i++)
        badShift[i] = maxshift; // default shift value
      for (int i = 0; i < last; i++)
        badShift[(int) patternArr[i]] = (last - i < maxshift) ? last - i : maxshift;

      // scan text
      while (offset <= maxoffset) {
        for (scan = last; patternArr[scan] == textArr[scan+offset] || patternArr[scan] == '_'; scan--){
          if(scan == 0){ // found match
            matchIndices.add(offset);
            break;
          }
        }
        offset += badShift[textArr[offset + last]];
      }
      if (matchIndices.isEmpty()) matchIndices.add(-1);
      return matchIndices;
    } else {
      return null;
    }
  }

  // print information about pattern matches
  public void printInfo(List<Integer> matchIndices) {
    if (this.validFiles) {
      if (matchIndices.get(0) == -1) {
        System.out.println("\nNo matches found.");
      } else {
        System.out.printf("\nFound %d matches of the following pattern: ", matchIndices.size());
        System.out.printf("\"%s\".\n\n", new String(patternArr));

        for (int index : matchIndices) {
          System.out.printf("Match at index %d (to %d): \"", index, index + patternArr.length-1);
          for (int i = index; i <= index + patternArr.length-1; i++)
            System.out.print(textArr[i]);
          System.out.print("\"\n");
        }
        System.out.print("\n");
      }
    }
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Invalid parameters.");
      System.out.println("The program should be executed as follows:");
      System.out.println("java PatternMatcher [text file] [pattern file]");
    } else {
      PatternMatcher matcher = new PatternMatcher();
      matcher.readFiles(args[0], args[1]);
      matcher.checkConditions(args[0], args[1]);
      List<Integer> matchIndices = matcher.findPattern();
      matcher.printInfo(matchIndices);
    }
  }
}
