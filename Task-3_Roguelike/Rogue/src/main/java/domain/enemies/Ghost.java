package domain.enemies;

import com.googlecode.lanterna.TextColor;
import domain.Enemy;
import domain.Icon;
import domain.Player;
import domain.Stats;
import domain.World;

public class Ghost extends Enemy {
  private static final String GHOST_NAME = "Ghost";
  private static final Icon GHOST_ICON = new Icon('G', TextColor.ANSI.WHITE_BRIGHT);
  private static final Stats GHOST_STATS = new Stats(12, 12, 2, 7);

  private final int TPRANGE = 6;

  private boolean visible = true;

  public Ghost() {
    super(GHOST_NAME, GHOST_ICON, GHOST_STATS, 10);
  }

  @Override
  public boolean visible() {
    return visible;
  }

  public void setVisible(boolean value) {
    this.visible = value;
  }

  @Override
  protected void wander(World w) {
    int dy = 0, dx = 0;
    do {
      dx = (int) (Math.random() * TPRANGE - TPRANGE / 2);
      dy = (int) (Math.random() * TPRANGE - TPRANGE / 2);
    } while (!w.isFree(y() + dy, x() + dx));
    setPos(y() + dy, x() + dx);
    flicker();
  }

  private void flicker() {
    int r = (int) (Math.random() * 3);
    if (r == 0 ^ visible) {
      visible = !visible;
    }
  }

  @Override
  protected void attack(Player p) {
    visible = true;
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
