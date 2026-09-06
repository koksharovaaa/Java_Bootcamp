package tictactoe.domain.model;

import java.util.UUID;

public class Game {
  private final UUID uuid;
  private final Field field;

  public Game(UUID uuid, Field field) {
    this.uuid = uuid;
    this.field = field;
  }

  public UUID uuid() {
    return this.uuid;
  }

  public boolean isFieldNull() {
    return this.field == null;
  }

  public boolean isEmpty() {
    return field.isEmpty();
  }

  public int cell(int y, int x) {
    return this.field.cell(y, x);
  }

  public void set(int y, int x, int cell) {
    this.field.set(y, x, cell);
  }

  public int[][] exportField() {
    return field.exportField();
  }

  public Game exportGame() {
    return new Game(uuid, new Field(this.exportField()));
  }
}
