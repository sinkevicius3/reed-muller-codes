public final class Arithmetics {

  private static final int MODULO = 2; // we will be working in GF(2)

  /** Prevents instantiation of this utility class. */
  private Arithmetics() {
    // Private constructor to prevent instantiation
  }

  /**
   * Multiplies two matrices using ordinary integer arithmetic.
   *
   * @param a first matrix
   * @param b second matrix
   * @return the matrix product of {@code a} and {@code b}
   * @throws IllegalArgumentException if the matrix dimensions are incompatible
   */
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

    return new Matrix(product);
  }

  /**
   * Computes the Kronecker product of two matrices.
   *
   * @param a first matrix
   * @param b second matrix
   * @return the Kronecker product of {@code a} and {@code b}
   */
  public static Matrix kroneckerProduct(Matrix a, Matrix b) {
    int[][] kroneckerProduct = new int[a.getRows() * b.getRows()][a.getCols() * b.getCols()];

    for (int i = 0; i < a.getRows(); ++i) {
      for (int j = 0; j < a.getCols(); ++j) {
        for (int i2 = 0; i2 < b.getRows(); ++i2) {
          for (int j2 = 0; j2 < b.getCols(); ++j2) {
            kroneckerProduct[i * b.getRows() + i2][j * b.getCols() + j2] = a.getDataCell(i, j) * b.getDataCell(i2, j2);
          }
        }
      }
    }

    return new Matrix(kroneckerProduct);
  }

  /**
   * Applies modulo 2 to every value in a matrix.
   *
   * @param matrix input matrix
   * @return a new matrix containing each value reduced modulo 2
   */
  public static Matrix applyModulo(Matrix matrix) {
    int[][] data = new int[matrix.getRows()][matrix.getCols()];
    for (int i = 0; i < matrix.getRows(); ++i) {
      for (int j = 0; j < matrix.getCols(); ++j) {
        data[i][j] = ((matrix.getDataCell(i, j) % MODULO) + MODULO) % MODULO; // Ensure non-negative result
      }
    }
    return new Matrix(data);
  }

  /**
   * Raises an integer base to a non-negative integer power.
   *
   * @param base integer base
   * @param power non-negative exponent
   * @return {@code base}^{@code power}
   * @throws IllegalArgumentException if {@code power} is negative
   */
  public static int pow(int base, int power) { // Method is for integer exponentiation
    if (power < 0) {
      throw new IllegalArgumentException("Power for integers must be non-negative.");
    }
    int result = 1;
    while (power > 0) {
      if ((power & 1) == 1) { // If power is odd
        result *= base;
      }
      base *= base;
      power >>= 1; // Divide power by 2
    }
    return result;
  }

  /**
   * Returns absolute value of an integer.
   *
   * @param value integer value
   * @return the absolute value of {@code value}
   */
  public static int abs(int value) { // Method is for integer absolute value
    return (value < 0) ? -value : value;
  }
}
