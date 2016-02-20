package org.mmog2048.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

public class EmbeddedRedis extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedRedis.class);
    private static String OS = System.getProperty("os.name").toLowerCase();
    private RedisServer server;

    @Override
    public void start(Future<Void> future) {
        try {
            RedisServerBuilder builder = RedisServer.builder()
                    .port(MainVerticle.REDIS_PORT);
            if(isWindows()) {
                // https://github.com/kstyrc/embedded-redis/issues/52
                builder = builder.setting("maxheap 512M");
            }
            server = builder.build();
            server.start(); // seems to be blocking
            LOG.info("Redis started on " + MainVerticle.REDIS_PORT);
            future.complete();
        } catch (Exception ioe) {
            LOG.error(ioe.getMessage(),ioe);
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

    private boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }
}
