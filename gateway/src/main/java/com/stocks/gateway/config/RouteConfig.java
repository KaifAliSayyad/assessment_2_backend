// package com.stocks.gateway.config;

// import org.springframework.cloud.gateway.route.RouteLocator;
// import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;

// @Configuration
// public class RouteConfig {

// @Bean
// public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
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
// }
