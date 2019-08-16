package com.example.demo.lock.tools;

import com.example.demo.lock.anno.Lock;
import com.google.protobuf.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;

@Order(1)
@Aspect
public class LockManager {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private RedisTemplate redisTemplate;

    public LockManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(com.example.demo.lock.anno.Lock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Method targetMethod = AopUtils.getMostSpecificMethod(method, joinPoint.getTarget().getClass());

        Lock redisLock = AnnotationUtils.findAnnotation(targetMethod, Lock.class);//获取注解上的信息
        Object fieldValue = null;
        try {
            for (Object arg : joinPoint.getArgs()) {
                if (redisLock.param().isInstance(arg)) {
                    //使用BeanWrapper,代码更健壮
                    BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(arg);
                    fieldValue = bw.getPropertyValue(redisLock.attributes());
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Reflection has cause some exception !", e);
            throw new RuntimeException("Reflection has cause some exception !");//直接抛系统异常
        }

        if (fieldValue == null) throw new RuntimeException("param attributes is not correct !");//直接抛系统异常

        String lockKey = String.format(redisLock.lockKey(), fieldValue);
        String requestId = Thread.currentThread().getId() + "_" + System.currentTimeMillis();

        boolean isLocked = true;
        try {
            //获取锁
            isLocked = RedisTools.tryGetDistributedLock(redisTemplate, lockKey, requestId, redisLock.expire());
            if (!isLocked) {
                log.debug("线程：{}，加锁失败，key:{}", Thread.currentThread().getId(), lockKey);
                throw new ServiceException("请勿重复提交, 休息一会再提交吧~");
            }
            log.debug("线程：{}，加锁成功，key:{}", Thread.currentThread().getId(), lockKey);
            return joinPoint.proceed();
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            log.error("Reflection has cause some exception !", e);
            throw new RuntimeException("cause some Exception !", e);
        } finally {
            if (isLocked) {
                try {
                    //释放锁
                    if (RedisTools.releaseDistributedLock(redisTemplate, lockKey, requestId)) {
                        log.debug("线程：{}，释放锁成功，key:{}", Thread.currentThread().getId(), lockKey);
                    } else {
                        log.debug("线程：{}，释放锁失败，key:{}", Thread.currentThread().getId(), lockKey);
                    }
                } catch (Exception e) {
                    log.error("释放锁异常，错误信息" + e.getMessage(), e);
                }
            }
        }
    }

}


