
public class ReedMuller {
  private final int r = 1; // order of the Reed-Muller code, in our case r = 1 is fixed
  private final int m; // 2^m is the codeword length; m is the number of Boolean variables

  private final Matrix G11 = new Matrix(new int[][]{{1, 1}, {0, 1}}); // generator matrix for RM(1, 1)

  private final Matrix generator;

  public ReedMuller(int m) {
    if (m < 1) {
      throw new IllegalArgumentException("Parameter m must be at least 1.");
    }
    this.m = m;
    this.generator = generateGeneratorMatrix();
  }

  public int getOrder() {
    return r;
  }

  public int getM() {
    return m;
  }

  public int getCodewordLength() {
    return Arithmetics.pow(2, m);
  }

  private Matrix getG11() {
    return G11;
  }

  public Matrix getGenerator() {
    return generator;
  }

  private Matrix generateGeneratorMatrix() {
    Matrix current = getG11();

    for (int i = 2; i <= getM(); ++i) {
      int currentRows = current.getRows();
      int currentCols = current.getCols();

      Matrix next = new Matrix(currentRows + 1, 2 * currentCols); // new matrix will have one more row and double the amount of columns

      // this section builds the top part of the new generator matrix, [G(r, i-1) | G(r, i-1)]
      for (int row = 0; row < currentRows; ++row) {
        for (int col = 0; col < currentCols; ++col) {
          int value = current.getDataCell(row, col);
          next.setDataCell(row, col, value); // we set the top part's left side
          next.setDataCell(row, col + currentCols, value); // we set the top part's right side
        }
      }

      // this section builds the bottom part of the new generator matrix, [0 ... 0 | 1 ... 1]
      // the left side is filled with 0 by default, so we only need to fill the right side with 1
      for (int col = 0; col < currentCols; ++col) {
        next.setDataCell(currentRows, currentCols + col, 1);
      }

      current = next; // update current to newly created matrix
    }

    return current; // if m = 1, this will return G11; if m > 1, this will return G(1,m)
  }
}
