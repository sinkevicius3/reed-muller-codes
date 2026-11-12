import java.util.Scanner;

public class UserInterface {
  
  public static UserInterface instance = null;

  private static final Scanner scanner = new Scanner(System.in);

  private UserInterface() {
    // Private constructor to prevent instantiation
  }

  public static UserInterface getInstance() {
    if (instance == null) {
      instance = new UserInterface();
    }
    return instance;
  }

  private Scanner getScanner() {
    return scanner;
  }

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

  private void printSeparator() {
    System.out.println("------------------------------------------");
  }

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
