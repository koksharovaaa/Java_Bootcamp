import java.util.InputMismatchException;
import java.util.Scanner;

public class Perimeter {
  public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in)) {
      while (true) {
        try {
          double ay = getDouble(sc);
          double ax = getDouble(sc);
          double by = getDouble(sc);
          double bx = getDouble(sc);
          double cy = getDouble(sc);
          double cx = getDouble(sc);

          double l1 = sideLendth(ay, ax, by, bx);
          double l2 = sideLendth(ay, ax, cy, cx);
          double l3 = sideLendth(by, bx, cy, cx);

          if (isTriangle(l1, l2, l3)) {
            System.out.printf("Perimeter: %.3f\n", l1 + l2 + l3);
          } else {
            System.out.println("It's not a triangle");
          }
          break;
        } catch (NumberFormatException e) {
          System.out.println("Could not parse a number. Please, try again");
        }
      }
    }
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

  private static double sideLendth(double y1, double x1, double y2, double x2) {
    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }

  private static boolean isTriangle(double a, double b, double c) {
    return a + b > c && a + c > b && b + c > a;
  }
}
