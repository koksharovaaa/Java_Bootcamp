package domain;

import domain.items.Item;
import domain.items.ItemType;
import domain.transfer.ItemData;
import java.util.ArrayList;
import java.util.EnumMap;

public class Backpack {
  EnumMap<ItemType, Item[]> storage;

  public Backpack() {
    storage = new EnumMap<>(ItemType.class);
    for (ItemType itemType : ItemType.values()) {
      storage.put(itemType, new Item[9]);
    }
  }

  public boolean addItem(Item item) {
    Item[] items = storage.get(item.type);
    for (int i = 0; i < items.length; i++) {
      if (items[i] == null) {
        items[i] = item;
        return true;
      }
    }
    return false;
  }

  public void clearBackpack() {
    for (ItemType itemType : ItemType.values()) {
      Item[] items = storage.get(itemType);
      for (int i = 0; i < items.length; i++) {
        items[i] = null;
      }
    }
  }

  public void clearKeys() {
    Item[] keys = storage.get(ItemType.KEY);
    for (int i = 0; i < keys.length; i++) {
      keys[i] = null;
    }
  }

  public Item removeItem(ItemType type, int id) {
    Item[] items = storage.get(type);
    Item item = null;
    if (id >= 0 && id < items.length) {
      item = items[id];
      items[id] = null;
    }
    if (item != null) {
      for (int i = 0; i < items.length - 1; i++) {
        if (items[i] == null) {
          items[i] = items[i + 1];
          items[i + 1] = null;
        }
      }
    }
    return item;
  }

  public boolean hasKey(Tile door) {
    if (door != Tile.DOOR_RED
        && door != Tile.DOOR_YELLOW
        && door != Tile.DOOR_GREEN
        && door != Tile.DOOR_CYAN
        && door != Tile.DOOR_BLUE) {
      return false;
    }

    Item[] items = storage.get(ItemType.KEY);
    for (Item i : items) {
      if (i != null && i.icon().colour == door.icon().colour) {
        return true;
      }
    }
    return false;
  }

  public ArrayList<String> getItems(ItemType type) {
    Item[] items = storage.get(type);
    ArrayList<String> list = new ArrayList<>();
    if (items != null) {
      for (Item i : items) {
        if (i != null) {
          list.add(i.name);
        }
      }
    }
    return list;
  }

  public ArrayList<ItemData> exportBackpack() {
    ArrayList<ItemData> backpack = new ArrayList<>();

    for (ItemType type : ItemType.values()) {
      Item[] items = storage.get(type);
      for (Item i : items) {
        if (i != null) {
          ItemData id = i.exportItem();
          backpack.add(id);
        }
      }
    }
    return backpack;
  }
}
