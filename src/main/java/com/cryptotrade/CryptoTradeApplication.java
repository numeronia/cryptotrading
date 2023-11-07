package com.cryptotrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoTradeApplication {
    public static void main (String[] args) {
        SpringApplication.run(CryptoTradeApplication.class, args);
    }
}