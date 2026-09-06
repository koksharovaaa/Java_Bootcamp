package domain.enemies;

import com.googlecode.lanterna.TextColor;
import domain.Enemy;
import domain.Icon;
import domain.Player;
import domain.Stats;
import domain.World;
import domain.items.ItemSubtype;

public class Mimic extends Enemy {
  private static final String MIMIC_NAME = "Mimic";
  private static final Icon MIMIC_ICON = new Icon('M', TextColor.ANSI.BLUE);
  private static final Stats MIMIC_STATS = new Stats(22, 22, 5, 7);
  private static final ItemSubtype MASKS[] = ItemSubtype.values();

  private boolean revealed = false;
  private ItemSubtype mask;

  public Mimic() {
    super(MIMIC_NAME, MIMIC_ICON, MIMIC_STATS, 5);
    mask = MASKS[(int) (Math.random() * MASKS.length)];
  }

  @Override
  public Icon icon() {
    if (!revealed) {
      return mask.icon;
    }
    return this.icon;
  }

  public ItemSubtype maskItem() {
    return mask;
  }

  public void setMask(ItemSubtype mask) {
    this.mask = mask;
  }

  @Override
  protected void wander(World w) {}

  @Override
  protected void onProximity() {
    if (!revealed) {
      revealed = true;
    }
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
