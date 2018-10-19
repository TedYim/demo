package com.example.demo.kafka.integrate.collection.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface ORMHBaseTable {

    public String tableName() default "";

}
