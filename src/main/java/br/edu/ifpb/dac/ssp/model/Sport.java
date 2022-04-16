package br.edu.ifpb.dac.ssp.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SPORTS_PRACTICE")
@Entity
public class Sport implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SPORT_ID")
	private Integer id;

	@Column(name = "SPORT_NAME", nullable = false)
	private String name;

	@Column(name = "SPORT_MINIMUM_NUMBER_OF_PRACTITIONERS", nullable = true)
	private int minimumNumberOfPractitioners;
	
	
	public Sport() {
		
	}
	
	
	public Sport(Integer id, String name, int minimumNumberOfPractitioners) {
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

	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sport other = (Sport) obj;
		return Objects.equals(id, other.id);
	}


	
}
