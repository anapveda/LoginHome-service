package com.players.LoginHome_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient/*enough since we want this to be discovered not calling any other one*/
public class LoginHomeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginHomeServiceApplication.class, args);
	}

}
