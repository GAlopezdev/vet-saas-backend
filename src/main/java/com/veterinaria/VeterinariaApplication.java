package com.veterinaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VeterinariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeterinariaApplication.class, args);
	}

}
