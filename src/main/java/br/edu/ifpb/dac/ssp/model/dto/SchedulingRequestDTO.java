package br.edu.ifpb.dac.ssp.model.dto;

import java.util.Objects;

public class SchedulingRequestDTO {

	// Aqui vão ficar os campos recebidos na requisição
	// A maioria está como string, estou pensando em como vamos receber melhor esses dados 
	
	private Integer id;
	private String scheduledDate;
	private String duration;
	private String placeName;
	private String sportName;
	// Aqui seria para caso o usuário já queira passar uma lista inicial de participantes, aí no service quero organizar uma lógica para, caso esse atributo não venha nulo,
	// puxar a lista e setar, vou ver se fica bom depois.
	private String listParticipants;
	// Organizar depois:
	// private Integer userId;
	
	public SchedulingRequestDTO() {
		
	}
	
	public SchedulingRequestDTO(Integer id, String scheduledDate, String duration, String placeName, String sportName, String listParticipants) {
		this.id = id;
		this.scheduledDate = scheduledDate;
		this.duration = duration;
		this.placeName = placeName;
		this.sportName = sportName;
		this.listParticipants = listParticipants;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
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

	public String getListParticipants() {
		return listParticipants;
	}

	public void setListParticipants(String listParticipants) {
		this.listParticipants = listParticipants;
	}

	@Override
	public int hashCode() {
		return Objects.hash(duration, id, listParticipants, placeName, scheduledDate, sportName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedulingRequestDTO other = (SchedulingRequestDTO) obj;
		return Objects.equals(duration, other.duration) && Objects.equals(id, other.id)
				&& Objects.equals(listParticipants, other.listParticipants)
				&& Objects.equals(placeName, other.placeName) && Objects.equals(scheduledDate, other.scheduledDate)
				&& Objects.equals(sportName, other.sportName);
	}
}
