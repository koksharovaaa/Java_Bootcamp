package domain.enemies;

import com.googlecode.lanterna.TextColor;
import domain.Enemy;
import domain.Icon;
import domain.Player;
import domain.Stats;
import domain.World;

public class SnakeMage extends Enemy {
  private static final String SNAKE_NAME = "Snake Mage";
  private static final Icon SNAKE_ICON = new Icon('S', TextColor.ANSI.WHITE_BRIGHT);
  private static final Stats SNAKE_STATS = new Stats(30, 30, 5, 10);

  private int dy = 1;
  private int dx = 1;

  public SnakeMage() {
    super(SNAKE_NAME, SNAKE_ICON, SNAKE_STATS, 20);
  }

  @Override
  protected void attack(Player p) {
    if (attackRoll(p)) {
      int dmg = damageRoll();
      p.modifyHp(-dmg);
      if ((int) (Math.random() * 10) == 0) {
        p.fallAsleep();
        actions.add(name + " cursed you!");
      }
      actions.add(name + " hit you! (" + dmg + ")");
    } else {
      p.addMissedAttack();
      actions.add(name + " missed you!");
    }
  }

  @Override
  protected void wander(World w) {
    checkDirection(w);
    move(w, dy, dx);
  }

  private void checkDirection(World w) {
    if (!w.isFree(y() + dy, x())) {
      dy *= -1;
    }

    if (!w.isFree(y(), x() + dx)) {
      dx *= -1;
    }
  }
}
