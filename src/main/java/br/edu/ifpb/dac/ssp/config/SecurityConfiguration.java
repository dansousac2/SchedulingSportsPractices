package br.edu.ifpb.dac.ssp.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
		.antMatchers(HttpMethod.GET, "/actuator/**/").permitAll()
		.antMatchers(HttpMethod.POST, "/api/login").permitAll();
//		.anyRequest().authenticated();
	}
}
