package com.cryptotrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "com.cryptotrade.model")
public class CryptoTradeApplication {
    public static void main (String[] args) {
        SpringApplication.run(CryptoTradeApplication.class, args);
    }
}