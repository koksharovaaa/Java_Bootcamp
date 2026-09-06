package domain.transfer;

public class Score implements Comparable<Score> {
  public int level;
  public int gold;
  public String state;

  public int steps = 0;
  public int treasures = 0;
  public int foods = 0;
  public int elixirs = 0;
  public int scrolls = 0;

  public int hitsLanded = 0;
  public int hitsFailed = 0;
  public int dodges = 0;

  public String toRow() {
    return String.format(
        "%5d  %4d  %6s  %5d  %9d  %4d  %7d  %7d  %12d  %6d  %6d",
        level,
        gold,
        state,
        steps,
        treasures,
        foods,
        elixirs,
        scrolls,
        hitsLanded,
        hitsFailed,
        dodges);
  }

  @Override
  public int compareTo(Score s) {
    if (this.level == s.level) {
      return Integer.signum(this.gold - s.gold);
    }
    return Integer.signum(this.level - s.level);
  }
}
