package me.lfojacintho.hellofresh.deliverycheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("me.lfojacintho.hellofresh.deliverycheck")
public class HellofreshDeliveryCheckApplication {

	public static void main(String[] args) {
			SpringApplication.run(HellofreshDeliveryCheckApplication.class, args);
	}
}
