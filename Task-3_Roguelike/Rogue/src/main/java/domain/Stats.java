package domain;

public class Stats {
  public int maxHp;
  public int hp;
  public int str;
  public int agi;

  public Stats(int hp, int maxHp, int str, int agi) {
    this.hp = hp;
    this.maxHp = maxHp;
    this.str = str;
    this.agi = agi;
  }

  public Stats(Stats stats) {
    this.hp = stats.hp;
    this.maxHp = stats.hp;
    this.str = stats.str;
    this.agi = stats.agi;
  }
}
