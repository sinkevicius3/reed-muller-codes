import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Experiment {

  private static volatile Matrix decoded;

  /**
   * Runs the selected experiment.
   *
   * @param args command-line arguments, not used
   * @throws Exception if an error occurs during the experiment
   */
  public static void main(String[] args) {
    experiment1(10, 100, 1000, 0.10, "experiments/experiment1.csv");
    experiment1(10, 100, 1000, 0.25, "experiments/experiment2.csv");
  }

  /**
   * Implements experiment1, which measures the average decoding time for Reed-Muller codes RM(1, m) 
   * and saves the results to a CSV file.
   *
   * @param maxM the maximum m value for RM(1, m) to experiment with
   * @param warmupTrials the number of warm-up trials before timing to ensure accurate measurements
   * @param trials the number of trials to run for each m
   * @param errorProbability the probability of error in the channel
   * @param path the path to the CSV file to save the results to
   */
  public static void experiment1(int maxM, int warmupTrials, int trials, double errorProbability, String path) {
    Random rng = new Random(0);

    try (FileWriter writer = new FileWriter(path)) {
      writer.write("m,decode_microseconds,uncorrected_errors\n");

      Channel channel = new Channel(errorProbability);

      for (int m = 1; m <= maxM; ++m) {
        ReedMuller reedMuller = new ReedMuller(m);
        long totalTimeNs = 0;
        long uncorrectedCount = 0;

        for (int j = -warmupTrials; j < trials; ++j) {
          Matrix vector = new Matrix(1, m + 1);
          for (int k = 0; k < m + 1; ++k) {
            vector.setDataCell(0, k, rng.nextInt(channel.getQ()));
          }

          Matrix encoded = reedMuller.encode(vector);
          Matrix transmitted = channel.transmit(encoded);

          long start = System.nanoTime();
          decoded = reedMuller.decode(transmitted);
          long end = System.nanoTime();

          if (j >= 0) {
            totalTimeNs += (end - start);

            Matrix encodedDecoded = reedMuller.encode(decoded);
            for (int i = 0; i < encoded.getCols(); ++i) {
              if (encoded.getDataCell(0, i) != encodedDecoded.getDataCell(0, i)) {
                ++uncorrectedCount;
              }
            }
          }
        }
        writer.write(String.format("%d,%d,%.2f%n", m, (int)(totalTimeNs / 1000.0 / trials), (uncorrectedCount / (double) trials)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
