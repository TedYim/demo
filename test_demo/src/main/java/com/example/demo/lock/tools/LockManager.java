package com.example.demo.lock.tools;

import com.example.demo.lock.anno.Lock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Order(1)
@Aspect
public class LockManager {

    private static final Logger log = LoggerFactory.getLogger(LockManager.class);

    private RedisTemplate redisTemplate;

    public LockManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(com.example.demo.lock.anno.Lock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Lock redisLock = AnnotationUtils.findAnnotation(
                AopUtils.getMostSpecificMethod(((MethodSignature) joinPoint.getSignature()).getMethod(),
                        joinPoint.getTarget().getClass()), Lock.class);

        switch (redisLock.type()) {
            case SINGLE:
                return this.singleLock(redisLock, joinPoint);
            case MULTI:
                return this.multiLock(redisLock, joinPoint);
            default:
                return joinPoint.proceed();
        }

    }

    private Object multiLock(Lock redisLock, ProceedingJoinPoint joinPoint) throws Throwable {

        Collection fieldValue = (Collection) joinPoint.getArgs()[0];
        Assert.notEmpty(fieldValue, "Multi lock key is not correct !");

        Map<String, String> lockMap = new HashMap<>();
        for (Object value : fieldValue) {
            String lockKey = String.format(redisLock.lockKey(), value);
            lockMap.put(lockKey, Thread.currentThread().getId() + "_" + System.currentTimeMillis());
        }

        try {
            //获取锁
            Map<String, Boolean> keyMap = RedisTools.tryGetDistributedLock(redisTemplate, lockMap, redisLock.expire());
            for (Map.Entry<String, Boolean> entry : keyMap.entrySet()) {
                if (entry.getValue()) {
                    log.info("Thread：{}，Batch lock success，key:{}", Thread.currentThread().getId(), entry.getKey() + "");
                } else {
                    lockMap.remove(entry.getKey());
                    log.info("Thread：{}，Batch lock failed，key:{}", Thread.currentThread().getId(), entry.getKey() + "");
                    throw (RuntimeException) redisLock.exceptionType().getConstructor(String.class).newInstance(redisLock.errorMsg());
                }
            }
            return joinPoint.proceed();
        } catch (RuntimeException e) {
            log.error("Business has cause some exception !", e);
            throw e;
        } catch (Throwable e) {
            log.error("Method has cause some exception !", e);
            throw new RuntimeException("Method cause some Exception !", e);
        } finally {
            try {
                //释放锁
                Map<String, Boolean> keyMap = RedisTools.releaseDistributedLock(redisTemplate, lockMap);
                for (Map.Entry<String, Boolean> entry : keyMap.entrySet()) {
                    if (entry.getValue()) {
                        log.info("Thread：{}，Batch release success，key:{}", Thread.currentThread().getId(), entry.getKey() + "");
                    } else {
                        log.info("Thread：{}，Batch release failed，key:{}", Thread.currentThread().getId(), entry.getKey() + "");
                    }
                }
            } catch (Exception e) {
                log.error("释放锁异常，错误信息" + e.getMessage(), e);
            }
        }


    }


    private Object singleLock(Lock redisLock, ProceedingJoinPoint joinPoint) throws Throwable {

        Object fieldValue = joinPoint.getArgs()[0];
        Assert.notNull(fieldValue, "Single lock key is not correct !");

        String lockKey = String.format(redisLock.lockKey(), fieldValue);
        String requestId = Thread.currentThread().getId() + "_" + System.currentTimeMillis();

        try (RedisTools.Lock lock = RedisTools.tryGetDistributedLock2(redisTemplate, lockKey, requestId, redisLock.expire())) {
            if (!lock.isSuccess()) {
                log.info("Thread：{}，Lock failed，key:{}", Thread.currentThread().getId(), lockKey);
                throw (RuntimeException) redisLock.exceptionType().getConstructor(String.class).newInstance(redisLock.errorMsg());
            }
            log.debug("Thread：{}，Lock success，key:{}", Thread.currentThread().getId(), lockKey);
            return joinPoint.proceed();
        } catch (RuntimeException e) {
            log.error("Business has cause some exception !", e);
            throw e;
        } catch (Throwable e) {
            log.error("Method has cause some exception !", e);
            throw new RuntimeException(" Method has cause some Exception !", e);
        }

    }

}


