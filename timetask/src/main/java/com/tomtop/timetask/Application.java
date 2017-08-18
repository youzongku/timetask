package com.tomtop.timetask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	

	public static void main(String[] args) {
//		String log4jPath = Application.class.getClassLoader().getResource("").getPath()+"log4j.properties";
//		PropertyConfigurator.configure(log4jPath);
		SpringApplication.run(Application.class, args);
	}
}
