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
    deploymentIds = new ArrayList<>(1);
  }

  @Override
  public void start(Future<Void> future) {
    DeploymentOptions webserverOptions = new DeploymentOptions();
    vertx.deployVerticle("WebServer.groovy",webserverOptions,serverResult -> {
      if(serverResult.failed()) {
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
    config.put("host", "localhost");
    config.put("port", REDIS_PORT);
    return config;
  }
}
