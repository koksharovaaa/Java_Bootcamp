package domain.items;

public enum ItemType {
  FOOD(false, 0),
  ELIXIR(true, 10),
  SCROLL(false, 0),
  WEAPON(true, 20),
  TREASURE(false, 0),
  KEY(false, 0);

  public final boolean temporary;
  public final int duration;

  private ItemType(boolean temporary, int duration) {
    this.temporary = temporary;
    this.duration = duration;
  }
}
