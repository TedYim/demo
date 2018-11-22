package com.example.demo.configuration;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConf {

    /**
     * http服务端口
     */
    @Value("${custom.server.http.port}")
    private Integer httpPort;

    /**
     * https服务端口
     */
    @Value("${server.port}")
    private Integer httpsPort;


    /**
     * HTTP2作为新的HTTP标准，目前在Undertow上已经可以被很好地支持了，而在代码中只需要配置下，就可以启用HTTP2作为web的协议：
     * 采用Undertow作为服务器。
     * Undertow是一个用java编写的、灵活的、高性能的Web服务器，提供基于NIO的阻塞和非阻塞API，特点：
     * 非常轻量级，Undertow核心瓶子在1Mb以下。它在运行时也是轻量级的，有一个简单的嵌入式服务器使用少于4Mb的堆空间。
     * 支持HTTP升级，允许多个协议通过HTTP端口进行多路复用。
     * 提供对Web套接字的全面支持，包括JSR-356支持。
     * 提供对Servlet 3.1的支持，包括对嵌入式servlet的支持。还可以在同一部署中混合Servlet和本机Undertow非阻塞处理程序。
     * 可以嵌入在应用程序中或独立运行，只需几行代码。
     * 通过将处理程序链接在一起来配置Undertow服务器。它可以对各种功能进行配置，方便灵活。
     * Embedded 嵌入式
     */
    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        // 使用HTTP2作为协议
        factory.addBuilderCustomizers((Undertow.Builder builder) -> {
            builder.addHttpListener(httpPort, "0.0.0.0");
            //添加其他监听端口 Undertow started on port(s) 8004 (http) 8080 (http)
            builder.addHttpListener(8081, "0.0.0.0");
            // 开启HTTP2
            builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
        });

        /*factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            // 开启HTTP自动跳转至HTTPS
            deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                    .addWebResourceCollection(new WebResourceCollection().addUrlPattern("*//*"))
                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                    .setConfidentialPortManager(exchange -> httpsPort);
        });*/
        return factory;
    }

    /**
     * java web应用，做系统的初始化，一般在哪里做呢？
     * ServletContextListener里面做，listener，会跟着整个web应用的启动，就初始化，类似于线程池初始化的构建
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<InitListener> servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<InitListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(new InitListener());
        return servletListenerRegistrationBean;
    }

}
