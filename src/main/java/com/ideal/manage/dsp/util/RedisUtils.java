//package com.ideal.manage.dsp.util;
//
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
///**
// * Created by xbb on 16/10/11.
// */
//public class RedisUtils {
//    private static String HOST = "192.168.169.138";
//
//    private static int PORT = 6379;
//
//    private static int MAX_ACTIVE = 1024;
//
//    private static int MAX_IDLE = 200;
//
//    private static int MAX_WAIT = 10000;
//
//    private static int TIMEOUT = 10000;
//
//    private static boolean TEST_ON_BORROW = true;
//
//    private static JedisPool jedisPool = null;
//
//    static {
//        try {
//            JedisPoolConfig config = new JedisPoolConfig();
//            config.setMaxTotal(MAX_ACTIVE);
//            config.setMaxIdle(MAX_IDLE);
//            config.setMaxWaitMillis(MAX_WAIT);
//            config.setTestOnBorrow(TEST_ON_BORROW);
//            jedisPool = new JedisPool(config, HOST, PORT, TIMEOUT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**获得jedis对象*/
//
//    public static Jedis getJedisObject(){
//
//        return jedisPool.getResource();
//
//    }
//
//    /**归还jedis对象*/
//
//    public static void recycleJedisOjbect(Jedis jedis){
//        jedisPool.returnResource(jedis);
//    }
//
//    public static void main(String[] args) {
//
//        Jedis jedis = getJedisObject();//获得jedis实例
//
//        //获取jedis实例后可以对redis服务进行一系列的操作
//
//        jedis.set("name", "zhuxun");
//
//        System.out.println(jedis.get("name"));
//
//        jedis.del("name");
//
//        System.out.println(jedis.exists("name"));
//
//        recycleJedisOjbect(jedis); //将 获取的jedis实例对象还回迟中
//
//    }
//}
