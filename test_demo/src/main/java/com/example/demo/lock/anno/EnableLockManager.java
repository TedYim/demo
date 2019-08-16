package com.example.demo.lock.anno;


import com.example.demo.lock.tools.LockManagerSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LockManagerSelector.class)
public @interface EnableLockManager {
}
