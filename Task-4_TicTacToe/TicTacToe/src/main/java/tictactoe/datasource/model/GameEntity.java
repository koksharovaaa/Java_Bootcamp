package tictactoe.datasource.model;

import java.util.UUID;

public class GameEntity {
  private final UUID uuid;
  private final FieldEntity fieldEntity;

  public GameEntity(UUID uuid, FieldEntity fieldEntity) {
    this.uuid = uuid;
    this.fieldEntity = fieldEntity;
  }

  public UUID uuid() {
    return this.uuid;
  }

  public int[][] exportField() {
    return fieldEntity.exportField();
  }
}
