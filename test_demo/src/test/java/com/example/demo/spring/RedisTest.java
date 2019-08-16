package com.example.demo.spring;

import com.example.demo.jedis.ExpireTimeConstant;
import com.example.demo.jedis.RedisKeyConstant;
import com.example.demo.jedis.RedisTools;
import com.example.demo.jedis.RedisUtils;
import com.example.demo.spring_event.code.EventDemo;
import com.example.demo.spring_event.code.TestPublishCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 测试成功,可以修改key的过期时间
     * @throws Exception
     */
    @Test
    public void testRedisExpire() throws Exception {
        String lockKey = String.format(RedisKeyConstant.or_orderMain_order_id, "510000");
        String requestId = Thread.currentThread().getId() + "_" + System.currentTimeMillis();
        //1. 先加锁10min
        Boolean isLocked = RedisTools.tryGetDistributedLock(redisTemplate, lockKey, requestId, ExpireTimeConstant.TEN_MIN);
        //2. 拿到这个锁的超时时间
        System.out.println(isLocked);
        //3. 修改超时时间
        redisUtils.expire(lockKey, 60);
        //4. 再获取看超时时间
        System.out.println(isLocked);


    }

}
