package domain.enemies;

import com.googlecode.lanterna.TextColor;
import domain.Enemy;
import domain.Icon;
import domain.Player;
import domain.Stats;
import domain.World;

public class Ogre extends Enemy {
  private static final String OGRE_NAME = "Ogre";
  private static final Icon OGRE_ICON = new Icon('O', TextColor.ANSI.YELLOW);
  private static final Stats OGRE_STATS = new Stats(30, 30, 10, 5); // fix

  private boolean skipsTurn = false;

  public Ogre() {
    super(OGRE_NAME, OGRE_ICON, OGRE_STATS, 15);
  }

  @Override
  protected void wander(World w) {
    int dy = 0, dx = 0;
    switch ((int) (Math.random() * 2)) {
      case 0 -> {
        dx = ((int) (Math.random() * 2) == 0) ? -1 : 1;
      }
      case 1 -> {
        dy = ((int) (Math.random() * 2) == 0) ? -1 : 1;
      }
    }
    for (int i = 0; i < 2; i++) {
      move(w, dx, dy);
    }
  }

  @Override
  protected void attack(Player p) {
    if (skipsTurn) {
      skipsTurn = false;
    } else {
      if (attackRoll(p)) {
        int dmg = damageRoll();
        p.modifyHp(-dmg);
        actions.add(name + " hit you! (" + dmg + ")");
      } else {
        skipsTurn = true;
        p.addMissedAttack();
        actions.add(name + " missed you!");
      }
    }
  }
}
