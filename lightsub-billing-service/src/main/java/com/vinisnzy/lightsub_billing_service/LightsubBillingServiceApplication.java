package com.vinisnzy.lightsub_billing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class LightsubBillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LightsubBillingServiceApplication.class, args);
	}

}
