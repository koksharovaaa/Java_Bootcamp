package domain;

import domain.enemies.Ghost;
import domain.enemies.Mimic;
import domain.enemies.Ogre;
import domain.enemies.SnakeMage;
import domain.enemies.Vampire;
import domain.enemies.Zombie;
import domain.items.Item;
import domain.items.ItemSubtype;
import domain.items.ItemType;
import domain.technical.BresenhamLine;
import domain.technical.Point;
import domain.transfer.EnemyData;
import domain.transfer.ItemData;
import java.util.ArrayList;

public abstract class Enemy extends Creature {
  public final String name;
  private final int agression;

  public final ArrayList<Item> treasures;

  public Enemy(String name, Icon icon, Stats stats, int agression) {
    super(icon, stats);
    this.agression = agression;
    this.name = name;
    treasures = new ArrayList<>();
  }

  public void adjust(int level, GameDifficulty diff) {
    ItemSubtype subtype = ItemSubtype.TREASURE_BAG;
    if (hp() <= 20) {
      subtype = ItemSubtype.TREASURE_POUCH;
    } else if (hp() >= 30) {
      subtype = ItemSubtype.TREASURE_CHEST;
      treasures.add(new Item(ItemType.TREASURE, ItemSubtype.TREASURE_POUCH));
    }
    treasures.add(new Item(ItemType.TREASURE, subtype));
    treasures.get(0).modifyCost(level - 1);

    int bonus = (level - 1);
    if (diff == GameDifficulty.MEDIUM) {
      bonus += 5;
      treasures.add(new Item(ItemType.TREASURE, ItemSubtype.TREASURE_POUCH));
    } else if (diff == GameDifficulty.HARD) {
      bonus += 10;
      treasures.add(new Item(ItemType.TREASURE, ItemSubtype.TREASURE_BAG));
    }

    modifyHp(bonus);
    modifyMaxHp(bonus);
    modifyAgi(bonus / 2);
    modifyStr(bonus / 2);
  }

  public boolean visible() {
    return true;
  }

  public ArrayList<String> takeTurn(World w) {
    if (hp() < 1) {
      for (Item i : treasures) {
        dropItem(w, i);
      }
      treasures.clear();
      return actions;
    }

    if (isNear(w.player)) {
      onProximity();
      attack(w.player);
    }

    if (chase(w)) {
      onProximity();
    } else {
      wander(w);
    }
    return actions;
  }

  protected void onProximity() {}

  protected abstract void attack(Player p);

  protected abstract void wander(World w);

  private boolean chase(World w) {
    if (distance(w.player.y(), w.player.x()) > agression) {
      return false;
    }

    BresenhamLine line = new BresenhamLine(y(), x(), w.player.y(), w.player.x(), w);
    if (line.broken) {
      return false;
    }

    Point p = line.nextStep(y(), x());
    if (p != null && w.isFree(p.y, p.x)) {
      setPos(p.y, p.x);
    }
    return true;
  }

  @Override
  protected void move(World w, int dy, int dx) {
    if (w.isFree(y() + dy, x())) {
      setPos(y() + dy, x());
    }
    if (w.isFree(y(), x() + dx)) {
      setPos(y(), x() + dx);
    }
  }

  public EnemyData exportEnemy() {
    EnemyData enemydata = new EnemyData();
    enemydata.name = name;
    enemydata.stats = new Stats(hp(), maxHp(), str(), agi());
    enemydata.y = y();
    enemydata.x = x();
    enemydata.visible = visible();
    enemydata.treasures = new ArrayList<>();

    for (Item t : this.treasures) {
      enemydata.treasures.add(t.exportItem());
    }

    if (this.name.equals("Mimic")) {
      Mimic m = (Mimic) this;
      enemydata.mimicMask = m.maskItem();
    } else {
      enemydata.mimicMask = null;
    }

    if (this.name.equals("Vampire")) {
      Vampire v = (Vampire) this;
      enemydata.firstDefence = v.firstDefence();
    } else {
      enemydata.firstDefence = false;
    }
    return enemydata;
  }

  public static Enemy importEnemy(EnemyData enemydata) {
    Enemy enemy;
    switch (enemydata.name) {
      case "Ghost" -> {
        enemy = new Ghost();
        Ghost g = (Ghost) enemy;
        g.setVisible(enemydata.visible);
      }
      case "Mimic" -> {
        enemy = new Mimic();
        Mimic m = (Mimic) enemy;
        m.setMask(enemydata.mimicMask);
      }
      case "Ogre" -> {
        enemy = new Ogre();
      }
      case "Snake Mage" -> {
        enemy = new SnakeMage();
      }
      case "Vampire" -> {
        enemy = new Vampire();
        Vampire v = (Vampire) enemy;
        v.setFirstDefence(enemydata.firstDefence);
      }
      case "Zombie" -> {
        enemy = new Zombie();
      }
      default -> {
        return null;
      }
    }
    enemy.setStats(enemydata.stats);
    enemy.setPos(enemydata.y, enemydata.x);
    for (ItemData i : enemydata.treasures) {
      enemy.treasures.add(Item.importItem(i));
    }
    return enemy;
  }
}
