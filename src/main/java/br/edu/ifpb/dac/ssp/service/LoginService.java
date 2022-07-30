package br.edu.ifpb.dac.ssp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
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
	
	private String suapToken;
	
	public User suapLogin(String username, String password) throws NumberFormatException, Exception {
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
		
		return user;
	}
}
