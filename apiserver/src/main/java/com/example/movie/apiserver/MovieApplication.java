package com.example.movie.apiserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 影片服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.example.movie", "com.example.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example")
@MapperScan("com.example.movie.infrastructure.mapper")
public class MovieApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieApplication.class, args);
    }
}
