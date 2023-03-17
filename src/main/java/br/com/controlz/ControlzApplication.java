package br.com.controlz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ControlzApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlzApplication.class, args);
	}
}


