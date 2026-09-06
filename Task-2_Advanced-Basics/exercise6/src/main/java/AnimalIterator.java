import java.util.List;

public class AnimalIterator implements BaseIterator<Animal> {
  private final List<Animal> list;
  private int id;

  public AnimalIterator(List<Animal> pets) {
    this.list = pets;
    this.id = 0;
  }

  @Override
  public Animal next() {
    Animal a = list.get(id);
    id++;
    return a;
  }

  @Override
  public boolean hasNext() {
    return id < this.list.size();
  }

  @Override
  public void reset() {
    this.id = 0;
  }
}

interface BaseIterator<T> {
  abstract T next();

  abstract boolean hasNext();

  abstract void reset();
}
