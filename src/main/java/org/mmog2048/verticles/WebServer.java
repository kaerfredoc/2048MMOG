package org.mmog2048.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.mmog2048.dao.RedisDAO;
import org.mmog2048.utils.RedisUtils;

import java.util.Date;
import java.util.UUID;

public class WebServer extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);

  @Override
  public void start(Future<Void> future) {
    try {
      JsonObject config = context.config();
      RedisOptions redisOptions = RedisUtils.createRedisOptions(config.getJsonObject("redis"));
      RedisClient redisClient = RedisClient.create(vertx, redisOptions);
      // Clear db on restart
      redisClient.flushall(event2 -> System.out.println(event2.result()));
      redisClient.info(event1 -> System.out.println(event1.result()));
      RedisDAO redisDAO = new RedisDAO(redisClient);

      EventBus eb = vertx.eventBus();
      vertx.setPeriodic(
          500, event -> {
            String now = DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date());
            System.out.println(now);

            redisDAO.getTopScores(GameEngine.contest, event1 -> {
              eb.publish("org.mmog2048:game-state",
                  new JsonObject().put("boards", event1));
              String msg = "Current High Score is: " + ((JsonObject)event1.getValue(0)).getInteger("score");
              eb.publish("org.mmog2048:status-message", msg);
            });
          }
      );

      eb.consumer("org.mmog2048:register", event -> {
        String name = ((JsonObject)event.body()).getString("name");

        JsonObject board = new JsonObject().put("tiles", new JsonArray(
            "[0,0,0,0," +
             "0,2,0,0," +
             "0,0,0,0," +
             "0,0,4,0]"))
            .put("name", name);

        String uuid = UUID.randomUUID().toString();
        redisDAO.saveBoardInfo(GameEngine.contest, uuid, board, event1 -> {});

        // Register this specific game channel to receive moves
        eb.consumer("org.mmog2048:move:" + uuid, eventMove -> {
          JsonObject eventMoveJson = ((JsonObject) eventMove.body());
          String move = eventMoveJson.getString("move");
          GameEngine.updateGame(uuid, move, redisDAO, boardInfo -> eventMove.reply(
              new JsonObject().put("board", boardInfo)
          ));
        });

        JsonObject reply = new JsonObject();
        reply.put("token", uuid);
        reply.put("board", board);
        event.reply(reply);
      });

      SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
      BridgeOptions options = new BridgeOptions();
      PermittedOptions permitted = new PermittedOptions(); /* allow everything, we don't care for the demo */
      options.addOutboundPermitted(permitted);
      options.addInboundPermitted(permitted);
      sockJSHandler.bridge(options);

      Router router = Router.router(vertx);
      StaticHandler assetHandler = StaticHandler
          .create()
          .setDirectoryListing(true);

      router.route("/eventbus/*").handler(sockJSHandler);
      router.get("/*").handler(assetHandler);

      Integer httpPort = 8080;

      vertx
          .createHttpServer()
          .requestHandler(router::accept)
          .listen(httpPort);

      LOG.info("Listening on " + httpPort);
      future.complete();
    } catch (Exception ioe) {
      LOG.error(ioe.getMessage(), ioe);
      future.fail(ioe);
    }
  }
}