package br.edu.ifpb.dac.ssp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import br.edu.ifpb.dac.ssp.model.User;

@Service
// Comentei aqui porque um dos erros que deu foi relacionado à esse campo
//@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class LoginService {

	@Autowired
	private UserService userService;
	@Autowired
	private SuapService suapService;
	@Autowired
	private LoginConverterService loginConverter;
	@Autowired
	private AuthenticationManager authenticationMng;
	
	private String suapToken;

	public String suapLogin(String username, String password) throws NumberFormatException, Exception {
		// erro em caso de falha na autenticação
		Authentication authentication = 
				authenticationMng.authenticate(new UsernamePasswordAuthenticationToken(username, password));
				
		String jsonToken = suapService.login(username, password);
		this.suapToken = loginConverter.jsonToToken(jsonToken);
		
		if(this.suapToken == null) {
			throw new IllegalArgumentException("Campo username ou password inválido!");
		}
		
		User user = new User();
		
		try {
			user = userService.findByRegistration(Long.parseLong(username)).get();
		} catch(Exception e) {
			String json = suapService.findUser(this.suapToken, username);
			user = loginConverter.jsonToUser(json);
			user.setToken(this.suapToken);
			user = userService.save(user);
		}
		
		return suapToken;
	}
	
	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (User) authentication.getPrincipal();
	}
}
