package br.edu.ifpb.dac.ssp.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Table(name = "SCHEDULED_PRACTICE", uniqueConstraints = {@UniqueConstraint(columnNames = {})})
@Entity

public class Scheduling implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULED_PRACTICE_ID")
	private Integer id;
	
	@Temporal(TemporalType.DATE)
	private Date scheduledDate;
	
	@Temporal(TemporalType.TIME)
	private LocalTime duration;

	@OneToOne
	@JoinColumn(name = "place_id", referencedColumnName = "PLACE_ID")
	private Place place;
	
	@OneToOne
	@JoinColumn(name = "sport_id", referencedColumnName = "SPORT_ID")
	private Sport sport;
	
	// Organizar melhor depois:
	private User creator;
	
	@ManyToMany
	private Set<User> participants;
	
	@Transient
	private int quantityOfParticipants; 
	
	public Scheduling() {
		this.participants = new HashSet<User>();
		this.quantityOfParticipants = 0;
	}

	public Scheduling(Date scheduledDate, LocalTime duration, Place place, Sport sport,
			User creatorOfPractice) {
		this.scheduledDate = scheduledDate;
		this.duration = duration;
		this.place = place;
		this.sport = sport;
		this.creator = creatorOfPractice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public LocalTime getDuration() {
		return duration;
	}

	public void setDurationOfPractice(LocalTime duration) {
		this.duration = duration;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
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
		this.participants.remove(user);
		this.quantityOfParticipants -= 1;
	}
	
	public int getQuantityOfParticipants() {
		return quantityOfParticipants;
	}

	public void setQuantityOfParticipants(int quantityOfParticipants) {
		this.quantityOfParticipants = quantityOfParticipants;
	}
}
