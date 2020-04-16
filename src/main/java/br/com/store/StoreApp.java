package br.com.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StoreApp {

	public static void main(String[] args) {
		SpringApplication.run(StoreApp.class, args);
	}

}

