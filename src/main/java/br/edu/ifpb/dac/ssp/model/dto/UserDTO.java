package br.edu.ifpb.dac.ssp.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {
	
	private Integer id;
	
	@NotBlank(message = "É obrigatório informar o nome do usuário!")
	@Size(min = 3, max = 255, message = "Nome inválido! Deve possuir mais que 3 caracteres")
	private String name;
	
	@Email(message = "O email deve ser válido!")
	@Pattern(regexp=".+@.+\\..+", message="O email deve ser válido!")
	private String email;
	
	@NotNull(message = "É obrigatório informar a matrícula do usuário!")
	private Integer registration;
	
	@NotBlank(message = "É obrigatório informar a senha do usuário!")
	@Size(min = 7, max = 50, message = "A senha deve possuir no mínimo 7 caracteres!")
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
