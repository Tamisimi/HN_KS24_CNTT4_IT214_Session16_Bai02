package com.fintech.user.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager manager = new ConcurrentMapCacheManager("users");
        // Không lưu giá trị null vào cache (tương đương disableCachingNullValues)
        manager.setAllowNullValues(false);
        return manager;
    }
}
