package domain;

public class Room {
  final int y1, x1, y2, x2;

  private boolean connected = false;
  private boolean start = false;

  public Room(int y1, int x1, int y2, int x2) {
    this.y1 = y1;
    this.x1 = x1;
    this.y2 = y2;
    this.x2 = x2;
  }

  public boolean connected() {
    return this.connected;
  }

  public boolean start() {
    return this.start;
  }

  public void connect() {
    this.connected = true;
  }

  public void makeStart() {
    this.start = true;
  }
}
