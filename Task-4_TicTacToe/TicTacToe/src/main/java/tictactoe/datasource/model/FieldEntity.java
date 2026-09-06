package tictactoe.datasource.model;

import java.util.Arrays;

public class FieldEntity {
  public static final int SIZE = 3;
  public static final int EMPTY = 0;

  private final int field[][];

  public FieldEntity() {
    this.field = new int[SIZE][SIZE];
    for (int[] f : field) {
      for (int j = 0; j < field[0].length; j++) {
        f[j] = EMPTY;
      }
    }
  }

  public FieldEntity(int cells[][]) {
    this.field = cells;
  }

  public int[][] exportField() {
    int copy[][] = new int[field.length][field[0].length];
    for (int i = 0; i < copy.length; i++) {
      copy[i] = Arrays.copyOf(field[i], field[i].length);
    }
    return copy;
  }
}
