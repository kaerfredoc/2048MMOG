package org.mmog2048.dao;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.redis.RedisClient;

public class RedisDAO {
    private static final Logger log = LoggerFactory.getLogger(RedisDAO.class);

    private RedisClient redis;

    public RedisDAO(RedisClient redis) {
        this.redis = redis;
    }
}
