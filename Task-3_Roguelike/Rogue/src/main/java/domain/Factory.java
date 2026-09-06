package domain;

import domain.enemies.Ghost;
import domain.enemies.Mimic;
import domain.enemies.Ogre;
import domain.enemies.SnakeMage;
import domain.enemies.Vampire;
import domain.enemies.Zombie;
import domain.items.Item;
import domain.items.ItemSubtype;
import domain.items.ItemType;
import domain.technical.Edge;
import domain.technical.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class Factory {
  private static final int YLIMIT = 7;
  private static final int XLIMIT = 12;
  private static final int ROWS = 3;
  private static final int COLS = 3;
  public static final int LEVELS = 21;

  private static final ItemType TYPES[] = {
    ItemType.ELIXIR, ItemType.FOOD, ItemType.SCROLL, ItemType.WEAPON
  };

  private static final ItemSubtype FOODS[] = {
    ItemSubtype.FOOD_APPLE, ItemSubtype.FOOD_BREAD, ItemSubtype.FOOD_CAKE
  };

  private static final ItemSubtype ELIXIRS[] = {
    ItemSubtype.ELIXIR_HP, ItemSubtype.ELIXIR_STR, ItemSubtype.ELIXIR_AGI
  };

  private static final ItemSubtype SCROLLS[] = {
    ItemSubtype.SCROLL_HP, ItemSubtype.SCROLL_STR, ItemSubtype.SCROLL_AGI
  };

  private static final ItemSubtype WEAPONS[] = {
    ItemSubtype.WEAPON_DAGGER, ItemSubtype.WEAPON_SWORD, ItemSubtype.WEAPON_MACE
  };

  private static final Item KEYS[] = {
    new Item(ItemType.KEY, ItemSubtype.KEY_RED),
    new Item(ItemType.KEY, ItemSubtype.KEY_YELLOW),
    new Item(ItemType.KEY, ItemSubtype.KEY_GREEN),
    new Item(ItemType.KEY, ItemSubtype.KEY_CYAN),
    new Item(ItemType.KEY, ItemSubtype.KEY_BLUE)
  };

  private static final Tile DOORS[] = {
    Tile.DOOR_RED, Tile.DOOR_YELLOW, Tile.DOOR_GREEN, Tile.DOOR_CYAN, Tile.DOOR_BLUE
  };

  private static final List<Supplier<Enemy>> ENEMY_SUP =
      List.of(Zombie::new, Vampire::new, Ogre::new, Ghost::new, SnakeMage::new, Mimic::new);

  public static void createLayout(World w) {
    if (w == null) {
      return;
    }

    if (w.h() / ROWS < YLIMIT || w.w() / COLS < XLIMIT) {
      return;
    }

    cleanLayout(w);
    w.rooms.clear();

    determineDifficulty(w);
    makeRooms(w);
    makeCorridors(w);
    placeEnemies(w);
    placeItems(w);
    placeTile(w, Tile.EXIT);
    placePlayer(w);
  }

  private static void determineDifficulty(World w) {
    if (w.level() == 1 || w.player.hp() <= w.player.maxHp() / 3) {
      w.diff = GameDifficulty.EASY;
    } else if (w.player.hp() > w.player.maxHp() * 2 / 3) {
      w.diff = GameDifficulty.HARD;
    } else {
      w.diff = GameDifficulty.MEDIUM;
    }
  }

  private static void cleanLayout(World w) {
    for (int i = 0; i < w.h(); i++) {
      for (int j = 0; j < w.w(); j++) {
        w.tiles[i][j] = Tile.VOID;
        w.visited[i][j] = false;
        w.items[i][j] = null;
      }
    }
    w.enemies.clear();
  }

  private static void makeRooms(World w) {
    int stepY = w.h() / ROWS, stepX = w.w() / COLS;
    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        int x1 = (int) (Math.random() * (stepX - XLIMIT - 2)) + j * stepX + 1;
        int x2 = x1 + XLIMIT + (int) (Math.random() * (stepX - x1 + j * stepX - XLIMIT - 1));
        int y1 = (int) (Math.random() * (stepY - YLIMIT - 2)) + i * stepY + 1;
        int y2 = y1 + YLIMIT + (int) (Math.random() * (stepY - y1 + i * stepY - YLIMIT - 1));
        w.rooms.add(new Room(y1, x1, y2, x2));
      }
    }
    int random = (int) (Math.random() * w.rooms.size());
    w.rooms.get(random).makeStart();
    for (Room r : w.rooms) {
      for (int i = r.y1; i <= r.y2; i++) {
        w.tiles[i][r.x1] = Tile.WALL;
        w.tiles[i][r.x2] = Tile.WALL;
      }
      for (int j = r.x1; j <= r.x2; j++) {
        w.tiles[r.y1][j] = Tile.WALL;
        w.tiles[r.y2][j] = Tile.WALL;
      }
      for (int i = r.y1 + 1; i < r.y2; i++) {
        for (int j = r.x1 + 1; j < r.x2; j++) {
          w.tiles[i][j] = Tile.FLOOR;
        }
      }
    }
  }

  private static void makeCorridors(World w) {
    int id = 0;
    int set[] = new int[w.rooms.size()];
    ArrayList<Edge> allCorridors = new ArrayList<>();
    for (int i = 0; i < w.rooms.size(); i++) {
      set[i] = i;
      if (i / ROWS == (i + 1) / ROWS && i + 1 < w.rooms.size()) {
        allCorridors.add(new Edge(i, i + 1));
        id++;
      }
      if (i + COLS < w.rooms.size()) {
        allCorridors.add(new Edge(i, i + COLS));
        id++;
      }
    }

    Collections.shuffle(allCorridors);
    ArrayList<Edge> corridors = new ArrayList<>();
    for (Edge c : allCorridors) {
      if (leader(set, c.r1) != leader(set, c.r2)) {
        unite(set, c.r1, c.r2);
        corridors.add(c);
      }
    }

    placeDoors(w, corridors);
    for (Edge c : corridors) {
      drawCorridor(w, c);
    }
  }

  private static int leader(int[] set, int value) {
    if (set[value] == value) {
      return value;
    }
    return leader(set, set[value]);
  }

  private static void unite(int set[], int a, int b) {
    a = leader(set, a);
    b = leader(set, b);
    set[b] = a;
  }

  private static void drawCorridor(World w, Edge c) {
    Room r1 = w.rooms.get(c.r1), r2 = w.rooms.get(c.r2);
    int y1 = -1, x1 = -1, y2 = -1, x2 = -1;
    if (c.r1 + 1 == c.r2) {
      x1 = r1.x2;
      y1 = (int) (Math.random() * (r1.y2 - r1.y1 - 1)) + r1.y1 + 1;
      x2 = r2.x1;
      y2 = (int) (Math.random() * (r2.y2 - r2.y1 - 1)) + r2.y1 + 1;
      int xTurn = (int) (Math.random() * (x2 - x1 - 1)) + x1 + 1;
      for (int i = x1; i <= xTurn; i++) {
        w.tiles[y1][i] = Tile.CORRIDOR;
      }
      int stepY = (y1 < y2 ? 1 : -1);
      for (int i = y1; i != y2; i += stepY) {
        w.tiles[i][xTurn] = Tile.CORRIDOR;
      }
      for (int i = xTurn; i <= x2; i++) {
        w.tiles[y2][i] = Tile.CORRIDOR;
      }
    } else if (c.r1 + ROWS == c.r2) {
      x1 = (int) (Math.random() * (r1.x2 - r1.x1 - 1)) + r1.x1 + 1;
      y1 = r1.y2;
      x2 = (int) (Math.random() * (r2.x2 - r2.x1 - 1)) + r2.x1 + 1;
      y2 = r2.y1;
      int yTurn = (int) (Math.random() * (y2 - y1 - 1)) + y1 + 1;
      for (int i = y1; i <= yTurn; i++) {
        w.tiles[i][x1] = Tile.CORRIDOR;
      }
      int stepX = (x1 < x2 ? 1 : -1);
      for (int i = x1; i != x2; i += stepX) {
        w.tiles[yTurn][i] = Tile.CORRIDOR;
      }
      for (int i = yTurn; i <= y2; i++) {
        w.tiles[i][x2] = Tile.CORRIDOR;
      }
    }

    if (c.door != null && y1 != -1) {
      int k = (int) (Math.random() * 2);
      w.tiles[k == 0 ? y1 : y2][k == 0 ? x1 : x2] = c.door;
    }
  }

  private static void placeDoors(World w, ArrayList<Edge> c) {
    HashMap<Integer, Item> keys = new HashMap<>();
    Graph graph = new Graph(w.rooms.size());
    graph.fillGraph(c);

    int start = 0;
    for (; start < w.rooms.size(); start++) {
      if (w.rooms.get(start).start()) {
        break;
      }
    }

    for (Tile d : DOORS) {
      int id;
      do {
        id = (int) (Math.random() * c.size());
      } while (c.get(id).door != null || !validCorridor(c, w.rooms, id));
      c.get(id).door = d;

      int dst = (int) (Math.random() * 2) == 0 ? c.get(id).r1 : c.get(id).r2;
      if (c.get(id).r1 == start) {
        dst = c.get(id).r2;
      } else if (c.get(id).r2 == start) {
        dst = c.get(id).r1;
      }

      int keyRoom = graph.keyRoom(start, dst, c, keys);
      if (keyRoom != -1) {
        Item k = key(c.get(id));
        keys.put(keyRoom, k);
        placeInRoom(k, keyRoom, w);
      } else {
        // c.get(id).door = null;
      }
    }
  }

  private static Item key(Edge c) {
    if (c == null || c.door == null) {
      return null;
    }

    for (Item k : KEYS) {
      if (k.icon().colour == c.door.icon().colour) {
        return k;
      }
    }
    return null;
  }

  private static boolean validCorridor(ArrayList<Edge> c, ArrayList<Room> r, int id) {
    Edge corridor = c.get(id);
    return !r.get(corridor.r1).start() && !r.get(corridor.r2).start();
  }

  private static void placeTile(World w, Tile t) {
    Room r = null;
    do {
      r = w.rooms.get((int) (Math.random() * w.rooms.size()));
    } while (r.start());

    int y, x;
    do {
      y = (int) (Math.random() * (r.y2 - r.y1 - 1)) + r.y1 + 1;
      x = (int) (Math.random() * (r.x2 - r.x1 - 1)) + r.x1 + 1;
    } while (w.tiles[y][x] != Tile.FLOOR || w.items[y][x] != null);
    w.tiles[y][x] = t;
  }

  private static void placePlayer(World w) {
    int id = -1;
    for (int i = 0; i < w.rooms.size(); i++) {
      if (w.rooms.get(i).start()) {
        id = i;
        break;
      }
    }

    placeInRoom(w.player, id, w);
  }

  private static void placeEnemies(World w) {
    for (int i = 0; i < w.rooms.size(); i++) {
      if (w.rooms.get(i).start()) {
        continue;
      }

      int count = (int) (Math.random() * (w.diff.enemyCount + w.level() / 5)) + 1;
      for (int j = 0; j < count; j++) {
        Supplier<Enemy> s = ENEMY_SUP.get((int) (Math.random() * ENEMY_SUP.size()));
        Enemy e = s.get();
        e.adjust(w.level(), w.diff);
        placeInRoom(e, i, w);
        w.enemies.add(e);
      }
    }
  }

  private static void placeItems(World w) {
    for (int i = 0; i < w.rooms.size(); i++) {
      if (w.rooms.get(i).start()) {
        continue;
      }

      int count = (int) (Math.random() * (w.diff.foodCount + (LEVELS - w.level()) / LEVELS)) + 1;
      for (int j = 0; j < count; j++) {
        ItemSubtype subtype = FOODS[(int) (Math.random() * FOODS.length)];
        placeInRoom(new Item(ItemType.FOOD, subtype), i, w);
      }

      count = (int) (Math.random() * (w.diff.itemCount + (LEVELS - w.level()) / LEVELS));
      for (int j = 0; j < count; j++) {
        ItemType type = TYPES[(int) (Math.random() * TYPES.length)];
        ItemSubtype subtype = null;
        switch (type) {
          case ELIXIR -> {
            subtype = ELIXIRS[(int) (Math.random() * ELIXIRS.length)];
          }
          case FOOD -> {
            subtype = FOODS[(int) (Math.random() * FOODS.length)];
          }
          case SCROLL -> {
            subtype = SCROLLS[(int) (Math.random() * SCROLLS.length)];
          }
          case WEAPON -> {
            subtype = WEAPONS[(int) (Math.random() * WEAPONS.length)];
          }
        }
        placeInRoom(new Item(type, subtype), i, w);
      }
    }
  }

  private static void placeInRoom(Creature c, int id, World w) {
    if (id < 0 || id >= w.rooms.size()) {
      return;
    }

    int y, x;
    Room r = w.rooms.get(id);
    do {
      y = (int) (Math.random() * (r.y2 - r.y1 - 1)) + r.y1 + 1;
      x = (int) (Math.random() * (r.x2 - r.x1 - 1)) + r.x1 + 1;
    } while (!w.isFree(y, x) || w.tiles[y][x] == Tile.CORRIDOR || w.items[y][x] != null);
    c.setPos(y, x);
  }

  private static void placeInRoom(Item item, int id, World w) {
    if (id < 0 || id >= w.rooms.size() || item == null) {
      return;
    }

    int y, x;
    Room r = w.rooms.get(id);
    do {
      y = (int) (Math.random() * (r.y2 - r.y1 - 1)) + r.y1 + 1;
      x = (int) (Math.random() * (r.x2 - r.x1 - 1)) + r.x1 + 1;
    } while (!w.isFree(y, x) || w.tiles[y][x] == Tile.CORRIDOR || w.items[y][x] != null);

    w.items[y][x] = item;
  }
}
