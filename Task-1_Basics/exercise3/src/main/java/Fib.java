import java.util.InputMismatchException;
import java.util.Scanner;

public class Fib {
  public static void main(String args[]) {
    try (Scanner sc = new Scanner(System.in)) {
      int n = getInt(sc);
      if (n < 0) {
        System.out.println("Too small n");
      } else if (n > 47) {
        System.out.println("Too large n");
      } else {
        System.out.println(fibonacci(n));
      }
    }
  }

  private static int getInt(Scanner s) {
    int n = -1;
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

  private static int fibonacci(int n) {
    if (n < 0 || n > 47) {
      return -1;
    } else if (n < 2) {
      return n;
    }
    return fibonacci(n - 1) + fibonacci(n - 2);
  }
}
