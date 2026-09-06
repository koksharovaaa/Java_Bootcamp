package tictactoe.web.mapper;

import java.util.UUID;
import tictactoe.domain.model.Field;
import tictactoe.domain.model.Game;
import tictactoe.web.model.Request;
import tictactoe.web.model.Response;

public class WebMapper {
  public static Game toGame(UUID uuid, Request request) {
    return new Game(uuid, new Field(request.field()));
  }

  public static Response toResponse(Game game) {
    Response response = new Response(game.uuid(), game.exportField());
    return response;
  }
}
