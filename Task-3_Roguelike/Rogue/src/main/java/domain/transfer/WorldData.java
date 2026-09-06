package domain.transfer;

import domain.GameDifficulty;
import domain.Room;
import java.util.ArrayList;

public class WorldData {
  public int h;
  public int w;
  public int level;
  public GameDifficulty diff = GameDifficulty.EASY;

  public ArrayList<String> log; // for messages - ?

  public ArrayList<Room> rooms;

  public int tileId[][];
  public ArrayList<ItemData> items;
  public boolean visited[][];

  public ArrayList<EnemyData> enemies;
  public PlayerData player;
}
