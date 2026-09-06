package tictactoe.domain.service;

import tictactoe.domain.model.Game;

public interface Service {
  public abstract String check(Game g);

  public abstract void takeTurn(Game g);

  public abstract int isGameOver(Game g);
}
