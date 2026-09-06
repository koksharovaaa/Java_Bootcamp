package domain;

import domain.items.Item;
import domain.transfer.EnemyData;
import domain.transfer.ItemData;
import domain.transfer.WorldData;
import java.util.ArrayList;

public class World {
  private final int w;
  private final int h;

  public ArrayList<Room> rooms;

  private int level = 1;
  public GameDifficulty diff = GameDifficulty.EASY;

  public Tile tiles[][];
  public Item items[][];
  public boolean visited[][];

  public ArrayList<Enemy> enemies;
  public Player player;

  public World() {
    this.h = Constants.WORLDH;
    this.w = Constants.WORLDW;

    tiles = new Tile[h][w];
    visited = new boolean[h][w];
    items = new Item[h][w];

    rooms = new ArrayList<>();
    enemies = new ArrayList<>();
    player = new Player();
  }

  public int h() {
    return this.h;
  }

  public int w() {
    return this.w;
  }

  public int level() {
    return this.level;
  }

  public void increaseLevel() {
    this.level++;
  }

  public boolean isFree(int y, int x) {
    return (isFlat(y, x)
        && tiles[y][x] != Tile.EXIT
        && !isDoor(y, x)
        && playerAt(y, x) == null
        && enemyAt(y, x) == null);
  }

  public boolean isDoor(int y, int x) {
    return tiles[y][x] == Tile.DOOR_RED
        || tiles[y][x] == Tile.DOOR_YELLOW
        || tiles[y][x] == Tile.DOOR_GREEN
        || tiles[y][x] == Tile.DOOR_CYAN
        || tiles[y][x] == Tile.DOOR_BLUE;
  }

  public boolean isFlat(int y, int x) {
    return (inField(y, x) && tiles[y][x] != Tile.VOID && tiles[y][x] != Tile.WALL && !isDoor(y, x));
  }

  public boolean isStructural(int y, int x) {
    return (inField(y, x) && (tiles[y][x] == Tile.WALL || tiles[y][x] == Tile.CORRIDOR));
  }

  public boolean inField(int y, int x) {
    return (y >= 0 && y < h && x >= 0 && x < w);
  }

  public Player playerAt(int y, int x) {
    if (player != null && player.y() == y && player.x() == x) {
      return player;
    }
    return null;
  }

  public Enemy enemyAt(int y, int x) {
    if (enemies != null) {
      for (Enemy e : enemies) {
        if (e.y() == y && e.x() == x) {
          return e;
        }
      }
    }

    return null;
  }

  public WorldData exportWorld() {
    WorldData worldData = new WorldData();
    worldData.h = h;
    worldData.w = w;
    worldData.level = level;
    worldData.diff = diff;
    worldData.rooms = new ArrayList<>();
    for (Room r : rooms) {
      worldData.rooms.add(new Room(r.y1, r.x1, r.y2, r.x2));
    }

    worldData.tileId = new int[h][w];
    worldData.visited = new boolean[h][w];
    worldData.items = new ArrayList<>();

    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        worldData.tileId[i][j] = tiles[i][j].id();
        worldData.visited[i][j] = visited[i][j];
        if (items[i][j] != null) {
          ItemData id = ((Item) items[i][j]).exportItem();
          id.setPos(i, j);
          worldData.items.add(id);
        }
      }
    }

    worldData.enemies = new ArrayList<>();
    for (Enemy e : enemies) {
      worldData.enemies.add(e.exportEnemy());
    }

    worldData.player = player.exportPlayer();
    return worldData;
  }

  public static World importWorld(WorldData worldData) {
    World world = new World();
    world.level = worldData.level;
    world.diff = worldData.diff;

    for (Room r : worldData.rooms) {
      world.rooms.add(new Room(r.y1, r.x1, r.y2, r.x2));
    }

    for (int i = 0; i < worldData.h; i++) {
      for (int j = 0; j < worldData.w; j++) {
        world.tiles[i][j] = Tile.fromId(worldData.tileId[i][j]);
        world.visited[i][j] = worldData.visited[i][j];
      }
    }

    for (ItemData i : worldData.items) {
      world.items[i.y][i.x] = Item.importItem(i);
    }

    for (EnemyData e : worldData.enemies) {
      world.enemies.add(Enemy.importEnemy(e));
    }

    world.player = Player.importPlayer(worldData.player);
    return world;
  }

  public void clearWorld() {
    level = 1;
    diff = GameDifficulty.EASY;
    player.clearPlayer();
  }
}
