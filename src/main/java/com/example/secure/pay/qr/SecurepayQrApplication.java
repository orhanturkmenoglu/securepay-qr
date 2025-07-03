package com.example.secure.pay.qr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SecurepayQrApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurepayQrApplication.class, args);
	}

}
