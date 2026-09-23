package com.fintech.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching  // BẮT BUỘC — kích hoạt Cache AOP Proxy + CacheInterceptor
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
