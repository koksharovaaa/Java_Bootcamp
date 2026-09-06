package tictactoe.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {
  private final int field[][];

  public Request(@JsonProperty("field") int field[][]) {
    this.field = field;
  }

  public int[][] field() {
    return this.field;
  }
}
