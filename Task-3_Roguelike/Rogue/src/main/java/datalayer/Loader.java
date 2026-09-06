package datalayer;

import com.google.gson.Gson;
import domain.Game;
import domain.transfer.GameData;
import domain.transfer.Score;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

public class Loader {
  private static final String SAVENAME = "GameData.json";
  private static final String SCORENAME = "GameScore.json";
  private final Gson gson;
  private final Path directory;

  public Loader() throws IOException {
    gson = new Gson();
    directory = Path.of("../Saves");
    if (!Files.exists(directory)) {
      Files.createDirectories(directory);
    }
  }

  public void saveGame(GameData gamedata) throws IOException {
    if (gamedata == null) {
      return;
    }

    String str;
    if (gamedata.playing == false) {
      str = "";
    } else {
      str = gson.toJson(gamedata);
    }

    Path save = directory.resolve(SAVENAME);
    Files.writeString(save, str);
  }

  public GameData loadGame() throws IOException {
    Path save = directory.resolve(SAVENAME);
    if (Files.exists(save)) {
      String str = Files.readString(save);
      GameData gameData = gson.fromJson(str, GameData.class);
      return gameData;
    }
    return null;
  }

  public ArrayList<Score> loadScores() throws IOException {
    Path save = directory.resolve(SCORENAME);
    if (Files.exists(save)) {
      String str = Files.readString(save);
      ScoreBoard board = gson.fromJson(str, ScoreBoard.class);
      if (board != null) {
        sortScores(board.scores);
      }

      return (board == null ? new ArrayList<>() : board.scores);
    }
    return new ArrayList<>();
  }

  public void saveScores(Game game, ArrayList<Score> scores) throws IOException {
    Score score = game.exportScore();
    if (score != null) {
      scores.add(score);
      sortScores(scores);
      ScoreBoard board = new ScoreBoard();
      board.scores = scores;
      String str = gson.toJson(board);
      Path save = directory.resolve(SCORENAME);
      Files.writeString(save, str);
    }
  }

  private void sortScores(ArrayList<Score> scores) {
    scores.sort(Comparator.reverseOrder());
    if (scores.size() >= 10) {
      scores.subList(10, scores.size()).clear();
    }
  }
}
