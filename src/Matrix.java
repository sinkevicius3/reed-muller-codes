public class Matrix {
  
  private int rows;
  private int cols;
  private int[][] data;

  public Matrix(int rows, int cols) {
    this(rows, cols, false);
  }

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

  public Matrix(int[][] data) {
    this.rows = data.length;
    this.cols = data[0].length;
    this.data = new int[rows][cols];

    for (int i = 0; i < rows; ++i) {
      System.arraycopy(data[i], 0, this.data[i], 0, cols); // (source, sourcePos, destination, destinationPos, length);
    }
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  public void setDataCell(int row, int col, int value) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IndexOutOfBoundsException("Invalid row or column index.");
    }

    data[row][col] = value;
  }

  public int getDataCell(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IndexOutOfBoundsException("Invalid row or column index.");
    }

    return data[row][col];
  }
}
