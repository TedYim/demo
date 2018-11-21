package com.example.demo.spring_event.code;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * Created by Ted on 2018/11/21.
 */
@Component
public class TestPublishCode implements ApplicationEventPublisherAware {

    private static ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        TestPublishCode.applicationEventPublisher = applicationEventPublisher;
    }

    public static void publishEvent(ApplicationEvent communityArticleEvent) {
        applicationEventPublisher.publishEvent(communityArticleEvent);
    }

}
