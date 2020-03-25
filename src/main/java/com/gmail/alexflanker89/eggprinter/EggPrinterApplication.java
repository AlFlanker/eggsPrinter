package com.gmail.alexflanker89.eggprinter;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class EggPrinterApplication implements ApplicationRunner {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(EggPrinterApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

	}
}
