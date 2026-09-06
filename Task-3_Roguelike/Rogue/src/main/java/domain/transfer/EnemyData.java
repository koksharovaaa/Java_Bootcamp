package domain.transfer;

import domain.Stats;
import domain.items.ItemSubtype;
import java.util.ArrayList;

public class EnemyData {
  public String name;
  public ItemSubtype mimicMask;
  public Stats stats;

  public int y;
  public int x;

  public boolean visible;
  public boolean firstDefence;
  public ArrayList<ItemData> treasures;
}
