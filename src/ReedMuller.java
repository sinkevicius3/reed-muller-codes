
public class ReedMuller {

  private final int r = 1; // order of the Reed-Muller code, in our case r = 1 is fixed
  private final int m; // 2^m is the codeword length; m is the number of boolean variables

  private final Matrix G11 = new Matrix(new int[][]{{1, 1}, {0, 1}}); // generator matrix for RM(1, 1)

  private final Matrix H = new Matrix(new int[][]{{1 ,1}, {1, -1}});

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

  private Matrix getH() {
    return H;
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

  public Matrix encode(Matrix messageVector) {
    if (messageVector.getRows() != 1 || messageVector.getCols() != (getM() + 1)) {
      throw new IllegalArgumentException("Message vector must be a 1 x " + (getM() + 1) + " dimension matrix.");
    }
    
    return Arithmetics.applyModulo(Arithmetics.multiply(messageVector, getGenerator()));
  }

  public Matrix decode(Matrix receivedVector) {
    if (receivedVector.getRows() != 1 || receivedVector.getCols() != getCodewordLength()) {
      throw new IllegalArgumentException("Received vector must be a 1 x " + getCodewordLength() + " dimension matrix.");
    }

    // copy received vector into a new vector that we will transform and replace all 0 coordinates with -1
    Matrix vector = new Matrix(1, receivedVector.getCols());
    for (int i = 0; i < vector.getCols(); ++i) {
      vector.setDataCell(0, i, receivedVector.getDataCell(0, i) == 0 ? -1 : receivedVector.getDataCell(0, i));
    }

    // for each i from 1 to m, we apply the transformation H_i to the vector
    for (int i = 1; i < getM() + 1; ++i) {
      Matrix I_left = new Matrix(Arithmetics.pow(2, getM() - i), 
                              Arithmetics.pow(2, getM() - i), 
                              true);
      Matrix I_right = new Matrix(Arithmetics.pow(2, i - 1), 
                               Arithmetics.pow(2, i - 1), 
                               true);
      Matrix H_i = Arithmetics.kroneckerProduct(Arithmetics.kroneckerProduct(I_left, getH()), I_right);
      vector = Arithmetics.multiply(vector, H_i);
    }

    // search for the maximum absolute value in the vector and its index
    int max_idx = 0;
    int max_value = Arithmetics.abs(vector.getDataCell(0, 0));
    for (int i = 1; i < vector.getCols(); ++i) {
      if (Arithmetics.abs(vector.getDataCell(0, i)) > max_value) {
        max_value = Arithmetics.abs(vector.getDataCell(0, i));
        max_idx = i;
      }
    }

    Matrix messageVector = new Matrix(1, getM() + 1);

    // set the first coordinate of messageVector to 1 if the maximum value is positive, otherwise leave it as 0 by default
    if (vector.getDataCell(0, max_idx) > 0) {
      messageVector.setDataCell(0, 0, 1);
    }

    // set the remaining coordinates based on the binary representation of max_idx (low-order digits first)
    for (int i = 1; i < messageVector.getCols(); ++i) {
      messageVector.setDataCell(0, i, max_idx & 1);
      max_idx >>= 1; // right shift to check the next bit
    }

    return messageVector;
  }
}
