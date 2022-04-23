package br.edu.ifpb.dac.ssp.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class SportDTO { 
	
	private Integer id;
	
	@NotBlank(message = "Name not must be empty")
	@Pattern(regexp = "^[a-zA-ZÀ-ú\\s]{3,255}$", message = "Name is invalid! Verify if there are special characters nad size 4 chars or more.")
	private String name;
	 
	public SportDTO() {
		
	}
	
	
	public SportDTO(String name) {
		this.name = name;
	}

	public SportDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
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
		this.name = name;
	}

}
