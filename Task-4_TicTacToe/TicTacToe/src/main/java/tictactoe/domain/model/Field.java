package tictactoe.domain.model;

import java.util.Arrays;
import tictactoe.domain.constants.Default;

public class Field {
  private final int field[][];
  private boolean isEmpty = true;

  public Field(int cells[][]) {
    if (cells == null || cells.length != Default.SIZE || cells[0].length != Default.SIZE) {
      field = new int[Default.SIZE][Default.SIZE];
      for (int[] f : field) {
        for (int j = 0; j < field[0].length; j++) {
          f[j] = Default.FREE;
        }
      }
      return;
    }

    this.field = cells;
    for (int[] f : field) {
      for (int j = 0; j < field[0].length; j++) {
        if (f[j] != Default.FREE) {
          isEmpty = false;
          break;
        }
      }

      if (!isEmpty) {
        break;
      }
    }
  }

  public void set(int y, int x, int cell) {
    if (y < 0 || y >= field.length || x < 0 || x >= field[0].length) {
      return;
    }

    if (cell != Default.FREE && cell != Default.PLAYER && cell != Default.COMPUTER) {
      return;
    }

    this.field[y][x] = cell;
  }

  public int cell(int y, int x) {
    if (y < 0 || y >= field.length || x < 0 || x >= field[0].length) {
      return -1;
    }

    return this.field[y][x];
  }

  public int[][] exportField() {
    int copy[][] = new int[field.length][field[0].length];
    for (int i = 0; i < copy.length; i++) {
      copy[i] = Arrays.copyOf(field[i], field[i].length);
    }
    return copy;
  }

  public boolean isEmpty() {
    return this.isEmpty;
  }
}
