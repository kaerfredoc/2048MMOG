package org.mmog2048.verticles;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.mmog2048.dao.RedisDAO;
import org.mmog2048.models.Tile;
import org.mmog2048.utils.GameMoveEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameEngine {
  private static final Logger LOG = LoggerFactory.getLogger(GameEngine.class);
  //TODO: contest needs to come from somewhere else
  public static final String contest = "thecontest";

  // https://github.com/bulenkov/2048/blob/master/src/com/bulenkov/game2048/Game2048.java


  /**
   * - Get a token & move (U/D/L/R)
   * - Pull from redis key=token
   * - Turn value into json
   * - Get board property
   * - Run move
   * - Store board back in redis w/timestamp - SET
   * - Store score in top_scores as - ZADD
   * - Return board as array - io.vertx.core.json.JsonArray
   *
   * @param token
   * @param move
   * @param redisDAO
   * @return
   */
  public static void updateGame(String token, String move, RedisDAO redisDAO, Handler<JsonObject> handler) {
    redisDAO.getBoardInfo(contest, token, boardInfo -> {
      if (boardInfo == null || boardInfo.isEmpty()) {
        return;
      }

      GameMoveEngine gme = new GameMoveEngine(getMyTiles(boardInfo).toArray(new Tile[0]));

      switch (move) {
        case "L":
          gme.left();
          break;
        case "U":
          gme.up();
          break;
        case "R":
          gme.right();
          break;
        case "D":
          gme.down();
          break;
        default:
          return;
      }

      JsonObject jsonObject = new JsonObject().put("tiles", convertTilesToInts(Arrays.asList(gme.myTiles)));
      redisDAO.saveBoardInfo(contest, token, jsonObject, result -> handler.handle(result));
    });
  }

  private static List<Tile> getMyTiles(JsonObject boardInfo) {
    JsonArray tiles = boardInfo.getJsonArray("tiles");
    List<Tile> tileList = new ArrayList<>(tiles.size());
    tiles.forEach(i -> tileList.add(new Tile((Integer) i)));
    return tileList;
  }

  private static JsonArray convertTilesToInts(List<Tile> tiles) {
    JsonArray entries = new JsonArray();
    tiles.forEach(tile -> entries.add(tile.getValue()));
    return entries;
  }
}