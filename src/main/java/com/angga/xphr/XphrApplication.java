package com.angga.xphr;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.angga.xphr")
public class XphrApplication {

	public static void main(String[] args) {
		SpringApplication.run(XphrApplication.class, args);
	}

	@Bean
	public CommandLineRunner showRoutes(ApplicationContext context) {
		return args -> {
			var mapping = context
					.getBean(org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping.class);
			mapping.getHandlerMethods().forEach((req, method) -> {
				System.out.println(req + " => " + method);
			});
		};
	}

}
