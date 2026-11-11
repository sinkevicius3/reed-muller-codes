public class Test {
  public static void main(String[] args){
    int testingScenario = 5; // Change this value to select different test scenarios

    switch (testingScenario) {
      case 1:
        testMatrixArithmetics();
        break;
      case 2:
        testPow();
        break;
      case 3:
        testGeneratorMatrix();
        break;
      case 4:
        testDecode();
        break;
      case 5:
        testEncodeAndDecode();
        break;
      default:
        System.out.println("No valid test scenario selected.");
    }
  }

  private static void testMatrixArithmetics() { // case 1
    Matrix a = new Matrix(2, 2, true);
    Matrix b = new Matrix(new int[][]{{-1, 0}, {0, 1}});
    Matrix product = Arithmetics.multiply(a, b);
    Matrix kronecker = Arithmetics.kroneckerProduct(a, b);

    System.out.println("Matrix A:");
    Printer.printMatrix(a);
    System.out.println("Matrix B:");
    Printer.printMatrix(b);
    System.out.println("Product of A and B:");
    Printer.printMatrix(product);
    System.out.println("Kronecker Product of A and B:");
    Printer.printMatrix(kronecker);
  }

  private static void testPow() { // case 2
    int base = 2;
    int max_power = 10;
    for (int i = 0; i <= max_power; ++i) {
      System.out.println("pow(" + base + ", " + i + ") = " + Arithmetics.pow(base, i));
    }
  }

  private static void testGeneratorMatrix() { // case 3
    int m = 3; // Change this value to test different RM(1, m) codes
    ReedMuller reedMuller = new ReedMuller(m);
    System.out.println("Generator Matrix for RM(1, " + m + "):");
    Printer.printMatrix(reedMuller.getGenerator());
  }

  private static void testDecode() { // case 4
    // Examples taken from read literature

    int m = 3; // RM(1, m)
    ReedMuller reedMuller = new ReedMuller(m);

    Matrix receivedVector = new Matrix(new int[][]{{1, 0, 1, 0, 1, 0, 1, 1}});
    System.out.println("Received Vector:");
    Printer.printMatrix(receivedVector);
    System.out.println("Decoded Message Vector:");
    Printer.printMatrix(reedMuller.decode(receivedVector));

    Matrix receivedVector2 = new Matrix(new int[][]{{1, 0, 0, 0, 1, 1, 1, 1}});
    System.out.println("Received Vector:");
    Printer.printMatrix(receivedVector2);
    System.out.println("Decoded Message Vector:");
    Printer.printMatrix(reedMuller.decode(receivedVector2));
  }

  private static void testEncodeAndDecode() { // case 5
    int m = 5; // RM(1, m)
    ReedMuller reedMuller = new ReedMuller(m);

    Matrix messageVector = new Matrix(new int[][]{{1, 1, 1, 0, 1, 0}});
    System.out.println("Message Vector:");
    Printer.printMatrix(messageVector);
    Matrix encoded = reedMuller.encode(messageVector);
    System.out.println("Encoded Vector:");
    Printer.printMatrix(encoded);
    System.out.println("Decoded Vector:");
    Printer.printMatrix(reedMuller.decode(encoded));
  }
}
