import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Experiment {

  private static volatile Matrix decoded;
  public static void main(String[] args) {
    experiment1(10, 100, 500, 0.25);
  }

  public static void experiment1(int maxM, int warmupTrials, int trials, double errorProbability) {
    Random rng = new Random(0);

    try (FileWriter writer = new FileWriter("experiments/experiment1.csv")) {
      writer.write("m,decode_microseconds\n");

      for (int m = 1; m <= maxM; ++m) {
        ReedMuller reedMuller = new ReedMuller(m);
        Channel channel = new Channel(errorProbability);
        long totalTimeUs = 0;

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
            totalTimeUs += (end - start);
          }
        }
        writer.write(m + "," + (int)(totalTimeUs / 1000.0 / trials) + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
