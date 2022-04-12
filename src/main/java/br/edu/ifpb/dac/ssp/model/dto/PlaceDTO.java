package br.edu.ifpb.dac.ssp.model.dto;


public class PlaceDTO {

	private Integer id;
	private String name;
	private String reference;
	private int maximumCapacityParticipants;
	private boolean isPublic;
	
	public PlaceDTO(Integer id, String name, String reference, int maximumCapacityParticipants, boolean isPublic) {
		super();
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
