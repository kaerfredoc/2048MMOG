package org.mmog2048.verticles;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.mmog2048.utils.MultipleFutures;

import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {
  public static final int REDIS_PORT = 8888;
  private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);

  private List<String> deploymentIds;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    deploymentIds = new ArrayList<>(2);
  }

  @Override
  public void start(Future<Void> future) {
    deployWebServer(future);
  }

  private void deployEmbeddedDbs(Future<Void> future, Handler<Future<Void>> whatsNext) {
    MultipleFutures dbDeployments = new MultipleFutures();
    dbDeployments.add(this::deployEmbeddedRedis);
    dbDeployments.setHandler(result -> {
      if (result.failed()) {
        future.fail(result.cause());
      } else {
        whatsNext.handle(future);
      }
    });
    dbDeployments.start();
  }

  private void deployEmbeddedRedis(Future<Void> future) {
    DeploymentOptions options = new DeploymentOptions().setWorker(true);
    vertx.deployVerticle(EmbeddedRedis.class.getName(), options, result -> {
      if (result.failed()) {
        future.fail(result.cause());
      } else {
        deploymentIds.add(result.result());
        future.complete();
      }
    });
  }

  private void deployWebServer(Future<Void> future) {
    vertx.deployVerticle("src/main/js/board-engine-verticle.js");
    vertx.deployVerticle("org.mmog2048.verticles.WsClients");

    DeploymentOptions options = new DeploymentOptions().setConfig(redisConfig());
    vertx.deployVerticle(WebServer.class.getName(), options, serverResult -> {
      if (serverResult.failed()) {
        future.fail(serverResult.cause());
      } else {
        deploymentIds.add(serverResult.result());
        future.complete();
      }
    });
  }

  @Override
  public void stop(Future<Void> future) {
    MultipleFutures futures = new MultipleFutures(future);
    deploymentIds.forEach(deploymentId -> futures.add(fut -> undeploy(deploymentId, fut)));
    futures.start();
  }

  private void undeploy(String deploymentId, Future<Void> future) {
    vertx.undeploy(deploymentId, res -> {
      if (res.succeeded()) {
        future.complete();
      } else {
        future.fail(res.cause());
      }
    });
  }

  private static JsonObject redisConfig() {
    JsonObject config = new JsonObject();
    config.put("redis", (new JsonObject().put("host", "localhost").put("port", REDIS_PORT)));
    return config;
  }
}
