package br.edu.ifpb.dac.ssp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import br.edu.ifpb.dac.ssp.model.User;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class LoginService {

	@Autowired
	private UserService userService;
	@Autowired
	private SuapService suapService;
	@Autowired
	private LoginConverterService loginConverter;
	@Autowired
	private TokenService tokenService;
	
	private String suapToken;

	public String login(String username, String password) throws NumberFormatException, Exception {
		if(username == null || password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Campo username ou password inválido!");
		}
			
		String jsonToken = suapService.login(username, password);
		this.suapToken = loginConverter.jsonToToken(jsonToken);
		
		if(this.suapToken == null) {
			throw new IllegalArgumentException("Campo username ou password inválido!");
		}
		
		User user = new User();
		
		try {
			user = userService.findByRegistration(Long.parseLong(username)).orElse(null);
		} catch(Exception e) {
			String json = suapService.findUser(this.suapToken, username);
			user = loginConverter.jsonToUser(json);
			user = userService.save(user);
		}
		
		return tokenService.generate(user);
	}

	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (User) authentication.getPrincipal();
	}
}
