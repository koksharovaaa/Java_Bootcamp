import java.util.InputMismatchException;
import java.util.Scanner;

public class NegAvg {
  public static void main(String[] args) {
    try (Scanner s = new Scanner(System.in)) {
      int size = getInt(s);
      if (size <= 0) {
        System.out.println("Input error. Size <= 0");
        return;
      }
      int[] arr = new int[size];
      int sum = 0;
      int count = 0;
      for (int i = 0; i < size; i++) {
        arr[i] = getInt(s);
        if (arr[i] < 0) {
          sum += arr[i];
          count++;
        }
      }
      if (count == 0) {
        System.out.println("There are no negative elements");
      } else {
        System.out.printf("%f\n", (double) sum / (double) count);
      }
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
}
