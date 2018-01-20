import java.util.Arrays;

class MSDRadixSorter {
  final static int RADIX_BITS = 9; // bits in a digit
  final static int CUTOFF = 31; // use insertion sort if array has fewer elements

  double multiLRadix(int[] a) {
    long startTime = System.nanoTime();
    int[] b = new int[a.length];

    // find max element in a[]
    int max = 0;
    for (int item : a)
      if (item > max) max = item;

    // determine number of bits in max element of a[]
    int numBits = Integer.SIZE - Integer.numberOfLeadingZeros(max);

    // calculate shift and begin sort
    int shift = numBits - (numBits % RADIX_BITS);
    leftRadix(a, b, 0, a.length-1, shift, true); // begin sort

    double execTime = (System.nanoTime() - startTime) / 1e6; // calculate time
    testSort(a);
    return execTime; // returns exec time in ms
  }

  // test if an array is properly sorted (no output upon successful sorting)
  void testSort(int[] a) {
    for (int i = 0; i < a.length-1; i++) {
      if (a[i] > a[i+1]) {
        System.out.printf("Sort error at: a[%d]: %d > a[%d]: %d\n", i, a[i], i+1, a[i+1]);
        if (a.length == 100 || a.length == 1000) {
          System.out.println(Arrays.toString(a));
        }
        return;
      }
    }
  }

  // insertion sort for a segment of an array
  void insertionSort(int[] a, int left, int right) {
    for (int i = left; i <= right; i++) {
      for (int j = i; j > left && a[j] < a[j-1]; j--) {
        int temp = a[j];
        a[j] = a[j-1];
        a[j-1] = temp;
      }
    }
  }

  // copy segment of one array into another
  void copySegment(int[] source, int[] destination, int start, int end) {
    for (int i = start; i <= end; i++)
      destination[i] = source[i];
  }

  // Most Significant Digit Radix Sort
  void leftRadix(int[] a, int[] b, int left, int right, int shift, boolean isArrayA) {

    // use insertion sort if sorting a small segment
    if (right - left < CUTOFF) {
      insertionSort(a, left, right);
      if (!isArrayA) // copy back to the original array if sorting the buffer
        copySegment(a, b, left, right);
      return;
    }

    // frequencies of radix values in a
    int mask = (1 << RADIX_BITS) - 1;
    int[] count = new int[mask+1];
    for (int i = left; i <= right; i++) {
      int digit = (a[i] >> shift) & mask;
      count[digit]++;
    }

    // change frequencies to indices
    int freq = 0, acumVal = 0;
    for (int i = 0; i <= mask; i++) {
      freq = count[i];
      count[i] = acumVal;
      acumVal+= freq;
    }

    // move elements in a sorted manner
    for (int i = left; i <= right; i++) {
      int digit = (a[i] >> shift) & mask;
      b[left + count[digit]++] = a[i];
    }

    // copy back
    if (isArrayA)
      copySegment(b, a, left, right);

    // recursively sort for every radix value
    int nextShift = shift - RADIX_BITS;
    if (nextShift >= 0) {
      for (int i = 0; i < count.length; i++) {
        int nextLeft = (i == 0) ? left : left + count[i-1];
        int nextRight = left + count[i] - 1;
        if (nextRight >= nextLeft)
          leftRadix(b, a, nextLeft, nextRight, nextShift, !isArrayA);
      }
    } else {
      return;
    }
  }

}
