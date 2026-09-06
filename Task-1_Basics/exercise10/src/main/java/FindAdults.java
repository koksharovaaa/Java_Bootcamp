import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FindAdults {
  public static void main(String args[]) {
    try (Scanner sc = new Scanner(System.in)) {
      int size = getInt(sc);
      if (size <= 0) {
        System.out.println("Input error. Size <= 0");
      }
      ArrayList<User> list = new ArrayList<>();

      for (int i = 0; i < size; i++) {
        String name = sc.nextLine();
        int age = getInt(sc);
        if (age <= 0) {
          System.out.println("Incorrect input. Age <= 0");
          i--;
        } else {
          list.add(new User(name, age));
        }
      }

      List<String> names =
          list.stream()
              .filter(u -> u.getAge() >= 18)
              .map(User::getName)
              .collect(Collectors.toList());

      System.out.println(names);
    }
  }

  private static int getInt(Scanner sc) {
    int n = 0;
    while (true) {
      try {
        n = sc.nextInt();
        sc.nextLine();
        break;
      } catch (InputMismatchException e) {
        System.out.println("Could not parse a number. Please, try again");
        sc.next();
      }
    }
    return n;
  }
}
