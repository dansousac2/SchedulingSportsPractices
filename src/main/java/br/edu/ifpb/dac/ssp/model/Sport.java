package br.edu.ifpb.dac.ssp.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(name = "SPORTS_PRACTICE", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Entity
public class Sport implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SPORT_ID")
	private Integer id;

	@Column(name = "SPORT_NAME", nullable = false)
	private String name;
	
	public Sport() {
		
	}
	
	
	public Sport(Integer id, String name) {
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
