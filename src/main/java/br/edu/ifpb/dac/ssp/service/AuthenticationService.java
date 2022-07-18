package br.edu.ifpb.dac.ssp.service;

import br.edu.ifpb.dac.ssp.model.User;

public interface AuthenticationService {
	
	public String login(String username, String password);
	
	public String suapLogin(String username, String password);
	
	public User getLoggedUser();

}
