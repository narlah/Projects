package com.example.nk.qw.QWProvingGrounds;

import com.example.nk.qw.QWProvingGrounds.domain.MessageFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QwProvingGroundsApplication {

	@Bean
	MessageFactory getFactory(){
		return new MessageFactory();
	}

	public static void main(String[] args) {
		SpringApplication.run(QwProvingGroundsApplication.class, args);
	}
}
