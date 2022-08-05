package br.edu.ifpb.dac.ssp.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.userdetails.UserDetails;

@Table(name = "USER", uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_REGISTRATION"})})
@Entity
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Integer id;
	
	@Column(name = "USER_NAME", nullable = false)
	private String name;
	
	@Column(name = "USER_EMAIL", nullable = true)
	private String email;
	
	@Column(name = "USER_REGISTRATION", nullable = false)
	private Long registration;
	
	@Column(name = "USER_PASSWORD", nullable = true)
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Role> roles;
	
	public User() {
		
	}
	
	public User(Integer id, String name, String email, Long registration, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.registration = registration;
		this.password = password;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
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

	public Long getRegistration() {
		return registration;
	}

	public void setRegistration(Long registration) {
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

	@Override
	public Collection<Role> getAuthorities() {
		return roles;
	}
	
	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
