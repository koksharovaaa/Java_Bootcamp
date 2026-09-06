package tictactoe.domain.service;

import tictactoe.datasource.repository.Repository;
import tictactoe.domain.constants.Default;
import tictactoe.domain.model.Field;
import tictactoe.domain.model.Game;

public class ServiceImp implements Service {
  private final Repository repository;

  public ServiceImp(Repository repository) {
    this.repository = repository;
  }

  @Override
  public String check(Game game) {
    if (game == null || game.uuid() == null || game.isFieldNull()) {
      return Default.NOGAME;
    }

    Game old = repository.find(game.uuid());
    if (old == null) {
      repository.save(game);
      return null;
    }

    if (game.isEmpty()) {
      return Default.NOMOVE;
    }

    int diffCount = 0;
    for (int i = 0; i < Default.SIZE; i++) {
      for (int j = 0; j < Default.SIZE; j++) {
        if (game.cell(i, j) != old.cell(i, j)) {
          diffCount++;
          if (old.cell(i, j) != Default.FREE) {
            return Default.BADFIELD;
          }
        }
      }
    }
    if (diffCount > 1) {
      return Default.TOOMUCH;
    }

    if (diffCount < 1) {
      return Default.NOMOVE;
    }
    return null;
  }

  @Override
  public void takeTurn(Game game) {
    if (isGameOver(game) != Default.PLAYING) {
      return;
    }

    int weight = Integer.MIN_VALUE, y = -1, x = -1;
    for (int i = 0; i < Default.SIZE; i++) {
      for (int j = 0; j < Default.SIZE; j++) {
        if (game.cell(i, j) == Default.FREE) {
          Game g = new Game(game.uuid(), new Field(game.exportField()));
          g.set(i, j, Default.COMPUTER);
          int tmp = runMiniMax(g, Default.PLAYER);
          if (tmp > weight) {
            weight = tmp;
            y = i;
            x = j;
          }
        }
      }
    }

    if (y != -1 && x != -1) {
      game.set(y, x, Default.COMPUTER);
      repository.save(game);
    }
  }

  @Override
  public int isGameOver(Game game) {
    for (int i = 0; i < Default.SIZE; i++) {
      boolean v = true, h = true;
      for (int j = 1; j < Default.SIZE && (h || v); j++) {
        if (h && (game.cell(i, j) == Default.FREE || game.cell(i, j) != game.cell(i, j - 1))) {
          h = false;
        }
        if (v && (game.cell(j, i) == Default.FREE || game.cell(j, i) != game.cell(j - 1, i))) {
          v = false;
        }
      }

      if (h) {
        return (game.cell(i, 0) == Default.PLAYER) ? Default.PLAYER : Default.COMPUTER;
      }

      if (v) {
        return (game.cell(0, i) == Default.PLAYER) ? Default.PLAYER : Default.COMPUTER;
      }
    }

    boolean lr = true, rl = true;
    for (int i = 1; i < Default.SIZE; i++) {
      if (lr && (game.cell(i, i) == Default.FREE || game.cell(i, i) != game.cell(i - 1, i - 1))) {
        lr = false;
      }
      int s = Default.SIZE - 1 - i;
      if (rl && (game.cell(i, s) == Default.FREE || game.cell(i, s) != game.cell(i - 1, s + 1))) {
        rl = false;
      }
    }

    if (lr || rl) {
      return (game.cell(Default.SIZE / 2, Default.SIZE / 2) == Default.PLAYER)
          ? Default.PLAYER
          : Default.COMPUTER;
    }

    for (int i = 0; i < Default.SIZE; i++) {
      for (int j = 0; j < Default.SIZE; j++) {
        if (game.cell(i, j) == Default.FREE) {
          return Default.PLAYING;
        }
      }
    }

    return Default.DRAW;
  }

  private int runMiniMax(Game game, int turn) {
    int winState = isGameOver(game);
    if (winState == Default.COMPUTER) {
      return 10;
    }
    if (winState == Default.PLAYER) {
      return -10;
    }
    if (winState == Default.DRAW) {
      return 0;
    }

    int result = (turn == Default.PLAYER) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
    int actor = (turn == Default.PLAYER) ? Default.PLAYER : Default.COMPUTER;
    if (turn == Default.COMPUTER) {
      for (int i = 0; i < Default.SIZE; i++) {
        for (int j = 0; j < Default.SIZE; j++) {
          if (game.cell(i, j) == Default.FREE) {
            Game g = game.exportGame();
            g.set(i, j, actor);
            result = Math.max(result, runMiniMax(g, Default.PLAYER));
          }
        }
      }
    } else if (turn == Default.PLAYER) {
      for (int i = 0; i < Default.SIZE; i++) {
        for (int j = 0; j < Default.SIZE; j++) {
          if (game.cell(i, j) == Default.FREE) {
            Game g = game.exportGame();
            g.set(i, j, actor);
            result = Math.min(result, runMiniMax(g, Default.COMPUTER));
          }
        }
      }
    }
    return result;
  }
}
