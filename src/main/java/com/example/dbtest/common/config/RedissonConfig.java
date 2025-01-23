package com.example.dbtest.common.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Configuration
public class RedissonConfig {
    @Value("${redis.uri}")
    private String redisUri;

    @Bean
    public RedissonClient redissonClient() {
        String result = null;
        try {
            result = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            result = "";
        }
        if (result.equals("10.0.130.253")) redisUri = "redis://127.0.0.1:6379";
        log.info("hostName:{}, redisUri: {}", result, redisUri);
        Config config = new Config();
        config.useSingleServer().setAddress(redisUri);
        return Redisson.create(config);
    }
}
