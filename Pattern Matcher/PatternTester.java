import java.util.List;

// the program for testing PatternMatcher
public class PatternTester {

  // test PatternMatcher with given text and pattern files
  private static void testPattern(String textFile, String patternFile) {
    PatternMatcher matcher = new PatternMatcher();
    matcher.readFiles(textFile, patternFile);
    matcher.checkConditions(textFile, patternFile);
    List<Integer> matchIndices = matcher.findPattern();
    matcher.printInfo(matchIndices);
  }

  public static void main(String[] args) {
    System.out.println("This program tests the pattern matching algorithm.\n");

    System.out.println("=========== TEST CASE: EMPTY TEXT FILE ============");
    System.out.println("[Expected output: warning about empty file]\n");
    System.out.println("BEGIN TEST.");
    System.out.println("---------------------------------------------------");
    testPattern("empty.txt", "pattern1.txt");
    System.out.println("---------------------------------------------------");


    System.out.println("\n\n========== TEST CASE: EMPTY PATTERN FILE ==========");
    System.out.println("[Expected output: warning about empty file]\n");
    System.out.println("BEGIN TEST.");
    System.out.println("---------------------------------------------------");
    testPattern("text1.txt", "empty.txt");
    System.out.println("---------------------------------------------------");

    System.out.println("\n\n======== TEST CASE: PATTERN LONGER THAN TEXT ========");
    System.out.println("[Expected output: warning about invalid pattern size]\n");
    System.out.println("BEGIN TEST.");
    System.out.println("----------------------------------------------------");
    testPattern("textshort.txt", "patternlong.txt");
    System.out.println("---------------------------------------------------");

    System.out.println("\n\n============ TEST CASE: REGULAR PATTERN ===========");
    System.out.println("[Expected output: matches \"are\" at 42 and 506.]\n");
    System.out.println("BEGIN TEST.");
    System.out.println("---------------------------------------------------");
    testPattern("text1.txt", "pattern1.txt");
    System.out.println("---------------------------------------------------");

    System.out.println("\n\n============= TEST CASE: ONE WILDCARD ==============");
    System.out.println("[Expected output: matches \"th_t\" at 325 and 453.]\n");
    System.out.println("BEGIN TEST.");
    System.out.println("---------------------------------------------------");
    testPattern("text1.txt", "pattern2.txt");
    System.out.println("---------------------------------------------------");

    System.out.println("\n\n============= TEST CASE: TWO WILDCARDS ==============");
    System.out.println("[Expected output: matches \"_thi_s\" at 123 and 187.]\n");
    System.out.println("BEGIN TEST.");
    System.out.println("---------------------------------------------------");
    testPattern("text1.txt", "pattern3.txt");
    System.out.println("---------------------------------------------------");
  }
}
