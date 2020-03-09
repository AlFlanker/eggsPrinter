package com.gmail.alexflanker89.eggPrinter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class EggPrinterApplication  {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(EggPrinterApplication.class, args);
	}
}
