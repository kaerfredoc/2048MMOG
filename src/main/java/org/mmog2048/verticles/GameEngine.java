package org.mmog2048.verticles;

import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.mmog2048.dao.RedisDAO;

public class GameEngine {
  private static final Logger LOG = LoggerFactory.getLogger(GameEngine.class);

  /**
   - Get a token & move (U/D/L/R)
   - Pull from redis key=token
   - Turn value into json
   - Get board property
   - Run move
   - Store board back in redis w/timestamp - SET
   - Store score in top_scores as - ZADD
   - Return board as array - io.vertx.core.json.JsonArray
   * @param token
   * @param move
   * @param redisDAO
   * @return
   */
  public static JsonArray updateBoard(String token, String move, RedisDAO redisDAO) {
    return new JsonArray();
  }
}