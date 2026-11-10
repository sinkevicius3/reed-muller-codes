public final class Arithmetics {

  private static final int MODULO = 2; // we will be working in GF(2)

  private Arithmetics() {
    // Private constructor to prevent instantiation
  }

  public static Matrix multiply(Matrix a, Matrix b) {
    if (a.getCols() != b.getRows()) {
      throw new IllegalArgumentException("Matrix dimensions do not match for multiplication.");
    }

    int[][] product = new int[a.getRows()][b.getCols()];

    for (int i = 0; i < a.getRows(); ++i) {
      for (int j = 0; j < b.getCols(); ++j) {
        for (int k = 0; k < a.getCols(); ++k) {
          product[i][j] += a.getDataCell(i, k) * b.getDataCell(k, j);
        }
      }
    }

    return applyModulo(new Matrix(product));
  }

  public static Matrix kroneckerProduct(Matrix a, Matrix b) {
    int[][] kroneckerProduct = new int[a.getRows() * b.getRows()][a.getCols() * b.getCols()];

    for (int i = 0; i < a.getRows(); ++i) {
      for (int j = 0; j < a.getCols(); ++j) {
        for (int k = 0; k < b.getRows(); ++k) {
          for (int l = 0; l < b.getCols(); ++l) {
            kroneckerProduct[i * b.getRows() + k][j * b.getCols() + l] = a.getDataCell(i, j) * b.getDataCell(k, l);
          }
        }
      }
    }

    return applyModulo(new Matrix(kroneckerProduct));
  }

  public static Matrix applyModulo(Matrix matrix) {
    int[][] data = new int[matrix.getRows()][matrix.getCols()];
    for (int i = 0; i < matrix.getRows(); ++i) {
      for (int j = 0; j < matrix.getCols(); ++j) {
        data[i][j] = ((matrix.getDataCell(i, j) % MODULO) + MODULO) % MODULO; // Ensure non-negative result
      }
    }
    return new Matrix(data);
  }
}
