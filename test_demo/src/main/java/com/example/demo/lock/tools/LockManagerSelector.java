package com.example.demo.lock.tools;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class LockManagerSelector implements ImportSelector {

    private static final String LOCK_MANAGER_CONFIGURATION_CLASS_NAME =
            "com.example.demo.lock.config.LockManagerConfiguration";

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] { LOCK_MANAGER_CONFIGURATION_CLASS_NAME };
    }
}
