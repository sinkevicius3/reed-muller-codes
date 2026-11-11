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
    System.out.println("Welcome to the Reed-Muller Code Generator!");

    while (true) {
      System.out.println("Please select an option:");
      System.out.println("1. Generate Reed-Muller Code");
      System.out.println("2. Exit");

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

    String binaryString = getUserInputBinaryString("Enter a binary vector of length " + (reedMuller.getM() + 1) + ": ", reedMuller.getM() + 1);

    // Convert the binary string to a matrix (1 x (m + 1))
    Matrix vector = new Matrix(new int[1][binaryString.length()]);
    for (int i = 0; i < binaryString.length(); ++i) {
      vector.setDataCell(0, i, Character.getNumericValue(binaryString.charAt(i)));
    }

    double errorProbability = getUserInputDouble("Enter the error probability in the range [0.0, 1.0]: ", 0.0, 1.0);
    
    Channel channel = new Channel(errorProbability);

    System.out.println("Original Vector:");
    Printer.printMatrix(vector);
    System.out.println("Encoded Vector:");
    Matrix encoded = reedMuller.encode(vector);
    Printer.printMatrix(encoded);
    System.out.println("Vector after transmission through the channel:");
    Matrix transmitted = channel.transmit(encoded);
    Printer.printMatrix(transmitted);

    while (true) {
      System.out.println("Please select an option:");
      System.out.println("1. Change the transmitted vector");
      System.out.println("2. Decode the transmitted vector");

      int choice = getUserInputInteger("Enter your choice: ", 1, 2);
      switch (choice) {
        case 1:
          System.out.println("Current Transmitted Vector:");
          Printer.printMatrix(transmitted);
          int col = getUserInputInteger("Enter the column index to change (0 to " + (transmitted.getCols() - 1) + "): ", 0, transmitted.getCols() - 1);
          transmitted.setDataCell(0, col, transmitted.getDataCell(0, col) ^ 1);
          System.out.println("Updated Transmitted Vector:");
          Printer.printMatrix(transmitted);
          break;
        case 2:
          Matrix decoded = reedMuller.decode(transmitted);
          System.out.println("Decoded Vector:");
          Printer.printMatrix(decoded);
          return;
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  private int getUserInputInteger(String prompt, int min, int max) {
    System.out.print(prompt);
    int input;
    while (true) {
      try {
        input = getScanner().nextInt();
        if (input < min || input > max) {
          System.out.println("Invalid input. Please enter an integer in the range [" + min + ", " + max + "].");
          getScanner().next(); // clear the invalid input
          continue;
        }
        return input;
      } catch (Exception e) {
        System.out.println("Invalid input. Please enter a valid integer.");
        getScanner().next(); // clear the invalid input
        continue;
      }
    }
  }

  private double getUserInputDouble(String prompt, double min, double max) {
    System.out.print(prompt);
    double input;
    while (true) {
      try {
        input = getScanner().nextDouble();
        if (input < min || input > max) {
          System.out.println("Invalid input. Please enter a number in the range [" + min + ", " + max + "].");
          getScanner().next(); // clear the invalid input
          continue;
        }
        return input;
      } catch (Exception e) {
        System.out.println("Invalid input. Please enter a valid real number.");
        getScanner().next(); // clear the invalid input
        continue;
      }
    }
  }

  private String getUserInputBinaryString(String prompt, int length) {
    System.out.print(prompt);
    String input;
    while (true) {
      try {
        input = getScanner().next();
        if (!input.matches("[01]+") || input.length() != length) {
          System.out.println("Invalid input. Please enter a binary string of length " + length + ".");
          getScanner().next(); // clear the invalid input
          continue;
        }
        return input;
      } catch (Exception e) {
        System.out.println("Invalid input. Please enter a valid string.");
        getScanner().next(); // clear the invalid input
        continue;
      }
    }
  }
}
