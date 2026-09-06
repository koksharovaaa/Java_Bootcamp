import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class MaxMin {
  static final double EPS = 1E-6;

  public static void main(String args[]) {
    File file;
    try (Scanner sc1 = new Scanner(System.in)) {
      String f = sc1.nextLine();
      file = new File(f);
    }

    try (Scanner sc2 = new Scanner(file)) {
      int n = 0;
      if (!sc2.hasNextInt() || (n = sc2.nextInt()) <= 0) {
        System.out.println("Input error. Size <= 0");
        return;
      }

      double[] arr = new double[n];

      for (int i = 0; i < n; i++) {
        while (true) {
          try {
            arr[i] = sc2.nextDouble();
            break;
          } catch (InputMismatchException e) {
            sc2.next();
          } catch (NoSuchElementException e) {
            System.out.println("Input error. Insufficient number of elements");
            return;
          }
        }
      }

      System.out.println(n);
      printArray(arr);

      double min = arr[0];
      double max = arr[0];
      for (int i = 0; i < arr.length; i++) {
        if (min - arr[i] > EPS) {
          min = arr[i];
        }
        if (arr[i] - max > EPS) {
          max = arr[i];
        }
      }

      System.out.println("Saving min and max values in file");
      try (PrintWriter p = new PrintWriter("result.txt")) {
        p.printf("%.1f %.1f", min, max);
      }
    } catch (FileNotFoundException e) {
      System.out.println("Input error. File doesn't exist");
    }
  }

  private static void printArray(double[] arr) {
    for (int i = 0; i < arr.length - 1; i++) {
      System.out.printf("%.1f ", arr[i]);
    }
    System.out.printf("%.1f\n", arr[arr.length - 1]);
  }
}
