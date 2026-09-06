package domain.transfer;

import domain.items.ItemSubtype;
import domain.items.ItemType;

public class ItemData {
  public ItemType type;
  public ItemSubtype subtype;
  public int duration;

  public int y = -1;
  public int x = -1;

  public void setPos(int y, int x) {
    this.y = y;
    this.x = x;
  }
}
