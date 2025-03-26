package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients
public class StockApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockApplication.class, args);
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
