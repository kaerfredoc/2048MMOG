package org.mmog2048.dao;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.redis.RedisClient;
import io.vertx.redis.op.RangeOptions;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;

public class RedisDAO {
  private static final Logger log = LoggerFactory.getLogger(RedisDAO.class);

  private RedisClient redis;

  public RedisDAO(RedisClient redis) {
    this.redis = redis;
  }

  public void getBoardInfo(final String contest, final String token, final Handler<JsonObject> handler) {
    String key = contest + "-" + token;
    redis.get(key, result -> {
      if (result.failed()) {
        log.error("no board found for " + token, result.cause());
        handler.handle(null);
      } else {
        String jsonAsString = result.result();
        if (StringUtils.isEmpty(jsonAsString)) {
          log.error("board found for " + token + " but the value is empty");
          handler.handle(null);
          return;
        }
        JsonObject jsonObject = new JsonObject(jsonAsString);
        handler.handle(jsonObject);
      }
    });
  }

  public void getTopScores(final String contest, final Handler<JsonArray> handler) {
    redis.zrevrange(contest, 0, 11, RangeOptions.NONE, topKeys -> {
      if (topKeys.failed()) {
        log.error("unabled to get top scores - " + contest, topKeys.cause());
        handler.handle(null);
      } else {
        if (topKeys.result().getList().size() > 0) {
          redis.mgetMany(topKeys.result().getList(), result -> {
            if (result.failed()) {
              log.error("no board found ", result.cause());
              handler.handle(null);
            } else {
              if (result.result().size() == 0) {
                log.error("board found but the value is empty");
                handler.handle(null);
                return;
              }
              JsonArray jsonArray = new JsonArray();
              result.result().forEach(o -> jsonArray.add(new JsonObject((String) o)));
              handler.handle(jsonArray);
            }
          });
        }
      };
    });
  }

  public void saveBoardInfo(final String contest, final String token, JsonObject boardInfo, Handler<JsonObject> handler) {
    String key = contest + "-" + token;
    getBoardInfo(contest, token, oldBoard -> {
      if (oldBoard != null) {
        boardInfo.put("name", oldBoard.getValue("name"));
      }
      boardInfo.put("lastUpdate", System.currentTimeMillis());
      JsonArray tiles = boardInfo.getJsonArray("tiles");
      int score = (int) Collections.max(tiles.getList());
      boardInfo.put("score", score);
      if (2048 == score) {
        boardInfo.put("complete", true);
      } else {
        boardInfo.put("complete", false);
      }
      redis.set(key, boardInfo.toString(), result -> {
        if (result.failed()) {
          log.error("unabled to save board for " + token, result.cause());
          handler.handle(null);
        } else {
          redis.zadd(contest, boardInfo.getInteger("score"), key, scoreResult -> {
            if (scoreResult.failed()) {
              log.error("unable to update score for contest: " + contest + " and token: " + token, scoreResult.cause());
              handler.handle(null);
            } else {
              handler.handle(boardInfo);
            }
          });
        }
      });
    });
  }
}
