import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PetAge {
  public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in)) {
      int size = getInt(sc);
      if (size <= 0) {
        System.out.println("Input error. Size <= 0");
        return;
      }

      List<Animal> pets =
          Stream.generate(() -> getPet(sc))
              .limit(size)
              .filter(x -> x != null)
              .collect(Collectors.toList());

      pets.stream()
          .forEach(
              e -> {
                if (e.getAge() > 10) {
                  e.setAge(e.getAge() + 1);
                }
              });

      pets.stream().forEach(e -> System.out.println(e.toString()));
    }
  }

  static int getInt(Scanner sc) {
    while (sc.hasNext()) {
      String line = sc.nextLine();
      try {
        int n = Integer.parseInt(line);
        return n;
      } catch (NumberFormatException  e) {
        System.out.println("Could not parse a number. Please, try again");
      }
    }
    return 0;
  }

  static Animal getPet(Scanner sc) {
    if (!sc.hasNext()) {
      return null;
    }
    String type = sc.nextLine();
    if (!type.equalsIgnoreCase("dog") && !type.equalsIgnoreCase("cat")) {
      System.out.println("Incorrect input. Unsupported pet type");
      return null;
    }

    if (!sc.hasNext()) {
      return null;
    }
    String name = sc.nextLine();
    int age = getInt(sc);

    if (age <= 0) {
      System.out.println("Incorrect input. Age <= 0");
      return null;
    }

    Animal a = null;
    if (type.equalsIgnoreCase("dog")) {
      a = new Dog(name, age);
    } else if (type.equalsIgnoreCase("cat")) {
      a = new Cat(name, age);
    }

    return a;
  }
}
