package domain.technical;

import domain.World;
import java.util.ArrayList;

public final class BresenhamLine {
  public final ArrayList<Point> line;
  public final boolean broken;

  public BresenhamLine(int y0, int x0, int y1, int x1, World w) {
    line = new ArrayList<>();
    if (!w.inField(y0, x0) || !w.inField(y1, x1)) {
      broken = true;
      return;
    }

    boolean k = Math.abs(y1 - y0) > Math.abs(x1 - x0);
    if (k) {
      int tmp = x0;
      x0 = y0;
      y0 = tmp;
      tmp = x1;
      x1 = y1;
      y1 = tmp;
    }

    if (x0 > x1) {
      int tmp = x0;
      x0 = x1;
      x1 = tmp;
      tmp = y0;
      y0 = y1;
      y1 = tmp;
    }

    int dx = x1 - x0, dy = Math.abs(y1 - y0);
    int yStep = (y0 < y1) ? 1 : -1, y = y0;
    int error = dx / 2;

    for (int x = x0; x <= x1; x++) {
      int py = k ? x : y, px = k ? y : x;
      line.add(new Point(py, px));
      if (x != x0 && x != x1 && !w.isFlat(py, px)) {
        broken = true;
        return;
      }
      error -= dy;
      if (error < 0) {
        y += yStep;
        error += dx;
      }
    }
    broken = false;
  }

  public Point nextStep(int y, int x) {
    if (line.size() <= 2) {
      return null;
    }

    Point point = new Point(y, x);
    if (point.equals(line.getFirst())) {
      return line.get(1);
    }

    if (point.equals(line.getLast())) {
      return line.get(line.size() - 2);
    }
    return null;
  }
}
