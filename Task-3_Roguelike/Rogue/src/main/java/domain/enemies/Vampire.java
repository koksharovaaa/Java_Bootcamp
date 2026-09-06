package domain.enemies;

import com.googlecode.lanterna.TextColor;
import domain.Enemy;
import domain.Icon;
import domain.Player;
import domain.Stats;
import domain.World;

public class Vampire extends Enemy {
  private static final String VAMPIRE_NAME = "Vampire";
  private static final Icon VAMPIRE_ICON = new Icon('V', TextColor.ANSI.RED);
  private static final Stats VAMPIRE_STATS = new Stats(25, 25, 5, 7);

  private int steps = 0;
  private final int pathLength = 8;
  private int dx = 0;
  private int dy = 0;

  private boolean firstDefence = true;

  public Vampire() {
    super(VAMPIRE_NAME, VAMPIRE_ICON, VAMPIRE_STATS, 15);
  }

  public boolean firstDefence() {
    return this.firstDefence;
  }

  public void setFirstDefence(boolean value) {
    this.firstDefence = value;
  }

  @Override
  protected void wander(World w) {
    if (steps <= 0) {
      steps = (int) (Math.random() * pathLength);
      switch ((int) (Math.random() * 2)) {
        case 0 -> {
          dx = ((int) (Math.random() * 2) == 0) ? -1 : 1;
          dy = 0;
        }
        case 1 -> {
          dy = ((int) (Math.random() * 2) == 0) ? -1 : 1;
          dx = 0;
        }
      }
    }
    move(w, dx, dy);
    steps--;
  }

  @Override
  protected void attack(Player p) {
    if (attackRoll(p)) {
      int dmg = damageRoll();
      p.modifyHp(-dmg);
      p.modifyMaxHp(-dmg / 5);
      actions.add(name + " hit you! (" + dmg + ")");
      if (Math.random() * 5 == 0) {
        actions.add(name + " bit you! (" + dmg / 5 + ")");
      }
    } else {
      p.addMissedAttack();
      actions.add(name + " missed you!");
    }
  }

  @Override
  public boolean isHittable() {
    if (firstDefence) {
      firstDefence = false;
      return false;
    }
    return true;
  }
}
