package org.mmog2048.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
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
import org.mmog2048.utils.RedisUtils;

public class WebServer extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);

  @Override
  public void start(Future<Void> future) {
    try {
    JsonObject config = context.config();
    RedisOptions redisOptions = RedisUtils.createRedisOptions(config.getJsonObject("redis"));
    RedisClient.create(vertx, redisOptions);

    vertx.eventBus().consumer("org.mmog2048",
        event -> LOG.info("Received news: " + event.body()));

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