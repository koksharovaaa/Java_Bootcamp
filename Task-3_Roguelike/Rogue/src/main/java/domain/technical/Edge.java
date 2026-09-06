package domain.technical;

import domain.Tile;

public class Edge {
  public final int r1;
  public final int r2;

  public Tile door;

  public Edge(int r1, int r2) {
    this.r1 = r1;
    this.r2 = r2;
  }
}
