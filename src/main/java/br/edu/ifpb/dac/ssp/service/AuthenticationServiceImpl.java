package br.edu.ifpb.dac.ssp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.User;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SuapService suapService;
	
	@Autowired
	private LoginConverterService converterService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private String suapToken;
	
	public String login(String username, String password) {
		return suapLogin(username, password);
	}

	@Override
	public String suapLogin(String username, String password) {
		String jsonToken = suapService.login(username, password);
		this.suapToken = converterService.jsonToToken(jsonToken);
		
		if(this.suapToken == null) {
			throw new IllegalArgumentException("Incorrect Email or Password");
		}
		
		User user = userService.findByName(username);
		
		if(user == null) {
			String json = suapService.findUser(suapToken, username);
			user = converterService.jsonToUser(json);
			
			user = userService.save(user);
		}
	}

	@Override
	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (User) authentication.getPrincipal();
	}
	
	

}

