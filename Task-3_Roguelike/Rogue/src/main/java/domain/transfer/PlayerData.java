package domain.transfer;

import domain.Stats;
import java.util.ArrayList;

public class PlayerData {
  public Stats stats;
  public int y;
  public int x;

  public ArrayList<ItemData> backpack;
  public int gold;

  public ItemData weapon;
  public ArrayList<ItemData> effects;
  public boolean sleeps;

  public int steps = 0;
  public int treasures = 0;
  public int foods = 0;
  public int elixirs = 0;
  public int scrolls = 0;

  public int hitsLanded = 0;
  public int hitsFailed = 0;
  public int dodges = 0;
}
