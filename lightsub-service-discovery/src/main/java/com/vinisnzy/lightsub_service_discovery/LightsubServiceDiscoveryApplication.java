package com.vinisnzy.lightsub_service_discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class LightsubServiceDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LightsubServiceDiscoveryApplication.class, args);
	}

}
