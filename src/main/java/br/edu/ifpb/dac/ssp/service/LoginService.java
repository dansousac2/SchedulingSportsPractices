package br.edu.ifpb.dac.ssp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private String suapToken;
	
	// LOCAL
	public String login(String username, String password) throws NumberFormatException, Exception {
		
		User user = suapLogin(username, password);
		
		// lança exceção em caso de falha
		Authentication authentication =
				authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password));
		
		return tokenService.generate(user);
	}

	// retorna usuário logado no contexto do Spring Security
	public User getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		return (User) authentication.getPrincipal();
	}
	
	// SUAP
	private User suapLogin(String username, String password) throws NumberFormatException, Exception {
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
			user.setPassword(password);
			user = userService.save(user);
		}
		
		return user;
	}
}
