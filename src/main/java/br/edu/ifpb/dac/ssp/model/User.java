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

@Table(name = "USER", uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_REGISTRATION"})})
@Entity
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Integer id;
	
	@Column(name = "USER_NAME", nullable = false)
	private String name;
	
	@Column(name = "USER_EMAIL", nullable = false)
	private String email;
	
	@Column(name = "USER_REGISTRATION", nullable = false)
	private Integer registration;
	
	@Column(name = "USER_PASSWORD", nullable = false)
	private String password;
	
	public User() {
		
	}
	
	public User(Integer id, String name, String email, Integer registration, String password) {
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
		this.name = name;
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
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
	

}
