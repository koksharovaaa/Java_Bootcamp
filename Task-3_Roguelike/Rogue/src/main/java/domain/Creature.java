package domain;

import domain.items.Item;
import java.util.ArrayList;

public abstract class Creature {
  private int y = 0; // coordinates block?
  private int x = 0;

  protected final Stats stats;
  protected final Icon icon;

  protected ArrayList<String> actions;

  public Creature(Icon icon, Stats stats) {
    this.icon = icon;
    this.stats = new Stats(stats);
    actions = new ArrayList<>();
  }

  public int y() {
    return this.y;
  }

  public int x() {
    return this.x;
  }

  public Icon icon() {
    return this.icon;
  }

  public int hp() {
    return this.stats.hp;
  }

  public int maxHp() {
    return this.stats.maxHp;
  }

  public int str() {
    return this.stats.str;
  }

  public int agi() {
    return this.stats.agi;
  }

  public void modifyMaxHp(int value) {
    stats.maxHp += value;
  }

  public void modifyHp(int value) {
    stats.hp += value;
  }

  public void modifyStr(int value) {
    stats.str += value;
  }

  public void modifyAgi(int value) {
    stats.agi += value;
  }

  public void checkHealth() {
    if (stats.hp < 1) {
      this.stats.hp = 1;
    }
    if (stats.maxHp < 1) {
      this.stats.maxHp = 1;
    }
  }

  public boolean at(int y, int x) {
    return this.x == x && this.y == y;
  }

  protected void setPos(int y, int x) {
    if (y < 0 || x < 0) {
      return;
    }
    this.y = y;
    this.x = x;
  }

  protected void setStats(Stats stats) {
    this.stats.hp = stats.hp;
    this.stats.maxHp = stats.maxHp;
    this.stats.str = stats.str;
    this.stats.agi = stats.agi;
  }

  public boolean isNear(Creature c) {
    return (Math.abs(x - c.x()) == 1 && y == c.y() || Math.abs(y - c.y()) == 1 && x == c.x());
  }

  protected abstract void move(World w, int dy, int dx);

  public boolean isHittable() {
    return true;
  }

  public int distance(int y, int x) {
    return (int) Math.sqrt(Math.pow(y - y(), 2) + Math.pow(x - x(), 2));
  }

  protected boolean attackRoll(Creature c) {
    return (c.isHittable() && Math.random() * agi() > Math.random() * c.agi());
  }

  protected int damageRoll() {
    return (int) (Math.random() * str() + 1);
  }

  protected boolean dropItem(World w, Item item) {
    int px = x();
    int py = y();
    int size = 9;
    int direction = 0;
    int step = 1;
    for (int k = 0; k < Math.sqrt(size) * 2 - 1; k++) {
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < step; j++) {
          switch (direction) {
            case 0 -> {
              py -= 1;
            }
            case 1 -> {
              px += 1;
            }
            case 2 -> {
              py += 1;
            }
            case 3 -> {
              px -= 1;
            }
          }
          if (w.isFree(py, px) && w.items[py][px] == null) {
            w.items[py][px] = item;
            return true;
          }
        }
        direction = (direction + 1) % 4;
      }
      step++;
    }
    return false;
  }
}
