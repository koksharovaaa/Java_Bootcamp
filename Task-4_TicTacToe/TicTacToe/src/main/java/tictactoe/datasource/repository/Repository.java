package tictactoe.datasource.repository;

import java.util.UUID;
import tictactoe.domain.model.Game;

public interface Repository {
  public abstract void save(Game game);

  public abstract Game find(UUID uuid);
}
