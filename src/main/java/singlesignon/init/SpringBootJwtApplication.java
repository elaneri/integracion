package singlesignon.init;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication(scanBasePackages={
"singlesignon.authorizationserver", "singlesignon.controller"})
@EnableCaching
public class SpringBootJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJwtApplication.class, args);
	}
	@Bean
	public Docket buildDocket() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("singlesignon.authorizationserver")).build();
	}
}