package org.mmog2048.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
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
import org.apache.commons.lang3.RandomStringUtils;
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
      RedisDAO redisDAO = new RedisDAO(redisClient);

      // todo What do we want to do with this guy
      redisClient.info(event1 -> System.out.println(event1.result()));

      EventBus eb = vertx.eventBus();
      vertx.setPeriodic(
          1000, event -> {
            String now = DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date());
            System.out.println(now);

            eb.publish("org.mmog2048:game-state",
                new JsonObject().put("serverTime", now));

            eb.publish("org.mmog2048:status-message", "Hello " + RandomStringUtils.randomAlphabetic(10) + " " + now);
          }
      );

      eb.consumer("org.mmog2048:register", event -> {
        String name = ((JsonObject)event.body()).getString("name");
        System.out.println(event.replyAddress());

        // todo write this to redis
        String uuid = UUID.randomUUID().toString();
        JsonObject reply = new JsonObject();
        reply.put("token", uuid);
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