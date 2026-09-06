import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PetIterator {
  public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in)) {
      int size = getInt(sc);
      if (size <= 0) {
        System.out.println("Input error. Size <= 0");
        return;
      }

      List<Animal> pets = new ArrayList<>();

      for (int i = 0; i < size; i++) {
        getPet(sc, pets);
      }

      AnimalIterator it = new AnimalIterator(pets);
      if (it.next() != null) {
        it.reset();
        do {
          System.out.println(it.next().toString());
        } while (it.hasNext());
      }
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

  static boolean getPet(Scanner sc, List<Animal> pets) {
    if (!sc.hasNext()) {
      return false;
    }
    String type = sc.nextLine();
    if (!type.equalsIgnoreCase("dog") && !type.equalsIgnoreCase("cat")) {
      System.out.println("Incorrect input. Unsupported pet type");
      return false;
    }

    if (!sc.hasNext()) {
      return false;
    }
    String name = sc.nextLine();
    int age = getInt(sc);

    if (age <= 0) {
      System.out.println("Incorrect input. Age <= 0");
      return false;
    }

    if (type.equalsIgnoreCase("dog")) {
      pets.add(new Dog(name, age));
    } else if (type.equalsIgnoreCase("cat")) {
      pets.add(new Cat(name, age));
    }

    return true;
  }
}
