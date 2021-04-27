package cc.robo.iot.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Swagger for testing the APIs
 */
@Configuration
@EnableSwagger2
public class SwaggerEnabler {

	private ApiKey apiKey() { 
		return new ApiKey("JWT", "Authorization", "header"); 
	}

	private SecurityContext securityContext() { 
		return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
	} 

	private List<SecurityReference> defaultAuth() { 
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
		authorizationScopes[0] = authorizationScope; 
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
	}
	
	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "Firmware Server API",
	      "This server manages the firmwares for the robots",
	      "1.0",
	      "Terms of service",
	      new Contact("Vijayasanthi", "www.testing.com", "vijayasanthi.pv@gmail.com"),
	      "License of API",
	      "API license URL",
	      Collections.emptyList());
	}

	
	/**
	 * Configures api controller path
	 * Returns the {@Docket }
	 * @param attributesPriority A ordered set of attributes representing the
	 * priority order in which attributes are evaluated
	 * @return
	 */
	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()))
				.select().apis(RequestHandlerSelectors.basePackage("cc.robart.iot.demoproject.controller"))
				.paths(regex("/api.*"))
				.build();

	}
}
