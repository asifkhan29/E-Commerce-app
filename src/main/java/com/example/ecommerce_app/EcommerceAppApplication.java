package com.example.ecommerce_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
@OpenAPIDefinition(
  info = @Info(
   title = "Ecommerce Management System API",
   version = "1.0.0",
   description = "API for managing products, users, and orders with dynamic discount computation"     
  )
)
@SecurityScheme(
 name = "bearerAuth",
 type = SecuritySchemeType.HTTP,
 scheme = "bearer",
 bearerFormat = "JWT"
)
public class EcommerceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceAppApplication.class, args);
	}

}
