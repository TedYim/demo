package com.example.demo.spring_event.anno;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * Created by Ted on 2018/11/21.
 */
@Component
public class TestPublishAnno {

    @Autowired
    private static ApplicationEventPublisher applicationEventPublisher;


    public static void publishEvent(ApplicationEvent communityArticleEvent) {
        applicationEventPublisher.publishEvent(communityArticleEvent);
    }

}
