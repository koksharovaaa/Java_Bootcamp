public abstract class Animal {
  private final String name;
  private final int age;

  public Animal(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }
}

interface Herbivore {
  abstract String chill();
}

interface Omnivore {
  abstract String hunt();
}
