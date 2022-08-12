package br.edu.ifpb.dac.ssp.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.edu.ifpb.dac.ssp.service.RoleService.AVALIABLE_ROLES;
import br.edu.ifpb.dac.ssp.service.TokenService;
import br.edu.ifpb.dac.ssp.service.UserService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private TokenService tokenService;
	@Autowired
	private UserService userService;
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Bean
	public TokenFilter jwtTokenFilter() {
		return new TokenFilter(tokenService, userService);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userService);
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		List<String> all = Arrays.asList("*");
		
		CorsConfiguration corsConf = new CorsConfiguration();
		corsConf.setAllowedMethods(all);
		corsConf.setAllowedOriginPatterns(all);
		corsConf.setAllowedHeaders(all);
		corsConf.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConf);
		
		CorsFilter corsFilter = new CorsFilter(source);
		
		FilterRegistrationBean<CorsFilter> filter =	new FilterRegistrationBean<CorsFilter>(corsFilter);
		filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return filter;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
		.antMatchers(HttpMethod.GET, "/actuator/**/").permitAll()
		.antMatchers(HttpMethod.POST, "/api/login").permitAll()
		.antMatchers(HttpMethod.POST, "/api/isValidToken").permitAll()
		.antMatchers(HttpMethod.DELETE, "/api/user").hasRole(AVALIABLE_ROLES.ADMIN.name())
		.anyRequest().authenticated()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		http.logout(logout -> logout
								.clearAuthentication(true)
								.invalidateHttpSession(true)
								.logoutUrl("/api/logout")
								.logoutSuccessHandler(new LogoutSuccessHandler() {
									@Override
									public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
											throws IOException, ServletException {
										// Não faz nada em caso de logout positivo.
									}
								})
		);
		
	}
}
