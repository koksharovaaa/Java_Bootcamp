import java.util.Scanner;

public class AscOrder {
  public static void main(String args[]) {
    try (Scanner s = new Scanner(System.in)) {
      if (s.hasNextInt()) {
        int previous = s.nextInt();
        int current = previous;
        int id = 1;
        while (s.hasNextInt()) {
          current = s.nextInt();
          if (current < previous) {
            System.out.println(
                "The sequence is not ordered from the ordinal number of the number " + id);
            break;
          }
          previous = current;
          id++;
        }
        if (current >= previous) {
          System.out.println("The sequence is ordered in ascending order");
        }
      } else {
        System.out.println("Input error");
      }
    }
  }
}
