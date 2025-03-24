package com.stocks.gateway;


import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    // @Bean
    // public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    //     return builder.routes()
    //             .route("example_route", r -> r.path("/example/**")
    //                     .uri("http://localhost:8081")
    //                     )
    //             .build();
    // }

    // @Bean
    // public GlobalCorsProperties globalCorsProperties() {
    //     return new GlobalCorsProperties();
    // }
}
