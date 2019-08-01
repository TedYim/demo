package com.example.demo.kafka.integrate.collection.annotation;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface ORMHBaseColumn {

    public String family() default "";

    public String qualifier() default "";

    public boolean timestamp() default false;
}
