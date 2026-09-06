package domain.technical;

public class Point {
  public int y;
  public int x;

  public Point(int y, int x) {
    this.y = (y >= 0) ? y : 0;
    this.x = (x >= 0) ? x : 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj instanceof Point point) {
      return (this.y == point.y && this.x == point.x);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.y + this.x;
  }
}
