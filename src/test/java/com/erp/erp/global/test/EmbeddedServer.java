package com.erp.erp.global.test;

import java.io.IOException;
import redis.embedded.RedisServer;

public class EmbeddedServer {

    private static RedisServer redisServer;

    private final static int REDIS_PORT = 6379;

    public static void startRedis() throws IOException {
        if (redisServer == null || !redisServer.isActive()) {
            redisServer = new RedisServer(REDIS_PORT);
            redisServer.start();
        }
    }

    public static void stopRedis() throws IOException {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }
}
