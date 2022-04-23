package br.edu.ifpb.dac.ssp.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.FutureOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "SCHEDULED_PRACTICE", uniqueConstraints = {@UniqueConstraint(columnNames = {"scheduledDate"})})
@Entity
public class Scheduling implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULED_PRACTICE_ID")
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "SCHEDULED_DATE", nullable = false)
	@FutureOrPresent(message = "Scheduled date shouldn't be in past")
	private LocalDateTime scheduledDate;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "SCHEDULED_DURATION", nullable = false)
	private LocalTime duration;

	@Column(name = "PRACTICE_PLACE_ID", nullable = false)
	private Integer placeId;
	
	@Column(name = "PRACTICE_SPORT_ID", nullable = false)
	private Integer sportId;
	
	/*
	// Organizar melhor depois
	@Column(name = "CREATED_BY")
	private User creator;
	*/
	
	@ManyToMany
	@JoinTable(
			name = "PRACTICE_PARTICIPANTS",
			joinColumns = @JoinColumn(name = "SCHEDULED_PRACTICE_ID"),
			inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	private Set<User> participants;
	
	@Transient
	private int quantityOfParticipants; 
	
	public Scheduling() {
		this.participants = new HashSet<User>();
		this.quantityOfParticipants = 0;
	}

	public Scheduling(LocalDateTime scheduledDate, LocalTime duration, Integer placeId, Integer sportId) {
		this.scheduledDate = scheduledDate;
		this.duration = duration;
		this.placeId = placeId;
		this.sportId = sportId;
		
		this.participants = new HashSet<User>();
		this.quantityOfParticipants = 0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDateTime scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public LocalTime getDuration() {
		return duration;
	}

	public void setDuration(LocalTime duration) {
		this.duration = duration;
	}

	public Integer getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}

	public Integer getSportId() {
		return sportId;
	}

	public void setSportId(Integer sportId) {
		this.sportId = sportId;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}

	public void addParticipant(User user) {
		this.participants.add(user);
		this.quantityOfParticipants += 1;
	}
	
	public void removeParticipant(User user) {
		if (this.participants.size() > 0) {
			this.participants.remove(user);
			this.quantityOfParticipants -= 1;
		}
	}
	
	public int getQuantityOfParticipants() {
		return quantityOfParticipants;
	}

	public void setQuantityOfParticipants(int quantityOfParticipants) {
		this.quantityOfParticipants = quantityOfParticipants;
	}

	public int hashCode() {
		return Objects.hash(id);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scheduling other = (Scheduling) obj;
		return Objects.equals(id, other.id);
	}
}
