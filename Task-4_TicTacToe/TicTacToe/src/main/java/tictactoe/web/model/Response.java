package tictactoe.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class Response {
  private final UUID uuid;
  private final int field[][];
  private String msg;

  public Response(UUID uuid, int field[][]) {
    this.uuid = uuid;
    this.field = field;
    this.msg = null;
  }

  public Response(UUID uuid, String msg) {
    this.uuid = uuid;
    this.field = null;
    this.msg = msg;
  }

  @JsonProperty
  public UUID uuid() {
    return this.uuid;
  }

  @JsonProperty
  public int[][] field() {
    return this.field;
  }

  @JsonProperty
  public String msg() {
    return this.msg;
  }

  public void setMsg(String str) {
    this.msg = str;
  }
}
