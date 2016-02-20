package org.mmog2048.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import redis.embedded.RedisServer;

import java.io.IOException;

public class EmbeddedRedis extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(EmbeddedRedis.class);
    private RedisServer server;

    @Override
    public void start(Future<Void> future) {
        try {
            server = new RedisServer(MainVerticle.REDIS_PORT);
            server.start(); // seems to be blocking
            log.info("Redis started on " + MainVerticle.REDIS_PORT);
            future.complete();
        } catch (IOException ioe) {
            log.error(ioe.getMessage(),ioe);
            future.fail(ioe);
        }
    }

    @Override
    public void stop(Future<Void> future) {
        if (server != null) {
            server.stop();
            server = null;
        }
        future.complete();
    }
}
