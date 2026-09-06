package domain.items;

import domain.Icon;
import domain.Stats;
import domain.transfer.ItemData;

public class Item {
  public final ItemType type;
  public final ItemSubtype subtype;
  private final Icon icon;
  public final String name;

  private final Stats bonus;
  private int duration;
  private final boolean temporary;

  private int cost;

  public Item(ItemType type, ItemSubtype subtype) {
    this.type = type;
    this.duration = type.duration;
    this.temporary = type.temporary;

    this.subtype = subtype;
    this.name = subtype.name;
    this.cost = subtype.cost;
    this.icon = subtype.icon;
    this.bonus = subtype.bonus;
  }

  public Item(ItemType type, ItemSubtype subtype, Stats bonus) {
    this.type = type;
    this.duration = type.duration;
    this.temporary = type.temporary;

    this.subtype = subtype;
    this.name = subtype.name;
    this.cost = subtype.cost;
    this.icon = subtype.icon;
    this.bonus = bonus;
  }

  public Icon icon() {
    return this.icon;
  }

  public int hp() {
    return this.bonus.hp;
  }

  public int maxHp() {
    return this.bonus.maxHp;
  }

  public int str() {
    return this.bonus.str;
  }

  public int agi() {
    return this.bonus.agi;
  }

  public int duration() {
    return this.duration;
  }

  public int cost() {
    return this.cost;
  }

  public void reduceDuration() {
    if (temporary) {
      this.duration--;
    }
  }

  public void modifyCost(int value) {
    this.cost += value * 5;
  }

  public ItemData exportItem() {
    ItemData itemdata = new ItemData();
    itemdata.type = type;
    itemdata.subtype = subtype;
    itemdata.duration = duration();
    return itemdata;
  }

  public static Item importItem(ItemData itemdata) {
    Item i = new Item(itemdata.type, itemdata.subtype);
    i.duration = itemdata.duration;
    return i;
  }
}
