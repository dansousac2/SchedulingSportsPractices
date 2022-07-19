package br.edu.ifpb.dac.ssp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.ifpb.dac.ssp.service.RoleService;

@SpringBootApplication
@EnableWebMvc
public class SchedulingSportPracticesApplication implements WebMvcConfigurer, CommandLineRunner {
	
	private RoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(SchedulingSportPracticesApplication.class, args);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");
	}

	@Override
	public void run(String... args) throws Exception {
		roleService.createDefaultValues();
		
	}
}
