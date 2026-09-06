import java.util.InputMismatchException;
import java.util.Scanner;

public class Sort {
  public static void main(String[] args) {
    try (Scanner s = new Scanner(System.in)) {
      int size = getInt(s);
      if (size <= 0) {
        System.out.println("Input error. Size <= 0");
        return;
      }
      double[] arr = new double[size];
      for (int i = 0; i < arr.length; i++) {
        arr[i] = getDouble(s);
      }
      insertSort(arr);
      printArray(arr);
    }
  }

  private static int getInt(Scanner s) {
    int n = 0;
    while (true) {
      try {
        n = s.nextInt();
        break;
      } catch (InputMismatchException e) {
        System.out.println("Could not parse a number. Please, try again");
        s.next();
      }
    }
    return n;
  }

  private static double getDouble(Scanner s) {
    double n = 0;
    while (true) {
      try {
        n = s.nextDouble();
        break;
      } catch (InputMismatchException e) {
        System.out.println("Could not parse a number. Please, try again");
        s.next();
      }
    }
    return n;
  }

  public static void insertSort(double[] arr) {
    int n = arr.length;
    for (int i = 1; i < n; i++) {
      double key = arr[i];
      int j = i - 1;
      while (j >= 0 && arr[j] > key) {
        arr[j + 1] = arr[j];
        j--;
      }
      arr[j + 1] = key;
    }
  }

  private static void printArray(double[] arr) {
    for (int i = 0; i < arr.length - 1; i++) {
      System.out.printf("%.1f ", arr[i]);
    }
    System.out.printf("%.1f\n", arr[arr.length - 1]);
  }
}
