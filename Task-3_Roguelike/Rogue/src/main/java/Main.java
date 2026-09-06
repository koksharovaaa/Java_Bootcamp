import com.googlecode.lanterna.input.KeyStroke;
import datalayer.Loader;
import domain.Game;
import domain.GameState;
import domain.transfer.GameData;
import domain.transfer.Score;
import java.io.IOException;
import java.util.ArrayList;
import presentation.GameScreen;

public class Main {
  public static void main(String[] args) {
    try {
      gameCycle();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      e.printStackTrace(System.err);
    }
  }

  private static void gameCycle() throws IOException {
    GameScreen gameScreen = new GameScreen();
    try {
      Loader loader = new Loader();
      GameData gameData = loader.loadGame();
      ArrayList<Score> scores = loader.loadScores();
      Game game = Game.importGame(gameData);
      while (game.state() != GameState.QUIT) {
        gameScreen.render(game, scores);
        KeyStroke k = gameScreen.getInput();
        game.processInput(k);
        if (game.savePending()) {
          loader.saveGame(game.exportGame());
        }
      }
      loader.saveScores(game, scores);
      loader.saveGame(game.exportGame());
    } finally {
      gameScreen.exit();
    }
  }
}
