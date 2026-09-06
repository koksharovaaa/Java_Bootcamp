package tictactoe.datasource.repository;

import java.util.UUID;

import tictactoe.datasource.mapper.EntityMapper;
import tictactoe.datasource.model.GameEntity;
import tictactoe.domain.model.Game;

public class RepositoryImp implements Repository {
  private final Storage storage;

  public RepositoryImp(Storage storage) {
    this.storage = storage;
  }

  @Override
  public void save(Game game) {
    if (game == null) {
      return;
    }

    GameEntity entity = EntityMapper.toEntity(game);
    storage.entities().put(entity.uuid(), entity);
  }

  @Override
  public Game find(UUID uuid) {
    GameEntity entity = storage.entities().get(uuid);

    if (entity == null) {
      return null;
    }

    Game game = EntityMapper.toGame(entity);
    return game;
  }
}
