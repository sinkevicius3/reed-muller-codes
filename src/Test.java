public class Test {
  public static void main(String[] args){
    int testingScenario = 1;

    switch (testingScenario) {
      case 1:
        testMatrixArithmetics();
        break;
      default:
        System.out.println("No test scenario selected.");
    }
  }

  private static void testMatrixArithmetics() {
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
}
