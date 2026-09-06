import java.util.Locale;

public class Cat extends Animal {
  public Cat(String name, int age, double weight) {
    super(name, age, weight);
  }

  @Override
  public String toString() {
    return String.format(
      Locale.US,
      "Cat name = %s, age = %d, mass = %.2f, feed = %.2f",
      getName(),
      getAge(),
      getWeight(),
      getFeedInfoKg());
  }

  @Override
  public double getFeedInfoKg() {
    return getWeight() * 0.1;
  }
}
