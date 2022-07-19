package br.edu.ifpb.dac.ssp.model.dto;

public class TokenDTO {
	
	private String token;
	private UserDTO user;
	
	public TokenDTO(String token, UserDTO user) {
		this.token = token;
		this.user = user;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}

}

