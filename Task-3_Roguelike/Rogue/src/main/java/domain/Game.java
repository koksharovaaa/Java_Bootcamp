package domain;

import com.googlecode.lanterna.input.KeyStroke;
import domain.items.ItemType;
import domain.transfer.GameData;
import domain.transfer.Score;
import java.util.ArrayList;
import java.util.Iterator;

public class Game {
  private final int resY = Constants.WORLDH;
  private final int resX = Constants.WORLDW;
  private World w;

  public static final int FINALLEVEL = 21;
  private boolean savePending = false;
  private boolean loadedFromSave = false;

  private GameState state = GameState.STANDBY;
  private boolean initialized = false;
  private boolean paused = false;
  private boolean backpackOpen = false;

  public ArrayList<String> playerInfo; // for player stats
  public ArrayList<String> enemyInfo; // for enemy stats

  public ArrayList<String> backpackInfo;
  private ItemType backpackTab = null;

  public ArrayList<String> log; // for messages

  private Game() {
    this.w = new World();
    this.playerInfo = new ArrayList<>();
    this.enemyInfo = new ArrayList<>();
    this.backpackInfo = new ArrayList<>();
    this.log = new ArrayList<>();
  }

  public boolean initialized() {
    return this.initialized;
  }

  public GameState state() {
    return this.state;
  }

  public int level() {
    return this.w.level();
  }

  public int resY() {
    return this.resY;
  }

  public int resX() {
    return this.resX;
  }

  public boolean paused() {
    return this.paused;
  }

  public boolean backpackOpen() {
    return this.backpackOpen;
  }

  public ItemType backpackTab() {
    return this.backpackTab;
  }

  public boolean savePending() {
    return this.savePending;
  }

  public boolean loadedFromSave() {
    return this.loadedFromSave;
  }

  public void processInput(KeyStroke k) {
    char c = Character.toLowerCase(k.getCharacter());
    if (c == 'q') {
      state = GameState.QUIT;
    }

    switch (state) {
      case STANDBY -> {
        if (c == 's') {
          clearGame();
          onNewLevel();
          state = GameState.PLAYING;
        } else if (c == 'c' && loadedFromSave) {
          state = GameState.PLAYING;
        } else if (c == 'l') {
          state = GameState.SCOREBOARD;
        }

        if (state == GameState.PLAYING) {
          initialized = true;
        }
      }
      case SCOREBOARD -> {
        if (c == 'l') {
          state = GameState.STANDBY;
        }
      }
      case PLAYING -> {
        clearActions();
        onPlaying(c);
        updatePlayerInfo();
        updateEnemyInfo();
        updateLog();
        if (w.level() > FINALLEVEL || w.player.hp() <= 0) {
          state = GameState.GAMEOVER;
        }
      }
      case GAMEOVER -> {
        if (c == 'r') {
          state = GameState.PLAYING;
          loadedFromSave = false;
          onNewLevel();
        }
      }
    }
  }

  private void onNewLevel() {
    Factory.createLayout(w);
    updatePlayerInfo();
    updateEnemyInfo();
    w.player.updateSeen(w);
    w.player.dropKeys();
  }

  private void onPlaying(char c) {
    savePending = false;
    if (c == 'p') {
      paused = !paused;
      return;
    }

    if (paused) {
      return;
    }

    if (!backpackOpen && isBackpackKey(c)) {
      backpackOpen = true;
      backpackTab = null;
    }

    if (backpackOpen) {
      if (isItemKey(c)) {
        w.player.useItem(backpackTab, c, w);
        if (c == '0') {
          backpackInfo.clear();
          backpackInfo = w.player.backpack.getItems(backpackTab);
        } else {
          backpackOpen = false;
        }
        return;
      }

      ItemType tab = backpackTabType(c);
      if (backpackTab == tab || !isBackpackKey(c)) {
        backpackOpen = false;
        return;
      }
      backpackTab = tab;
      backpackOpen = true;
      backpackInfo.clear();
      backpackInfo = w.player.backpack.getItems(backpackTab);
      return;
    }
    updateScene(c);
  }

  private boolean isBackpackKey(char c) {
    return (c == 'h' || c == 'j' || c == 'k' || c == 'e');
  }

  private ItemType backpackTabType(char c) {
    switch (c) {
      case 'h' -> {
        return ItemType.WEAPON;
      }
      case 'j' -> {
        return ItemType.FOOD;
      }
      case 'k' -> {
        return ItemType.ELIXIR;
      }
      case 'e' -> {
        return ItemType.SCROLL;
      }
    }
    return null;
  }

  private boolean isItemKey(char c) {
    return Character.isDigit(c);
  }

  private void updatePlayerInfo() {
    playerInfo.clear();
    String str = "EASY";
    if (w.diff == GameDifficulty.MEDIUM) {
      str = "MID";
    } else if (w.diff == GameDifficulty.HARD) {
      str = "HARD";
    }
    playerInfo.add("LEVEL: " + w.level() + " (" + str + ")");

    int tmp = w.player.hp();
    str = "HP: " + tmp;
    if (tmp > w.player.baseHp()) {
      str += "*";
    }

    tmp = w.player.maxHp();
    str += "/" + tmp;
    if (tmp > w.player.baseMaxHp()) {
      str += "*";
    }
    playerInfo.add(str);

    tmp = w.player.str();
    str = "STR: " + tmp;
    if (tmp > w.player.baseStr()) {
      str += "*";
    }
    tmp = w.player.weaponStr();
    if (tmp != 0) {
      str += " + " + tmp;
    }
    playerInfo.add(str);

    tmp = w.player.agi();
    str = "AGI: " + tmp;
    if (tmp > w.player.baseAgi()) {
      str += "*";
    }
    playerInfo.add(str);

    if (w.player.weapon() != null) {
      playerInfo.add("WEAPON: " + w.player.weapon() + " (" + w.player.weaponDuration() + ")");
    }
    playerInfo.add("GOLD: " + w.player.gold());
  }

  private void clearActions() {
    w.player.actions.clear();
    for (Enemy e : w.enemies) {
      e.actions.clear();
    }
  }

  private void updateEnemyInfo() {
    enemyInfo.clear();
    Enemy e = w.player.lastAttacked();
    if (e != null) {
      enemyInfo.add(e.name + ": " + e.hp() + "/" + e.maxHp());
    }
  }

  private void updateLog() {
    log.addAll(w.player.actions);
    for (Enemy e : w.enemies) {
      log.addAll(e.actions);
    }

    if (log.size() > 20) {
      log.subList(0, log.size() - 20).clear();
    }
  }

  private void updateScene(char c) {
    w.player.takeTurn(w, c);

    if (w.tiles[w.player.y()][w.player.x()] == Tile.EXIT) {
      w.increaseLevel();
      onNewLevel();
      savePending = true;
      log.clear();
      return;
    }

    for (Enemy e : w.enemies) {
      e.takeTurn(w);
    }

    Iterator<Enemy> it = w.enemies.iterator();
    while (it.hasNext()) {
      if (it.next().hp() < 1) {
        it.remove();
      }
    }
  }

  public Icon glyphAt(int y, int x) {
    if (y < 0 || y >= w.h() || x < 0 || x >= w.w()) {
      return Tile.VOID.icon();
    }

    Player p = w.playerAt(y, x);
    if (p != null) {
      return p.icon();
    }

    Enemy e = w.enemyAt(y, x);
    if (w.player.sees(w, y, x) && e != null && e.visible()) {
      return e.icon();
    }

    if (w.player.sees(w, y, x) && w.items[y][x] != null) {
      return w.items[y][x].icon();
    }

    if (w.player.sees(w, y, x) || w.visited[y][x]) {
      return w.tiles[y][x].icon();
    }

    return Tile.VOID.icon();
  }

  public GameData exportGame() {
    if (!initialized) {
      return null;
    }
    GameData gameData = new GameData();
    gameData.playing = false;
    gameData.world = null;
    if (w.player.hp() > 0 && w.level() <= FINALLEVEL) {
      gameData.playing = true;
      gameData.world = w.exportWorld();
    }
    return gameData;
  }

  public static Game importGame(GameData gameData) {
    Game game = new Game();

    if (gameData != null && gameData.world != null) {
      game.loadedFromSave = true;
      game.w = World.importWorld(gameData.world);
      game.w.player.updateSeen(game.w);
      game.updatePlayerInfo();
      game.updateEnemyInfo();
    }
    return game;
  }

  private void clearGame() {
    savePending = false;
    loadedFromSave = false;
    paused = false;
    backpackOpen = false;
    w.clearWorld();
  }

  public Score exportScore() {
    if (w.level() > FINALLEVEL || w.player.hp() <= 0) {
      Score gamescore = new Score();
      gamescore.level = w.level();
      gamescore.gold = w.player.gold();
      if (w.level() > FINALLEVEL) {
        gamescore.state = "WON";
      } else {
        gamescore.state = "LOST";
      }
      w.player.inputStatistics(gamescore);
      return gamescore;
    }
    return null;
  }
}
