package br.edu.ifpb.dac.ssp.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class PlaceDTO {

	private Integer id;
	@NotBlank(message = "É obrigatório informar o nome do local!")
	@Pattern(regexp = "^[a-zA-ZÀ-ú0-9\\s]{4,255}$", message = "Nome inválido! Deve possuir mais que 3 caracteres e não possuir caracteres especiais")
	private String name;
	private String reference;
	@Positive(message = "A capacidade de participantes deve ser um valor positivo!")
	@Max(value = 400, message = "O valor máximo para capacidade de participantes é 400!")
	private int maximumCapacityParticipants;
	private boolean isPublic;
	
	public PlaceDTO() {
		
	}
	
	public PlaceDTO(String name, String reference, int maximumCapacityParticipants, boolean isPublic) {
		this.name = name;
		this.reference = reference;
		this.maximumCapacityParticipants = maximumCapacityParticipants;
		this.isPublic = isPublic;
	}
	
	public PlaceDTO(Integer id, String name, String reference, int maximumCapacityParticipants, boolean isPublic) {
		this.id = id;
		this.name = name;
		this.reference = reference;
		this.maximumCapacityParticipants = maximumCapacityParticipants;
		this.isPublic = isPublic;
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

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public int getMaximumCapacityParticipants() {
		return maximumCapacityParticipants;
	}

	public void setMaximumCapacityParticipants(int maximumCapacityParticipants) {
		this.maximumCapacityParticipants = maximumCapacityParticipants;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
}
