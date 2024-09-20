package com.servicebuddy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class ServiceBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceBuddyApplication.class, args);
	}


	@Bean
	ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
