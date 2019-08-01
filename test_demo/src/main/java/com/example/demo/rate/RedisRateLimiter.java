package com.example.demo.rate;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * 基于redis lua脚本的线程安全的计数器限流方案
 */
public class RedisRateLimiter {

    /**
     * 限流访问的url
     */
    private String url;

    /**
     * 单位时间的大小,最大值为 Long.MAX_VALUE - 1,以秒为单位
     */
    final Long timeUnit;

    /**
     * 单位时间窗口内允许的访问次数
     */
    final Integer limit;

    public static final String scriptText =
            "local key = KEYS[1] --限流KEY（一秒一个）\n" +
                    "local limit = tonumber(ARGV[1])        --限流大小\n" +
                    "local current = tonumber(redis.call('get', key) or \"0\")\n" +
                    "if current + 1 > limit then --如果超出限流大小\n" +
                    "    redis.call(\"INCRBY\", key,\"1\") -- 如果不需要统计真是访问量可以不加这行\n" +
                    "    return 0\n" +
                    "else  --请求数+1，并设置2秒过期\n" +
                    "    redis.call(\"INCRBY\", key,\"1\")\n" +
                    "    if tonumber(ARGV[2]) > -1 then\n" +
                    "        redis.call(\"expire\", key,tonumber(ARGV[2])) --时间窗口最大时间后销毁键\n" +
                    "    end\n" +
                    "    return 1\n" +
                    "end";

    /**
     * 需要传入一个lua script,莫名其妙redisTemplate返回值永远是个Long
     */
    private RedisScript<Long> redisScript = new DefaultRedisScript<>(scriptText, Long.TYPE);

    private RedisTemplate redisTemplate;

    /**
     * 配置键是否会过期，
     * true：可以用来做接口流量统计，用定时器去删除
     * false：过期自动删除，时间窗口过小的话会导致键过多
     */
    private boolean isDurable = false;

    public void setRedisScript(RedisScript<Long> redisScript) {
        this.redisScript = redisScript;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDurable() {
        return isDurable;
    }

    public void setDurable(boolean durable) {
        isDurable = durable;
    }

    public RedisRateLimiter(Integer limit, Long timeUnit) {
        this.timeUnit = timeUnit;
        Assert.isTrue(timeUnit < Long.MAX_VALUE - 1);
        this.limit = limit;
    }

    public RedisRateLimiter(Integer limit, Long timeUnit, boolean isDurable) {
        this(limit, timeUnit);
        this.isDurable = isDurable;
    }

    public boolean acquire() {
        return this.acquire(this.url);
    }

    public boolean acquire(String url) {
        StringBuffer key = new StringBuffer();
        key.append("rateLimiter").append(":")
                .append(url).append(":")
                .append(System.currentTimeMillis() / 1000 / timeUnit);
        Integer expire = limit + 1;
        String convertExpire = isDurable ? "-1" : expire.toString();
        return redisTemplate.execute(redisScript, Arrays.asList(key.toString()), limit.toString(), convertExpire).equals(1l);
    }

}
