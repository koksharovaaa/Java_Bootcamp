package domain;

import com.googlecode.lanterna.TextColor;
import domain.items.Item;
import domain.items.ItemType;
import domain.technical.BresenhamLine;
import domain.transfer.ItemData;
import domain.transfer.PlayerData;
import domain.transfer.Score;
import java.util.ArrayList;
import java.util.Iterator;

public class Player extends Creature {
  private static final Icon PLAYER_ICON = new Icon('@', TextColor.ANSI.YELLOW_BRIGHT);
  private static final Stats PLAYER_STATS = new Stats(20, 20, 5, 6);

  public boolean seen[][];
  public final int sightY = 6;
  public final int sightX = sightY * 2;

  public Backpack backpack;
  public Item weapon = null;
  private int gold = 0;

  public ArrayList<Item> effects;
  private boolean sleeps = false;

  private int steps = 0;
  private int treasures = 0;
  private int foods = 0;
  private int elixirs = 0;
  private int scrolls = 0;

  private int hitsLanded = 0;
  private int hitsFailed = 0;
  private int dodges = 0;

  private Enemy lastAttacked = null;

  public Player() {
    super(PLAYER_ICON, PLAYER_STATS);
    backpack = new Backpack();
    effects = new ArrayList<>();
    seen = new boolean[sightY * 2 + 1][sightX * 2 + 1];
    for (int i = 0; i < sightY; i++) {
      for (int j = 0; j < sightX; j++) {
        seen[i][j] = false;
      }
    }
  }

  @Override
  public int hp() {
    return baseHp() + bonusHp();
  }

  @Override
  public int maxHp() {
    return baseMaxHp() + bonusHp();
  }

  @Override
  public int str() {
    return baseStr() + bonusStr() + weaponStr();
  }

  @Override
  public int agi() {
    return baseAgi() + bonusAgi();
  }

  public int baseHp() {
    return this.stats.hp;
  }

  public int baseMaxHp() {
    return this.stats.maxHp;
  }

  public int baseStr() {
    return this.stats.str;
  }

  public int baseAgi() {
    return this.stats.agi;
  }

  public int bonusHp() {
    int result = 0;
    if (effects != null) {
      for (Item e : effects) {
        result += e.hp();
      }
    }
    return result;
  }

  public int bonusStr() {
    int result = 0;
    if (effects != null) {
      for (Item e : effects) {
        result += e.str();
      }
    }
    return result;
  }

  public int bonusAgi() {
    int result = 0;
    if (effects != null) {
      for (Item e : effects) {
        result += e.agi();
      }
    }
    return result;
  }

  public int gold() {
    return this.gold;
  }

  public String weapon() {
    if (weapon != null) {
      return this.weapon.name;
    }
    return null;
  }

  public int weaponStr() {
    if (weapon != null) {
      return weapon.str();
    }
    return 0;
  }

  public int weaponDuration() {
    if (weapon != null) {
      return this.weapon.duration();
    }
    return 0;
  }

  private void addStats(Item item) {
    modifyHp(item.hp());
    modifyMaxHp(item.maxHp());
    modifyStr(item.str());
    modifyAgi(item.agi());

    if (hp() > maxHp()) {
      modifyHp(maxHp() - hp());
    }
  }

  public Enemy lastAttacked() {
    return this.lastAttacked;
  }

  public void fallAsleep() {
    this.sleeps = true;
  }

  public void addMissedAttack() {
    dodges++;
  }

  public void attack(Enemy e) {
    if (sleeps) {
      sleeps = false;
      actions.add("You were asleep!");
      return;
    }

    if (attackRoll(e)) {
      int dmg = damageRoll();
      e.modifyHp(-dmg);
      actions.add("You hit " + e.name + "! (" + dmg + ")");
      hitsLanded++;
    } else {
      actions.add("You missed " + e.name);
      hitsFailed++;
    }

    if (weapon != null) {
      weapon.reduceDuration();
      if (weapon.duration() < 1) {
        weapon = null;
        actions.add("Your weapon broke!");
      }
    }
  }

  @Override
  public void move(World w, int dy, int dx) {
    if (w.isFree(y() + dy, x())
        || w.tiles[y() + dy][x()] == Tile.EXIT
        || w.items[y() + dy][x()] != null
        || w.isDoor(y() + dy, x()) && backpack.hasKey(w.tiles[y() + dy][x()])) {
      setPos(y() + dy, x());
    }

    if (w.isFree(y(), x() + dx)
        || w.tiles[y()][x() + dx] == Tile.EXIT
        || w.items[y()][x() + dx] != null
        || w.isDoor(y(), x() + dx) && backpack.hasKey(w.tiles[y()][x() + dx])) {
      if (w.isDoor(y(), x() + dx)) {
        w.tiles[y() + dy][x()] = Tile.CORRIDOR;
      }
      setPos(y(), x() + dx);
    }

    if (w.isDoor(y(), x())) {
      w.tiles[y()][x()] = Tile.CORRIDOR;
    }

    steps++;
  }

  private void takeItem(World w) {
    Item i = w.items[y()][x()];
    if (i == null) {
      return;
    }

    w.items[y()][x()] = null;
    switch (i.type) {
      case ELIXIR -> {
        actions.add("You got an elixir.");
      }
      case FOOD -> {
        actions.add("You got some food.");
      }
      case SCROLL -> {
        actions.add("You got a scroll.");
      }
      case TREASURE -> {
        actions.add("You got a treasure.");
      }
      case WEAPON -> {
        actions.add("You got a weapon.");
      }
      case KEY -> {
        actions.add("You got a key.");
      }
    }

    if (i.type == ItemType.TREASURE) {
      gold += i.cost();
      treasures++;
      return;
    }

    backpack.addItem(i);
  }

  public void useItem(ItemType type, char c, World w) {
    if (type == ItemType.WEAPON) {
      if (c == '0') {
        if (backpack.addItem(weapon)) {
          weapon = null;
        } else if (dropItem(w, weapon)) {
          weapon = null;
        }
      } else {
        Item i = backpack.removeItem(type, c - '1');
        if (i == null) {
          return;
        }

        if (weapon == null) {
          weapon = i;
          actions.add("You equipped a " + weapon.name + ".");
          return;
        }

        if (dropItem(w, weapon)) {
          weapon = i;
        } else if (backpack.addItem(weapon)) {
          weapon = i;
        }
        actions.add("You equipped a " + weapon.name + ".");
      }
      return;
    }

    Item item = backpack.removeItem(type, c - '1');
    if (item != null) {
      if (item.type == ItemType.ELIXIR) {
        effects.add(item);
      } else {
        addStats(item);
      }

      switch (item.type) {
        case ELIXIR -> {
          elixirs++;
          actions.add("You used an elixir.");
        }
        case FOOD -> {
          foods++;
          actions.add("You ate food.");
        }
        case SCROLL -> {
          scrolls++;
          actions.add("You used a scroll.");
        }
      }
    }
  }

  private void reduceEffects() {
    Iterator<Item> it = effects.iterator();
    while (it.hasNext()) {
      Item e = it.next();
      e.reduceDuration();
      if (e.duration() <= 0) {
        it.remove();
      }
    }
    checkHealth();
  }

  public void updateSeen(World w) {
    for (int i = 0; i < sightY * 2 + 1; i++) {
      for (int j = 0; j < sightX * 2 + 1; j++) {
        int py = i - sightY, px = (j - sightX) / 2;
        seen[i][j] = (py * py + px * px <= sightY * sightY);
        if (seen[i][j]) {
          py = y() + i - sightY;
          px = x() + j - sightX;
          BresenhamLine line = new BresenhamLine(y(), x(), py, px, w);
          seen[i][j] = !line.broken;
        }
      }
    }
  }

  public boolean sees(World w, int y, int x) {
    if (!w.inField(y, x)) {
      return false;
    }

    for (Room r : w.rooms) {
      if (r.y1 <= y()
          && y() <= r.y2
          && r.x1 <= x()
          && x() <= r.x2
          && r.y1 <= y
          && y <= r.y2
          && r.x1 <= x
          && x <= r.x2) {
        if (w.isStructural(y, x)) {
          w.visited[y][x] = true;
        }
        return true;
      }
    }

    int i = y - y() + sightY;
    int j = x - x() + sightX;
    if (i >= 0 && i < sightY * 2 + 1 && j >= 0 && j < sightX * 2 + 1) {
      if (seen[i][j] && w.isStructural(y, x)) {
        w.visited[y][x] = true;
      }
      return seen[i][j];
    }

    return false;
  }

  public void dropKeys() {
    backpack.clearKeys();
  }

  public void takeTurn(World w, char c) {
    int dx = 0, dy = 0;
    switch (c) {
      case 'w' -> {
        dy = -1;
      }
      case 'a' -> {
        dx = -1;
      }
      case 's' -> {
        dy = 1;
      }
      case 'd' -> {
        dx = 1;
      }
      default -> {
        return;
      }
    }

    Enemy e = w.enemyAt(y() + dy, x() + dx);
    if (e != null) {
      attack(e);
      lastAttacked = e;
    } else {
      move(w, dy, dx);
      takeItem(w);
      lastAttacked = null;
    }

    if (lastAttacked != null && lastAttacked.hp() < 1) {
      actions.add("You killed " + lastAttacked.name);
      lastAttacked = null;
    }
    reduceEffects();
    updateSeen(w);
  }

  public PlayerData exportPlayer() {
    PlayerData playerdata = new PlayerData();
    playerdata.stats = new Stats(baseHp(), baseMaxHp(), baseStr(), baseAgi());
    playerdata.y = y();
    playerdata.x = x();
    playerdata.backpack = backpack.exportBackpack();

    playerdata.effects = new ArrayList<>();
    for (Item e : effects) {
      if (e != null) {
        playerdata.effects.add(e.exportItem());
      }
    }

    if (weapon != null) {
      playerdata.weapon = weapon.exportItem();
    } else {
      playerdata.weapon = null;
    }

    playerdata.gold = gold;
    playerdata.sleeps = sleeps;

    playerdata.steps = steps;
    playerdata.treasures = treasures;
    playerdata.foods = foods;
    playerdata.elixirs = elixirs;
    playerdata.scrolls = scrolls;

    playerdata.hitsLanded = hitsLanded;
    playerdata.hitsFailed = hitsFailed;
    playerdata.dodges = dodges;
    return playerdata;
  }

  public static Player importPlayer(PlayerData playerdata) {
    Player p = new Player();

    p.setPos(playerdata.y, playerdata.x);
    p.setStats(playerdata.stats);

    p.gold = playerdata.gold;
    p.backpack = new Backpack();
    for (ItemData i : playerdata.backpack) {
      if (i != null) {
        p.backpack.addItem(Item.importItem(i));
      }
    }
    if (playerdata.weapon != null) {
      p.weapon = Item.importItem(playerdata.weapon);
    }

    for (ItemData e : playerdata.effects) {
      p.effects.add(Item.importItem(e));
    }
    p.sleeps = playerdata.sleeps;

    p.steps = playerdata.steps;
    p.treasures = playerdata.treasures;
    p.foods = playerdata.foods;
    p.elixirs = playerdata.elixirs;
    p.scrolls = playerdata.scrolls;

    p.hitsLanded = playerdata.hitsLanded;
    p.hitsFailed = playerdata.hitsFailed;
    p.dodges = playerdata.dodges;
    return p;
  }

  public void clearPlayer() {
    setStats(PLAYER_STATS);
    backpack.clearBackpack();
    weapon = null;
    gold = 0;
    effects.clear();
    sleeps = false;
    steps = 0;
    treasures = 0;
    foods = 0;
    elixirs = 0;
    scrolls = 0;
    hitsLanded = 0;
    hitsFailed = 0;
    dodges = 0;
    lastAttacked = null;
  }

  public void inputStatistics(Score score) {
    score.steps = steps;
    score.treasures = treasures;
    score.foods = foods;
    score.elixirs = elixirs;
    score.scrolls = scrolls;

    score.hitsLanded = hitsLanded;
    score.hitsFailed = hitsFailed;
    score.dodges = dodges;
  }
}
