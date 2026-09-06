import java.util.InputMismatchException;
import java.util.Scanner;

public class Match {
  public static void main(String[] args) {
    try (Scanner s = new Scanner(System.in)) {
      int size = getInt(s);
      if (size <= 0) {
        System.out.println("Input error. Size <= 0");
        return;
      }

      int[] arr1 = new int[size];
      size = 0;
      int i = 0;
      while (i < arr1.length) {
        arr1[i] = getInt(s);
        if (firstLastMatch(arr1[i])) {
          size++;
        }
        i++;
      }

      if (size <= 0) {
        System.out.println("There are no such elements");
        return;
      }

      i = 0;
      int j = 0;
      int[] arr2 = new int[size];
      while (i < arr1.length) {
        if (firstLastMatch(arr1[i])) {
          arr2[j] = arr1[i];
          j++;
        }
        i++;
      }

      i = 0;
      while (i < arr2.length) {
        System.out.printf("%d", arr2[i]);
        if (i < arr2.length - 1) {
          System.out.printf(" ");
        }
        i++;
      }
      System.out.println();
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

  private static boolean firstLastMatch(int n) {
    int last = n % 10;
    while (n >= 10) {
      n /= 10;
    }
    return last == n;
  }
}
