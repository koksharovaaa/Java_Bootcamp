package domain;

import com.googlecode.lanterna.TextColor;

public enum Tile {
  FLOOR(new Icon('.', TextColor.ANSI.BLACK_BRIGHT)),
  WALL(new Icon('#', TextColor.ANSI.WHITE)),
  CORRIDOR(new Icon('+', TextColor.ANSI.WHITE)),
  EXIT(new Icon('X', TextColor.ANSI.WHITE)),
  VOID(new Icon(' ', TextColor.ANSI.WHITE)),

  DOOR_RED(new Icon('D', TextColor.ANSI.RED)),
  DOOR_YELLOW(new Icon('D', TextColor.ANSI.YELLOW)),
  DOOR_GREEN(new Icon('D', TextColor.ANSI.GREEN)),
  DOOR_CYAN(new Icon('D', TextColor.ANSI.CYAN)),
  DOOR_BLUE(new Icon('D', TextColor.ANSI.BLUE));

  private final Icon icon;

  private Tile(Icon icon) {
    this.icon = icon;
  }

  public Icon icon() {
    return this.icon;
  }

  public int id() {
    return this.ordinal();
  }

  public static Tile fromId(int id) {
    return values()[id];
  }
}
