import java.util.ArrayList;

public class Channel {

  private final int q = 2; // size of the alphabet, in our case q = 2 is fixed
  private final double p; // probability of an error
  private int errorCount = 0;
  private ArrayList<Integer> errorPositions = new ArrayList<>();

  /**
   * Creates a symmetric binary channel with the specified error probability.
   *
   * @param p probability that each transmitted bit is flipped
   * @throws IllegalArgumentException if {@code p} is outside [0, 1]
   */
  public Channel(double p) {
    if (p < 0 || p > 1) {
      throw new IllegalArgumentException("Error probability p must be in the range [0, 1].");
    }
    this.p = p;
  }

  /**
   * Returns the size of the channel alphabet.
   *
   * @return the binary alphabet size, which is fixed at 2
   */
  public int getQ() { // q is never used in code, but useful for future extensions and for semantics
    return q;
  }

  /**
   * Returns the error probability.
   *
   * @return the error probability
   */
  public double getP() {
    return p;
  }

  /**
   * Returns the number of bit errors from the most recent transmission.
   *
   * @return the most recent transmission's error count
   */
  public int getErrorCount() {
    return errorCount;
  }

  /**
   * Returns the indexes where errors occurred in the most recent transmission.
   *
   * @return the list of error positions
   */
  public ArrayList<Integer> getErrorPositions() {
    return errorPositions;
  }

  /**
   * Transmits a matrix, randomly flipping each bit according to the error probability.
   *
   * @param input matrix to transmit
   * @return a matrix that was transmitted through the channel
   */
  public Matrix transmit(Matrix input) {
    // clear error info for each transmission
    errorCount = 0;
    errorPositions.clear();

    Matrix output = new Matrix(input.getRows(), input.getCols());

    for (int i = 0; i < input.getRows(); ++i) {
      for (int j = 0; j < input.getCols(); ++j) {
        int bit = input.getDataCell(i, j);

        double randomValue = Math.random(); // generates a random number between 0.0 and 1.0

        // If the random generated double value in the range [0.0, 1.0) is less than the error probability, simulate an error
        if (randomValue < getP()) {
          output.setDataCell(i, j, bit ^ 1);
          ++errorCount;
          errorPositions.add(j); // Store the position of the error
        } else {
          output.setDataCell(i, j, bit);
        }
      }
    }

    return output;
  }
}
