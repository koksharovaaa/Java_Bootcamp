package presentation;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import domain.Game;
import domain.Icon;
import domain.transfer.Score;
import java.io.IOException;
import java.util.ArrayList;

public class GameScreen {
  Terminal terminal = null;
  Screen screen = null;
  final TextGraphics text;
  final int panel = 26;

  public GameScreen() throws IOException {
    DefaultTerminalFactory f = new DefaultTerminalFactory();
    f.setForceTextTerminal(true);
    this.terminal = f.createTerminal();

    this.screen = new TerminalScreen(terminal);
    this.screen.startScreen();
    this.screen.clear();
    this.screen.setCursorPosition(null);

    this.text = screen.newTextGraphics();
    text.setForegroundColor(TextColor.ANSI.WHITE);
    text.setBackgroundColor(TextColor.ANSI.BLACK);
  }

  public void render(Game game, ArrayList<Score> scores) throws IOException {
    switch (game.state()) {
      case STANDBY -> {
        startScreen(game);
      }
      case SCOREBOARD -> {
        scoreBoardScreen(game, scores);
      }
      case PLAYING -> {
        rogueScreen(game);
      }
      case GAMEOVER -> {
        gameoverScreen(game);
      }
    }
    screen.refresh();
  }

  private void startScreen(Game g) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);

    int w = g.resX() + panel + 5;
    int h = g.resY() + 2;
    printFrame(0, 0, w, g.resY() + 2);

    int line = 7;
    for (String s : Line.ROGUE) {
      text.putString(32, line++, s);
    }

    text.putString((w - Line.START.length()) / 2, h / 2, Line.START);
    if (g.loadedFromSave()) {
      text.putString((w - Line.LOAD.length()) / 2, h / 2 + 1, Line.LOAD);
      text.putString((w - Line.SCOREBOARD.length()) / 2, h / 2 + 2, Line.SCOREBOARD);
      text.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
      text.putString((w - Line.QUIT.length()) / 2, h / 2 + 4, Line.QUIT);
      text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    } else {
      text.putString((w - Line.SCOREBOARD.length()) / 2, h / 2 + 1, Line.SCOREBOARD);
      text.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
      text.putString((w - Line.QUIT.length()) / 2, h / 2 + 3, Line.QUIT);
      text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    }
  }

  private void scoreBoardScreen(Game g, ArrayList<Score> s) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);

    int w = g.resX() + panel + 5;
    int h = g.resY() + 2;
    printFrame(0, 0, w, h);

    int line = 8;
    text.putString((w - Line.SCORES.length()) / 2, line, Line.SCORES);
    ++line;

    int offset = (w - Line.SCOREHEADER.length()) / 2;
    text.putString(offset, ++line, Line.SCOREHEADER);

    for (Score result : s) {
      if (result.state.equals("LOST")) {
        text.setForegroundColor(TextColor.ANSI.RED);
      } else {
        text.setForegroundColor(TextColor.ANSI.GREEN);
      }
      text.putString(offset, ++line, result.toRow());
    }

    text.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
    if (s.isEmpty()) {
      ++line;
      text.putString((w - Line.SCORESEMPTY.length()) / 2, ++line, Line.SCORESEMPTY);
    }
    text.putString((w - Line.SCORESBACK.length()) / 2, h - 7, Line.SCORESBACK);
    text.putString((w - Line.QUIT.length()) / 2, h - 6, Line.QUIT);
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
  }

  private void rogueScreen(Game g) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);

    printBorder(0, 0, g.resX() + 2, g.resY() + 2);
    fillArea(g.resX() + 2, 0, panel + 3, g.resY() + 2, ' ');
    printField(g, 1, 1);
    printStats(g, g.resX() + 4, 0);
    printLog(g, g.resX() + 4, g.resY() - 23);

    if (g.paused()) {
      pauseScreen(g);
      return;
    }

    if (g.backpackOpen()) {
      backpackScreen(g);
    }
  }

  private void pauseScreen(Game g) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);
    int w = g.resX() + 2;
    int h = g.resY() + 2;

    int boxW = 28;
    int boxH = 12;
    int sx = (w - boxW) / 2;
    int sy = (h - boxH) / 2;

    printFrame(sx, sy, boxW, boxH);
    text.putString((w - Line.GAMEPAUSED.length()) / 2, sy + 2, Line.GAMEPAUSED);

    text.putString((w - Line.CTRL.length()) / 2, sy + 4, Line.CTRL);
    text.putString(sx + 3, sy + 5, "W, A, S, D: move");
    text.putString(sx + 3, sy + 6, "H, J, K, E: backpack");
    text.putString(sx + 3, sy + 7, "P: resume");
    text.putString(sx + 3, sy + 8, "Q: quit");
    text.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
    text.putString((w - Line.PAUSE.length()) / 2, sy + 10, Line.PAUSE);
  }

  private void backpackScreen(Game g) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);

    int w = g.resX() + 2;
    int h = g.resY() + 2;

    int boxW = Line.USEITEM.length() + 5;
    int boxH = g.backpackInfo.size() + (g.backpackInfo.isEmpty() ? 6 : 7);
    int sx = (w - boxW) / 2;
    int sy = (h - boxH) / 2;

    int line = sy + 2;
    printFrame(sx, sy, boxW, boxH);
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);

    switch (g.backpackTab()) {
      case FOOD -> {
        text.putString((w - Line.FOOD.length()) / 2, line, Line.FOOD);
      }
      case ELIXIR -> {
        text.putString((w - Line.ELIXIR.length()) / 2, line, Line.ELIXIR);
      }
      case SCROLL -> {
        text.putString((w - Line.SCROLL.length()) / 2, line, Line.SCROLL);
      }
      case WEAPON -> {
        text.putString((w - Line.WEAPON.length()) / 2, line, Line.WEAPON);
      }
    }

    line += 2;
    if (g.backpackInfo.isEmpty()) {
      text.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
      text.putString((g.resX() - Line.EMPTY.length()) / 2, line, Line.EMPTY);
    } else {
      for (int i = 0; i < g.backpackInfo.size(); i++) {
        text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        text.putString(sx + 2, line + i, g.backpackInfo.get(i));
        text.setForegroundColor(TextColor.ANSI.WHITE);
        text.putString(sx + boxW - 5, line + i, "(" + Integer.toString(i + 1) + ")");
      }
      line += g.backpackInfo.size() + 1;
      text.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
      text.putString((g.resX() - Line.USEITEM.length()) / 2, line, Line.USEITEM);
    }
  }

  private void printField(Game g, int x, int y) throws IOException {
    for (int i = 0; i < g.resY(); i++) {
      for (int j = 0; j < g.resX(); j++) {
        Icon icon = g.glyphAt(i, j);
        text.setForegroundColor(icon.colour);
        text.putString(j + x, i + y, Character.toString(icon.glyph));
      }
    }
  }

  private void printStats(Game g, int x, int y) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);

    int line = y + 2;
    printBorder(x, y, panel + 1, g.playerInfo.size() + 4);

    text.putString(x + (panel + 1 - Line.PLAYER.length()) / 2, line++, Line.PLAYER);

    for (String s : g.playerInfo) {
      text.putString(x + 2, line++, s);
    }

    if (!g.enemyInfo.isEmpty()) {
      line++;
      printBorder(x, line, panel + 1, g.enemyInfo.size() + 4);

      line += 2;
      text.putString(x + (panel + 1 - Line.ENEMY.length()) / 2 + 1, line++, Line.ENEMY);
      for (String s : g.enemyInfo) {
        text.putString(x + 2, line++, s);
      }
    }
  }

  private void printLog(Game g, int x, int y) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);

    printBorder(x, y, panel + 1, g.log.isEmpty() ? 4 : g.log.size() + 5);
    int line = y + 2;
    text.putString(x + (panel + 1 - Line.COMBAT.length()) / 2, line++, Line.COMBAT);
    line++;

    int black = (g.log.size() > 2 ? 1 : 0);
    int grey = (g.log.size() > 1 ? 1 : 0);
    if (black != 0) {
      grey = 2;
    }

    text.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
    for (int i = 0; i < black; i++) {
      text.putString(x + 2, line++, g.log.get(i));
    }

    text.setForegroundColor(TextColor.ANSI.WHITE);
    for (int i = black; i < grey; i++) {
      text.putString(x + 2, line++, g.log.get(i));
    }

    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    for (int i = grey; i < g.log.size(); i++) {
      text.putString(x + 2, line++, g.log.get(i));
    }
  }

  private void gameoverScreen(Game g) throws IOException {
    if (g.level() < Game.FINALLEVEL) {
      lossScreen(g);
    } else {
      victoryScreen(g);
    }
  }

  private void victoryScreen(Game g) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);

    int w = g.resX() + panel + 5;
    int h = g.resY() + 2;
    printFrame(0, 0, w, h);
    text.putString((w - Line.OVER.length()) / 2, h / 2 - 1, Line.OVER);
    text.putString((w - Line.WIN.length()) / 2, h / 2, Line.WIN);
    text.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
    text.putString((w - Line.RESTART.length()) / 2, h / 2 + 4, Line.RESTART);
    text.putString((w - Line.QUIT.length()) / 2, h / 2 + 5, Line.QUIT);
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);

    int line = 9;
    text.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
    for (String s : Line.WINICON) {
      text.putString(49, line++, s);
    }
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
  }

  private void lossScreen(Game g) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);

    int w = g.resX() + panel + 5;
    int h = g.resY() + 2;
    printFrame(0, 0, w, h);
    text.putString((w - Line.OVER.length()) / 2, h / 2 - 1, Line.OVER);
    text.putString((w - Line.LOSS.length()) / 2, h / 2, Line.LOSS);
    text.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
    text.putString((w - Line.RESTART.length()) / 2, h / 2 + 4, Line.RESTART);
    text.putString((w - Line.QUIT.length()) / 2, h / 2 + 5, Line.QUIT);
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);

    int line = 11;
    text.setForegroundColor(TextColor.ANSI.WHITE);
    for (String s : Line.LOSSICON) {
      text.putString(33, line++, s);
    }
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
  }

  private void printFrame(int x, int y, int w, int h) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);
    printBorder(x, y, w, h);
    fillArea(x + 1, y + 1, w - 2, h - 2, ' ');
  }

  private void printBorder(int x, int y, int w, int h) throws IOException {
    char cX = '_';
    char cY = '|';

    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);

    for (int i = 0; i <= w - 1; i++) {
      text.putString(x + i, y, Character.toString(cX));
      text.putString(x + i, y + h - 1, Character.toString(cX));
    }

    for (int i = 0; i <= h - 1; i++) {
      text.putString(x, y + i, Character.toString(cY));
      text.putString(x + w - 1, y + i, Character.toString(cY));
    }

    text.putString(x, y, Character.toString(cX));
    text.putString(x + w - 1, y, Character.toString(cX));
  }

  private void fillArea(int x, int y, int w, int h, char ch) throws IOException {
    text.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
    text.setBackgroundColor(TextColor.ANSI.BLACK);
    for (int i = y; i < y + h; i++) {
      for (int j = x; j < x + w; j++) {
        text.putString(j, i, Character.toString(ch));
      }
    }
  }

  public void exit() throws IOException {
    screen.stopScreen();
    screen.close();
    terminal.close();
  }

  public KeyStroke getInput() throws IOException {
    KeyStroke k;
    do {
      k = screen.readInput();
    } while (k.getKeyType() != KeyType.Character);
    return k;
  }
}
