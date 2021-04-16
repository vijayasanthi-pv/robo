package cc.robart.iot.demoproject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * The main class of this demo application.
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"cc.robart.iot.demoproject.*"})
public class DemoprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoprojectApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
