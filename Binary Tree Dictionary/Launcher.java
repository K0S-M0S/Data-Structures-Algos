public class Launcher {

  public static void main(String[] args) {
     Dictionary dict = new Dictionary();
      dict.readFile("dictionary.txt");
      dict.commandLoop();
  }
}
