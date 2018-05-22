package com.example.demo.configuration;

import io.undertow.UndertowOptions;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfiguration {

    /**
     * HTTP2作为新的HTTP标准，目前在Undertow上已经可以被很好地支持了，而在代码中只需要配置下，就可以启用HTTP2作为web的协议：
     * @return
     */
    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        // 使用HTTP2作为协议
        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
        //添加其他监听端口 Undertow started on port(s) 8004 (http) 8080 (http)
        factory.addBuilderCustomizers(builder -> builder.addHttpListener(8080, "0.0.0.0"));
        factory.addBuilderCustomizers(builder -> builder.addHttpListener(8081, "0.0.0.0"));
        return factory;
    }
}
