package br.edu.ifpb.dac.ssp.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.service.TokenService;
import br.edu.ifpb.dac.ssp.service.UserService;

public class TokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UserService userService;
	
	public TokenFilter(TokenService tokenService, UserService userService) {
		this.tokenService = tokenService;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = tokenService.get(request);
		boolean valid = tokenService.isValid(token);
		
		if(valid) {
			authenticate(token);
		}
		
		filterChain.doFilter(request, response);
	}

	private void authenticate(String token) {
		// para teste, criei a regra que o token é atribuído ao usuario no banco, 
		// pois aqui precisamos do user do banco (segundo o slide) e só temos o token
		User user = userService.findByToken(token);
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public TokenService getTokenService() {
		return tokenService;
	}

	public void setTokenService(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
