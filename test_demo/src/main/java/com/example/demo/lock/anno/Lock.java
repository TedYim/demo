package com.example.demo.lock.anno;

import java.lang.annotation.*;

import static com.example.demo.lock.constants.ExpireTimeConstant.TEN_MIN;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Lock {

    String lockKey();

    Class param();

    String attributes();

    int expire() default TEN_MIN;

    String errorMsg() default "请稍后重试";

    Class resultType() default Void.class;

}
