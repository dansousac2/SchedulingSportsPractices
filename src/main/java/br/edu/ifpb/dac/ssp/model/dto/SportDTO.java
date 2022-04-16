package br.edu.ifpb.dac.ssp.model.dto;

public class SportDTO { 
	
	private Integer id;
	private String name;
	private int minimumNumberOfPractitioners;
	 
	public SportDTO() {
		
	}
	
	
	public SportDTO(String name, int minimumNumberOfPractitioners) {
		this.name = name;
		this.minimumNumberOfPractitioners = minimumNumberOfPractitioners;
	}

	public SportDTO(Integer id, String name, int minimumNumberOfPractitioners) {
		this.id = id;
		this.name = name;
		this.minimumNumberOfPractitioners = minimumNumberOfPractitioners;
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
	public int getMinimumNumberOfPractitioners() {
		return minimumNumberOfPractitioners;
	}
	public void setMinimumNumberOfPractitioners(int minimumNumberOfPractitioners) {
		this.minimumNumberOfPractitioners = minimumNumberOfPractitioners;
	}
	

}
