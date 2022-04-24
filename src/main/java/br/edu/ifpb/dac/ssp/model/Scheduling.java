package br.edu.ifpb.dac.ssp.model;

import java.io.Serializable;
import java.time.LocalDate;
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
import javax.persistence.Transient;
import javax.validation.constraints.FutureOrPresent;


@Table(name = "SCHEDULED_PRACTICE")
@Entity
public class Scheduling implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULED_PRACTICE_ID")
	private Integer id;
	
	@Column(name = "SCHEDULED_DATE", nullable = false)
	@FutureOrPresent(message = "Scheduled date shouldn't be in past")
	private LocalDate scheduledDate;
	
	@Column(name = "SCHEDULED_START_TIME", nullable = false)
	private LocalTime scheduledStartTime;
	
	@Column(name = "SCHEDULED_FINISH_TIME", nullable = false)
	private LocalTime scheduledFinishTime;

	@Column(name = "PRACTICE_PLACE_NAME", nullable = false)
	private String placeName;
	
	@Column(name = "PRACTICE_SPORT_NAME", nullable = false)
	private String sportName;
	
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

	public Scheduling(LocalDate scheduledDate, LocalTime scheduledStartTime, LocalTime scheduledFinishTime, String placeName, String sportName) {
		this.scheduledDate = scheduledDate;
		this.scheduledStartTime = scheduledStartTime;
		this.scheduledFinishTime = scheduledFinishTime;
		this.placeName = placeName;
		this.sportName = sportName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public LocalTime getScheduledStartTime() {
		return scheduledStartTime;
	}

	public void setScheduledStartTime(LocalTime scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}

	public LocalTime getScheduledFinishTime() {
		return scheduledFinishTime;
	}

	public void setScheduledFinishTime(LocalTime scheduledFinishTime) {
		this.scheduledFinishTime = scheduledFinishTime;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
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
