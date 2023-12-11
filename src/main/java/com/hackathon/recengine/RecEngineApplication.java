package com.hackathon.recengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.hackathon.recengine.config")
public class RecEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecEngineApplication.class, args);
	}

}
