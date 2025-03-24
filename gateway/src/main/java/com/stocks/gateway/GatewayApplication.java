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
	// .path("/stocks/**")
	// .and()
	// .method("GET", "POST", "PUT", "DELETE", "OPTIONS")
	// .uri("http://localhost:8083"))

	// // Portfolio Service Routes - GET only
	// .route(r -> r
	// .path("/portfolio/**")
	// .and()
	// .method("GET", "POST", "PUT", "DELETE", "OPTIONS")
	// .uri("http://localhost:8081"))

	// // Registration Service Routes - POST only
	// .route("REGISTER",r -> r
	// .path("/register/**")
	// .and()
	// .method("GET", "POST", "PUT", "DELETE", "OPTIONS")
	// .uri("http://localhost:8080"))

	// // Trading Service Routes - All methods
	// .route(r -> r
	// .path("/trading/**")
	// .and()
	// .method("GET", "POST", "PUT", "DELETE", "OPTIONS")
	// .uri("http://localhost:8082"))
	// .build();
	// }
}
