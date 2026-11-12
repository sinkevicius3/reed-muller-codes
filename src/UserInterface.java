import java.util.Scanner;

public class UserInterface {
  
  public static UserInterface instance = null;

  private static final Scanner scanner = new Scanner(System.in);

  /** Prevents direct construction of the singleton. */
  private UserInterface() {
    // Private constructor to prevent instantiation
  }

  /**
   * Returns UserInterface instance, creating it if necessary.
   *
   * @return the singleton instance
   */
  public static UserInterface getInstance() {
    if (instance == null) {
      instance = new UserInterface();
    }
    return instance;
  }

  /**
   * Returns the scanner used to read console input.
   *
   * @return the shared console input scanner
   */
  private Scanner getScanner() {
    return scanner;
  }

  /**
   * Runs the main loop (program) for generating and decoding Reed-Muller codes.
   */
  public void loop(){
    printSeparator();
    System.out.println("Welcome to the Reed-Muller Code Generator!");

    while (true) {
      printSeparator();
      System.out.println("Please select an option:");
      System.out.println("1. Generate Reed-Muller Code");
      System.out.println("2. Exit");
      printSeparator();
      
      int choice = getUserInputInteger("Enter your choice: ", 1, 2);

      switch (choice) {
        case 1:
          generateReedMullerCode();
          break;
        case 2:
          System.out.println("Exiting...");
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  /**
   * Gets necessary code parameters from user, encodes a message, transmits it through a
   * simulated channel, lets user manually edit transmitted data and then decodes.
   */
  private void generateReedMullerCode() {
    int m = getUserInputInteger("Enter the value of m for RM(1, m): ", 1, Integer.MAX_VALUE);

    ReedMuller reedMuller = new ReedMuller(m);

    Matrix vector = getUserInputVector("Enter a binary vector of length " + (reedMuller.getM() + 1) + ": ", reedMuller.getM() + 1);

    double errorProbability = getUserInputDouble("Enter the error probability in the range [0.0, 1.0]: ", 0.0, 1.0);
    
    Channel channel = new Channel(errorProbability);

    Matrix encoded = reedMuller.encode(vector);
    Matrix transmitted = channel.transmit(encoded);

    printSeparator();
    System.out.println("Input Vector:");
    Printer.printMatrix(vector);
    System.out.println("Encoded Vector:");
    Printer.printMatrix(encoded);
    System.out.println("Transmitted Vector:");
    Printer.printMatrix(transmitted);
    System.out.println("Number of Errors: " + channel.getErrorCount());
    System.out.println("Positions of Errors: " + channel.getErrorPositions());
    printSeparator();

    while (true) {
      printSeparator();
      System.out.println("Please select an option:");
      System.out.println("1. Change the transmitted vector");
      System.out.println("2. Decode the transmitted vector");
      printSeparator();

      int choice = getUserInputInteger("Enter your choice: ", 1, 2);
      switch (choice) {
        case 1:
          transmitted = updateTransmittedVector(transmitted);
          break;
        case 2:
          decodeTransmittedVector(reedMuller, vector, encoded, transmitted);
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  /**
   * Reads an integer from the console until the integer is in the given range.
   *
   * @param prompt message displayed before each input attempt
   * @param min minimum accepted integer, inclusive
   * @param max maximum accepted integer, inclusive
   * @return the validated integer entered by the user
   */
  private int getUserInputInteger(String prompt, int min, int max) {
    while (true) {
      System.out.print(prompt);
      String line = getScanner().nextLine().trim();
      try {
        int input = Integer.parseInt(line);
        if (input >= min && input <= max) {
          return input;
        }
      } catch (NumberFormatException e) {
        // fall through to the error message
      }
      System.out.println("Invalid input. Please enter an integer in the range [" + min + ", " + max + "].");
    }
  }

  /**
   * Reads a real number from the console until it the number is in the given range.
   *
   * @param prompt message displayed before each input attempt
   * @param min minimum accepted number, inclusive
   * @param max maximum accepted number, inclusive
   * @return the validated real number entered by the user
   */
  private double getUserInputDouble(String prompt, double min, double max) {
    while (true) {
      System.out.print(prompt);
      String line = getScanner().nextLine().trim();
      if (line.isEmpty()) {
        System.out.println("Invalid input. Please enter a real number in the range [" + min + ", " + max + "].");
        continue;
      }
      try {
        double input = Double.parseDouble(line);
        if (input >= min && input <= max) {
          return input;
        }
      } catch (NumberFormatException e) {
        // fall through to the error message
      }
      System.out.println("Invalid input. Please enter a real number in the range [" + min + ", " + max + "].");
    }
  }

  /**
   * Reads a binary string of the required length from the console and converts it to a matrix (vector).
   *
   * @param prompt message displayed before each input attempt
   * @param length required number of binary digits
   * @return a one-row matrix (vector) containing the entered binary vector
   */
  private Matrix getUserInputVector(String prompt, int length) {
    String input;
    while (true) {
      System.out.print(prompt);
      input = getScanner().nextLine().trim();
      if (input.matches("[01]+") && input.length() == length) {
        break;
      }
      System.out.println("Invalid input. Please enter a binary string of length " + length + ".");
    }

    // Convert the binary string to a [1 x (m + 1)] matrix
    Matrix vector = new Matrix(new int[1][input.length()]);
    for (int i = 0; i < input.length(); ++i) {
      vector.setDataCell(0, i, Character.getNumericValue(input.charAt(i)));
    }

    return vector;
  }

  /** Prints a visual separator for the console interface. */
  private void printSeparator() {
    System.out.println("------------------------------------------");
  }

  /**
   * Changes one bit (selected by the user) in the transmitted vector.
   *
   * @param transmitted vector whose selected bit is changed
   * @return the updated transmitted vector
   */
  private Matrix updateTransmittedVector(Matrix transmitted) {
    printSeparator();
    System.out.println("Current Transmitted Vector:");
    Printer.printMatrix(transmitted);
    int col = getUserInputInteger("Enter the column index to change (0 to " + (transmitted.getCols() - 1) + "): ", 0, transmitted.getCols() - 1);
    transmitted.setDataCell(0, col, transmitted.getDataCell(0, col) ^ 1);
    System.out.println("Updated Transmitted Vector:");
    Printer.printMatrix(transmitted);
    printSeparator();
    return transmitted;
  }

  /**
   * Decode and display the input, encoded, transmitted, and decoded vectors.
   *
   * @param reedMuller code used to decode the transmitted vector
   * @param input original message vector
   * @param encoded encoded input vector
   * @param transmitted vector received after transmission and (or) manual edits
   */
  private void decodeTransmittedVector(ReedMuller reedMuller, Matrix input, Matrix encoded, Matrix transmitted) {
    printSeparator();
    System.out.println("Input Vector:");
    Printer.printMatrix(input);
    System.out.println("Encoded Vector:");
    Printer.printMatrix(encoded);
    System.out.println("Transmitted Vector:");
    Printer.printMatrix(transmitted);
    System.out.println("Decoded Vector:");
    Printer.printMatrix(reedMuller.decode(transmitted));
    printSeparator();
  }
}
