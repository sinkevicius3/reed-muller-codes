public final class Printer {

  private Printer() {
    // Private constructor to prevent instantiation
  }

  public static void printMatrix(Matrix matrix) {
    for (int i = 0; i < matrix.getRows(); ++i) {
      for (int j = 0; j < matrix.getCols(); ++j) {
        System.out.print(matrix.getDataCell(i, j) + " ");
      }
      System.out.println();
    }
  }
}
