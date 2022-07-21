package br.edu.ifpb.dac.ssp.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
	
	@Autowired
	private SuapService suapService;
	
	public boolean isValid(String token) {
		if(token == null) {
			return false;
		}
		return suapService.isValidToken(token);
	}

	public String get(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		
		if(authorization == null || !authorization.startsWith("Bearer")) {
			return null;
		}
		
		return authorization.split(" ")[1];
	}
	
	
}
