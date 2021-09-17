package com.userregistration.application;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
@EnableFeignClients
public class UserRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserRegistrationApplication.class, args);
		

		
	}
	
	@Bean
	@Qualifier("swagger")
	   public Docket productApi() {
	      return new Docket(DocumentationType.SWAGGER_2).select()
	         .apis(RequestHandlerSelectors.basePackage("com.userregistration.application")).build();
	   }
	
	@Bean public LinkDiscoverers discovers() 
	{
		 List<LinkDiscoverer> plugins = new ArrayList<>(); plugins.add(new
		 CollectionJsonLinkDiscoverer()); return new
		 LinkDiscoverers(SimplePluginRegistry.create(plugins));
	}
	
	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
	    return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
	        .circuitBreakerConfig(CircuitBreakerConfig.custom()
	           .slidingWindowSize(5)
	           .permittedNumberOfCallsInHalfOpenState(5)
	           .failureRateThreshold(50.0F)
	           .waitDurationInOpenState(Duration.ofMillis(50))
	           .slowCallDurationThreshold(Duration.ofMillis(5000))
	           .slowCallRateThreshold(50.0F)
	           .build())
	        .build());
	}

}
