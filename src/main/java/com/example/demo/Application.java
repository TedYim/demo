package com.example.demo;

import com.example.demo.configuration.DemoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.example.demo"})
//@MapperScan(basePackages = "com.topscore.uke.*.mapper")
@Import(value={DemoConfiguration.class})
@ImportResource(locations={"classpath:spring/spring-threadpool.xml"})
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}




