package br.edu.ifpb.dac.ssp.service;

import javax.servlet.http.HttpServletRequest;

import br.edu.ifpb.dac.ssp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;


public interface TokenService {
	
	String generate(User user);
	
	Claims getClaims(String token) throws ExpiredJwtException;
	
	boolean isValid(String token);
	
	String getUserName(String token);
	
	Long getUserId(String token);
	
	String get(HttpServletRequest request); 
}