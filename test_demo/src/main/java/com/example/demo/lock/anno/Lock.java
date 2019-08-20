package com.example.demo.lock.anno;


import com.example.demo.lock.constants.ExpireTimeConstant;
import com.example.demo.lock.constants.RedisKeyConstant;
import com.example.demo.lock.enums.LockType;
import com.example.demo.lock.tools.LockManager;

import java.lang.annotation.*;

/**
 * 定义了一个AOP实现的Redis分布式锁,处理类:
 * @see LockManager
 * 1.LockType必须指定,Single & Multi
 * 2.key必须在注解方法的第一个参数,Single为单值(String,Long等...),Multi必须为List类型
 * 3.使用@EnableLockManager启动分布式锁切面
 *
 * 改造方法:将内部业务逻辑抽取出来,并将要加锁的id放在内部方法的第一个参数上,
 *          必须使用public方法,以及处理好内部方法调用时无法切面的问题,这里就不赘述了.
 *
 * 例子:
 *  @Lock(type = LockType.MULTI, lockKey = RedisKeyConstant.or_orderMain_order_id, exceptionType = OrderServiceException.class )
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Lock {

    /**
     * @see LockType
     */
    LockType type();

    /**
     * @see RedisKeyConstant
     */
    String lockKey();

    /**
     * @see ExpireTimeConstant
     */
    int expire() default ExpireTimeConstant.TEN_MIN;

    String errorMsg() default "请勿重复提交, 休息一会再提交吧~";

    Class exceptionType() default RuntimeException.class;

    Class resultType() default Void.class;

}
