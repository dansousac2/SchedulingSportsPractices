package br.edu.ifpb.dac.ssp.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {
	
	private Integer id;
	
	@NotBlank(message = "Please enter your name. Is required!")
	@Size(min = 3, max = 255, message = "The space must be filled with a minimum of 3 characters and a maximum of 255!")
	private String name;
	
	@Email(message = "Email should be valid!")
	@Pattern(regexp=".+@.+\\..+", message="Please provide a valid email address")
	private String email;
	
	@NotNull(message = "Please enter your password. The password field cannot be null!")
	private Integer registration;
	
	@NotBlank(message = "Please enter your password. Is required!")
	@Size(min = 7, max = 32, message = "The password must be a minimum of 7 characters and a maximum of 32!")
	private String password;

	public UserDTO() {
		
	}
	
	public UserDTO(String name, String email, Integer registration, String password) {
		this.name = name;
		this.email = email;
		this.registration = registration;
		this.password = password;
	}
	
	public UserDTO(Integer id, String name, String email, Integer registration, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.registration = registration;
		this.password = password;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	//TODO
	public void setName(String name) {
		if(name != null) {
			this.name = name.trim();
		}
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getRegistration() {
		return registration;
	}
	public void setRegistration(Integer registration) {
		this.registration = registration;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
