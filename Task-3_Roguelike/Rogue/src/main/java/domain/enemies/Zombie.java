package domain.enemies;

import com.googlecode.lanterna.TextColor;
import domain.Enemy;
import domain.Icon;
import domain.Player;
import domain.Stats;
import domain.World;

public class Zombie extends Enemy {
  private static final String ZOMBIE_NAME = "Zombie";
  private static final Stats ZOMBIE_STATS = new Stats(20, 20, 5, 3);
  private static final Icon ZOMBIE_ICON = new Icon('Z', TextColor.ANSI.GREEN);

  public Zombie() {
    super(ZOMBIE_NAME, ZOMBIE_ICON, ZOMBIE_STATS, 15);
  }

  @Override
  protected void wander(World w) {
    int dx = 0, dy = 0;
    switch ((int) (Math.random() * 2)) {
      case 0 -> {
        dx = ((int) (Math.random() * 2) == 0) ? -1 : 1;
      }
      case 1 -> {
        dy = ((int) (Math.random() * 2) == 0) ? -1 : 1;
      }
    }
    move(w, dx, dy);
  }

  @Override
  protected void attack(Player p) {
    if (attackRoll(p)) {
      int dmg = damageRoll();
      p.modifyHp(-dmg);
      actions.add(name + " hit you! (" + dmg + ")");
    } else {
      p.addMissedAttack();
      actions.add(name + " missed you!");
    }
  }
}
