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
	
	@Value("${app.logintype}")
	private String logintype;
	
	private String suapToken;
	
	public User login(String username, String password) throws NumberFormatException, Exception {
		switch(logintype) {
		case "suap":
			return suapLogin(username, password);
		case "local":
			return localLogin(username, password);
		default:
			return localLogin(username, password);
		}
	}

	private User suapLogin(String username, String password) throws NumberFormatException, Exception {
		String jsonToken = suapService.login(username, password);
		this.suapToken = loginConverter.jsonToToken(jsonToken);
		
		if(this.suapToken == null) {
			throw new IllegalArgumentException("Campo username ou password inválido!");
		}
		
		User user = new User();
		
		try {
			user = userService.findByRegistration(Integer.parseInt(username)).orElse(null);
		} catch(Exception e) {
			String json = suapService.findUser(this.suapToken, username);
			System.out.println("\npassou 01"); //teste
			user = loginConverter.jsonToUser(json);
			System.out.println("passou 02");
			user = userService.save(user);
			System.out.println("passou 03");
		}
		System.out.println("passou do suapLogin"); // teste
		
		return user;
	}

	private User localLogin(String username, String password) throws NumberFormatException, Exception {
		User user = userService.findByRegistration(Integer.parseInt(username)).orElse(null);
		
		if(user == null || password == null || !password.equals(user.getPassword())) {
			throw new IllegalArgumentException("Campo username ou password inválido!");
		}
		
		return user;
	}

}
