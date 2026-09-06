package tictactoe.web.controller;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tictactoe.domain.constants.Default;
import tictactoe.domain.model.Game;
import tictactoe.domain.service.Service;
import tictactoe.web.mapper.WebMapper;
import tictactoe.web.model.Request;
import tictactoe.web.model.Response;

@RestController
@RequestMapping("/game")
public class Controller {
  private final Service service;

  public Controller(Service service) {
    this.service = service;
  }

  @PostMapping("/{uuid}")
  public ResponseEntity<Response> playGame(@PathVariable UUID uuid, @RequestBody Request request) {
    Game game = WebMapper.toGame(uuid, request);
    String str = service.check(game);
    if (str != null && str.equals(Default.NOMOVE)) {
      Response response = WebMapper.toResponse(game);
      response.setMsg(Default.NOMOVE);
      return ResponseEntity.ok(response);
    }

    if (str != null) {
      Response response = new Response(uuid, str);
      return ResponseEntity.badRequest().body(response);
    }

    if (!game.isEmpty()) {
      service.takeTurn(game);
    }

    Response response = WebMapper.toResponse(game);
    int status = service.isGameOver(game);
    switch (status) {
      case Default.PLAYER -> {
        response.setMsg(Default.PLAYERWON);
      }
      case Default.COMPUTER -> {
        response.setMsg(Default.COMPUTERWON);
      }
      case Default.DRAW -> {
        response.setMsg(Default.NOONEWON);
      }
    }

    return ResponseEntity.ok(response);
  }
}
