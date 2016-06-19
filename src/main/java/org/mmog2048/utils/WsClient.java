package org.mmog2048.utils;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonObject;

import java.util.Random;
import java.util.UUID;

public class WsClient {
  private String directions[] = {"U", "D", "L", "R"};
  private final String[] token = new String[1];
  private boolean moveAvailable = true;
  private Vertx vertx;
  private Random random = new Random();
  private HttpClient client;

  public WsClient(Vertx vertx, String name) {
    this.vertx = vertx;
    HttpClientOptions options = new HttpClientOptions();
    options.setDefaultHost("localhost");
    options.setDefaultPort(8080);
    options.setKeepAlive(true);
    client = vertx.createHttpClient(options);
    // We use raw websocket transport
    client.websocket("/eventbus/websocket", (Handler<WebSocket>) websocket -> {
      // Register
      JsonObject body = new JsonObject().put("name", name);
      String replyAddress = UUID.randomUUID().toString();
      JsonObject
          msg = new JsonObject().put("type", "send").put("address", "org.mmog2048:register").
          put("replyAddress", replyAddress).put("body", body);
      websocket.writeFrame(io.vertx.core.http.WebSocketFrame.textFrame(msg.encode(), true));

      websocket.handler(buff -> {
        String msg1 = buff.toString();
        token[0] = new JsonObject(msg1).getJsonObject("body").getString("token");
        if (token[0] != null) {
          scheduleATurn();
        }
      });
    });
  }

  private void scheduleATurn() {
    vertx.setPeriodic(3000, new Handler<Long>() {
      @Override
      public void handle(Long timerID) {
        if (!moveAvailable) {
          vertx.cancelTimer(timerID);
        }
        makeAMove();
      }
    });
  }

  private void makeAMove() {
    client.websocket("/eventbus/websocket", (Handler<WebSocket>) websocket -> {
      String move = directions[random.nextInt(3)];
      JsonObject body = new JsonObject().put("move", move);
      String replyAddress = UUID.randomUUID().toString();
      JsonObject
          msg = new JsonObject().put("type", "send").put("address", "org.mmog2048:move:" + token[0]).
          put("replyAddress", replyAddress).put("body", body);
      websocket.writeFrame(io.vertx.core.http.WebSocketFrame.textFrame(msg.encode(), true));

      websocket.handler(buff -> {
        String msg1 = buff.toString();
      });
    });
  }
}
