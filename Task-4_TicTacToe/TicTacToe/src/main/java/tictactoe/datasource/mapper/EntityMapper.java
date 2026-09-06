package tictactoe.datasource.mapper;

import tictactoe.datasource.model.FieldEntity;
import tictactoe.datasource.model.GameEntity;
import tictactoe.domain.model.Field;
import tictactoe.domain.model.Game;

public class EntityMapper {
  public static Game toGame(GameEntity entity) {
    return new Game(entity.uuid(), new Field(entity.exportField()));
  }

  public static GameEntity toEntity(Game game) {
    return new GameEntity(game.uuid(), new FieldEntity(game.exportField()));
  }
}
