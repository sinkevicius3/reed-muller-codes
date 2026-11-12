public class Matrix {
  
  private int rows;
  private int cols;
  private int[][] data;

  /**
   * Creates a zero-filled matrix with the specified dimensions.
   *
   * @param rows number of rows
   * @param cols number of columns
   */
  public Matrix(int rows, int cols) {
    this(rows, cols, false);
  }

  /**
   * Creates a matrix, optionally an identity matrix.
   *
   * @param rows number of rows
   * @param cols number of columns
   * @param isIdentity whether it is an identity matrix (only valid for square matrices)
   * @throws IllegalArgumentException if an identity matrix is requested with non-square dimensions
   */
  public Matrix(int rows, int cols, boolean isIdentity) {
    this.rows = rows;
    this.cols = cols;
    this.data = new int[rows][cols]; // this sets all values to 0 by default

    if (isIdentity) {
      if (rows != cols) {
        throw new IllegalArgumentException("Identity matrix must be square.");
      }
      for (int i = 0; i < rows; ++i) {
        data[i][i] = 1;
      }
    }
  }

  /**
   * Creates a matrix by using the provided 2D array of matrix values.
   *
   * @param data source matrix values
   */
  public Matrix(int[][] data) {
    this.rows = data.length;
    this.cols = data[0].length;
    this.data = new int[rows][cols];

    for (int i = 0; i < rows; ++i) {
      System.arraycopy(data[i], 0, this.data[i], 0, cols); // (source, sourcePos, destination, destinationPos, length);
    }
  }

  /**
   * Returns the number of rows in this matrix.
   *
   * @return the row count
   */
  public int getRows() {
    return rows;
  }

  /**
   * Returns the number of columns in this matrix.
   *
   * @return the column count
   */
  public int getCols() {
    return cols;
  }

  /**
   * Replaces one matrix cell with the given value.
   *
   * @param row zero-based row index
   * @param col zero-based column index
   * @param value new cell value
   * @throws IndexOutOfBoundsException if the row or column is outside the matrix dimensions
   */
  public void setDataCell(int row, int col, int value) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IndexOutOfBoundsException("Invalid row or column index.");
    }

    data[row][col] = value;
  }

  /**
   * Returns the value stored in one matrix cell.
   *
   * @param row zero-based row index
   * @param col zero-based column index
   * @return the cell value
   * @throws IndexOutOfBoundsException if the row or column is outside the matrix dimensions
   */
  public int getDataCell(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IndexOutOfBoundsException("Invalid row or column index.");
    }

    return data[row][col];
  }
}
