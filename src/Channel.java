import java.util.ArrayList;

public class Channel {

  private final int q = 2; // size of the alphabet, in our case q = 2 is fixed
  private final double p; // probability of an error
  private int errorCount = 0;
  private ArrayList<Integer> errorPositions = new ArrayList<>();

  public Channel(double p) {
    if (p < 0 || p > 1) {
      throw new IllegalArgumentException("Error probability p must be in the range [0, 1].");
    }
    this.p = p;
  }

  public int getQ() { // q is never used in code, but useful for future extensions and for semantics
    return q;
  }

  public double getP() {
    return p;
  }

  public int getErrorCount() {
    return errorCount;
  }

  public ArrayList<Integer> getErrorPositions() {
    return errorPositions;
  }

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
