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

	// filtra as requisições e faz autenticação se o token for válido
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = tokenService.get(request);
		// TODO tratamento para exceções do método "doFilterInternal" ?
		if (tokenService.isValid(token)) {
			try {
				authenticate(token);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		filterChain.doFilter(request, response);
	}

	// coloca o usuário autenticado no contexto do Spring Security
	private void authenticate(String token) throws NumberFormatException, Exception {
		Long userId = tokenService.getUserId(token);
		User user = userService.findById(Integer.parseInt(userId.toString()));

		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(user, null,	user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}