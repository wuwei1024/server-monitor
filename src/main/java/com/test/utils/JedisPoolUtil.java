package com.test.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {
    private static JedisPool pool;

    static {
        createJedisPool();
    }

    /**
     * 建立连接池 真实环境，一般把配置参数缺抽取出来。
     */
    private static void createJedisPool() {

        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();

        // 设置最大连接数
        config.setMaxTotal(100);

        // 设置最大阻塞时间，毫秒数milliseconds
        config.setMaxWaitMillis(1000);

        // 设置空间连接
        config.setMaxIdle(10);

        // 创建连接池
        pool = new JedisPool(config, "127.0.0.1", 6379, 3000, "123456");
    }

    /**
     * 获取一个jedis对象
     *
     * @return
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 归还一个连接
     *
     * @param jedis
     */
    public static void returnJedis(Jedis jedis) {
        pool.returnResource(jedis);
    }
}
