package org.mmog2048.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.mmog2048.utils.WsClient;

public class WsClients extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    for (int x = 0; x < 1000; x++) {
      new WsClient(vertx, "Bot" + x);
    }

    startFuture.complete();
  }
}

