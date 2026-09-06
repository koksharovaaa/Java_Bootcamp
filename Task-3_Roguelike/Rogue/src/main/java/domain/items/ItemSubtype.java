package domain.items;

import com.googlecode.lanterna.TextColor;
import domain.Icon;
import domain.Stats;

public enum ItemSubtype {
  FOOD_APPLE(new Icon('a', TextColor.ANSI.RED_BRIGHT), 0, "Apple", new Stats(2, 0, 0, 0)),
  FOOD_BREAD(new Icon('b', TextColor.ANSI.YELLOW), 0, "Bread", new Stats(6, 0, 0, 0)),
  FOOD_CAKE(new Icon('c', TextColor.ANSI.WHITE_BRIGHT), 0, "Cake", new Stats(10, 0, 0, 0)),

  ELIXIR_HP(new Icon('i', TextColor.ANSI.CYAN_BRIGHT), 0, "Elixir (HP)", new Stats(15, 15, 0, 0)),
  ELIXIR_STR(new Icon('i', TextColor.ANSI.CYAN_BRIGHT), 0, "Elixir (STR)", new Stats(0, 0, 8, 0)),
  ELIXIR_AGI(new Icon('i', TextColor.ANSI.CYAN_BRIGHT), 0, "Elixir (AGI)", new Stats(0, 0, 0, 8)),

  SCROLL_HP(new Icon('s', TextColor.ANSI.BLUE_BRIGHT), 0, "Scroll (HP)", new Stats(8, 8, 0, 0)),
  SCROLL_STR(new Icon('s', TextColor.ANSI.BLUE_BRIGHT), 0, "Scroll (STR)", new Stats(0, 0, 5, 0)),
  SCROLL_AGI(new Icon('s', TextColor.ANSI.BLUE_BRIGHT), 0, "Scroll (AGI)", new Stats(0, 0, 0, 4)),

  WEAPON_DAGGER(new Icon('k', TextColor.ANSI.BLACK_BRIGHT), 0, "Dagger", new Stats(0, 0, 5, 0)),
  WEAPON_SWORD(new Icon('l', TextColor.ANSI.BLACK_BRIGHT), 0, "Sword", new Stats(0, 0, 10, 0)),
  WEAPON_MACE(new Icon('h', TextColor.ANSI.BLACK_BRIGHT), 0, "Mace", new Stats(0, 0, 0, 20)),

  TREASURE_POUCH(new Icon('p', TextColor.ANSI.YELLOW_BRIGHT), 10, "Pouch", new Stats(0, 0, 0, 0)),
  TREASURE_BAG(new Icon('b', TextColor.ANSI.YELLOW_BRIGHT), 50, "Bag", new Stats(0, 0, 0, 0)),
  TREASURE_CHEST(new Icon('c', TextColor.ANSI.YELLOW_BRIGHT), 100, "Chest", new Stats(0, 0, 0, 0)),

  KEY_RED(new Icon('&', TextColor.ANSI.RED), 0, "K RED", new Stats(0, 0, 0, 0)),
  KEY_YELLOW(new Icon('&', TextColor.ANSI.YELLOW), 0, "K YELLOW", new Stats(0, 0, 0, 0)),
  KEY_GREEN(new Icon('&', TextColor.ANSI.GREEN), 0, "K GREEN", new Stats(0, 0, 0, 0)),
  KEY_CYAN(new Icon('&', TextColor.ANSI.CYAN), 0, "K CYAN", new Stats(0, 0, 0, 0)),
  KEY_BLUE(new Icon('&', TextColor.ANSI.BLUE), 0, "K BLUE", new Stats(0, 0, 0, 0));

  public final Icon icon;
  public final int cost;
  public final String name;
  public final Stats bonus;

  private ItemSubtype(Icon icon, int cost, String name, Stats stats) {
    this.icon = icon;
    this.cost = cost;
    this.name = name;
    this.bonus = stats;
  }
}
