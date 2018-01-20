import java.util.Random;
import java.util.Arrays;

public class SortingTester {

  // custom msd radix sorting algorithm on a random int array of given size
  public static double runLRadixSort(int size) {
    MSDRadixSorter radixSorter = new MSDRadixSorter();
    Random r = new Random();
    int[] arr = new int[size];

    for (int i = 0; i < size; i++)
      arr[i] = r.nextInt(size);

    return radixSorter.multiLRadix(arr);
  }

  // quicksort a random int array of given size
  public static double runQuickSort(int size) {
    Random r = new Random();
    int[] arr = new int[size];

    for (int i = 0; i < size; i++)
      arr[i] = r.nextInt(size);

    double quickStart = System.nanoTime();
    Arrays.sort(arr);
    double quickTime = (System.nanoTime() - quickStart) / 1e6;
    return quickTime;
  }

  // gather data on execution times of the sorting algorithms
  public void gatherData() {
    double[] radixTimes = new double[5];
    double[] quickTimes = new double[5];
    int count = 0;

    // tests for array sizes 1,000,000 to 100, decreasing 10 times each iteration
    for (int size = 1000000; size >= 100; size /= 10) {
      // do five runs of each sorting algorithm, store the median
      double[] execTimes = new double[5];
      for (int i = 0; i < 5; i++)
        execTimes[i] = runLRadixSort(size);
      Arrays.sort(execTimes);
      radixTimes[count] = execTimes[2];

      for (int i = 0; i < 5; i++)
        execTimes[i] = runQuickSort(size);
      Arrays.sort(execTimes);
      quickTimes[count] = execTimes[2];
      count++;
    }

    // compute speed-up
    double[] speedup = new double[5];
    for (int i = 0; i < 5; i++)
      speedup[i] = quickTimes[i]/radixTimes[i];

    // produce comparison table
    System.out.println("COMPUTED COMPARISON TABLE:");
    System.out.printf("Array size:     \t%7d\t%7d\t%7d\t%7d\t%8d\n", 100, 1000, 10000, 100000, 1000000);
    System.out.printf("Radix Sort (ms):\t%7.3f\t%7.3f\t%7.3f\t%7.3f\t%8.3f\n",
      radixTimes[4], radixTimes[3], radixTimes[2], radixTimes[1], radixTimes[0]);
    System.out.printf("Qucksort (ms):  \t%7.3f\t%7.3f\t%7.3f\t%7.3f\t%8.3f\n",
      quickTimes[4], quickTimes[3], quickTimes[2], quickTimes[1], quickTimes[0]);
    System.out.printf("Speed-up:       \t%7.3f\t%7.3f\t%7.3f\t%7.3f\t%8.3f\n",
      speedup[4], speedup[3], speedup[2], speedup[1], speedup[0]);
  }

  public static void main(String[] args) {
    SortingTester radixVSquick = new SortingTester();

    // introduction text
    System.out.println("This program compares execution times of MSD radix sort and quicksort.");
    System.out.println("The tests are performed on arrays of random integers from 0 to size-1.");
    System.out.println("Each sorting algorithm is run five times, and the median is displayed.\n");
    System.out.println("Chosen hyperparameters for MSD radix sort: ");
    System.out.printf("Number of bits per digit: %d bits.\n", MSDRadixSorter.RADIX_BITS);
    System.out.printf("Threshold segment size: %d items.\n\n", MSDRadixSorter.CUTOFF);

    // test sorting algorithms
    radixVSquick.gatherData();
  }
}
