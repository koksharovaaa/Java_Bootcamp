package domain;

public enum GameDifficulty {
  EASY(3, 3, 2),
  MEDIUM(6, 3, 2),
  HARD(10, 2, 1);

  public final int enemyCount;
  public final int foodCount;
  public final int itemCount;

  private GameDifficulty(int enemyCount, int itemCount, int foodCount) {
    this.enemyCount = enemyCount;
    this.itemCount = itemCount;
    this.foodCount = foodCount;
  }
}
