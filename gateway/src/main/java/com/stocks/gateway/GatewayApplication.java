package com.stocks.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	// @Bean
	// RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
	// return builder.routes()
	// // Exchange Service Routes - GET, POST only
	// .route(r -> r
	// .path("/exchange/**")
	// .and()
	// .method(HttpMethod.GET, HttpMethod.POST)
	// .uri("http://localhost:8080"))

	// // Portfolio Service Routes - GET only
	// .route(r -> r
	// .path("/portfolio/**")
	// .and()
	// .method(HttpMethod.GET)
	// .uri("http://localhost:8081"))

	// // Registration Service Routes - POST only
	// .route(r -> r
	// .path("/registration/**")
	// .and()
	// .method(HttpMethod.POST)
	// .uri("http://localhost:8082"))

	// // Trading Service Routes - All methods
	// .route(r -> r
	// .path("/trading/**")
	// .and()
	// .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
	// HttpMethod.DELETE)
	// .uri("http://localhost:8083"))
	// .build();
	// }
}
