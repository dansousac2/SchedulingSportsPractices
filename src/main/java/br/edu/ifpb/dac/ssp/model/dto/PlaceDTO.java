package br.edu.ifpb.dac.ssp.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class PlaceDTO {

	private Integer id;
	@Pattern(regexp = "^[^ ][a-zA-ZÀ-ú0-9\\s]{3,255}$", message = "Name is invalid! Verify and try again.")
	private String name;
	private String reference;
	@Size(max = 400, message = "capacity max is 400")
	@Positive(message = "capacity must be positive")
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
