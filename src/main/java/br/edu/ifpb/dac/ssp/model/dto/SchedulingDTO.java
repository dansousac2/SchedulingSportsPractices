package br.edu.ifpb.dac.ssp.model.dto;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SchedulingDTO {
	
	private Integer id;
	
	@NotBlank(message = "It's mandatory to inform a scheduled date!")
	@Pattern(regexp = "^\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d$",
			message = "ScheduledDate should be formatted as 'dd/MM/yyyy HH:mm:ss'")
	private String scheduledDate;
	
	@NotBlank(message = "It's mandatory to inform a duration of practice!")
	@Pattern(regexp = "^\\d\\d:\\d\\d:\\d\\d$",
	message = "Duration for practice should be formatted as 'HH:mm:ss'")
	private String duration;
	
	@NotBlank(message = "It's mandatory to inform a place for practice with 4 characters or more!")
	@Size(min = 4, max = 255)
	private String placeName;
	
	@NotBlank(message = "It's mandatory to inform a sport for practice!")
	@Size(min = 4, max = 255)
	private String sportName;
	
	// Organizar depois:
	// private Integer userId;
	
	public SchedulingDTO() {
		
	}
	
	public SchedulingDTO(Integer id, String scheduledDate, String duration, String placeName, String sportName) {
		this.id = id;
		this.scheduledDate = scheduledDate.strip();
		this.duration = duration.strip();
		this.placeName = placeName.strip();
		this.sportName = sportName.strip();
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
		this.scheduledDate = scheduledDate.strip();
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration.strip();
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName.strip();
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName.strip();
	}

	@Override
	public int hashCode() {
		return Objects.hash(duration, id,  placeName, scheduledDate, sportName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedulingDTO other = (SchedulingDTO) obj;
		return Objects.equals(duration, other.duration) && Objects.equals(id, other.id)
				&& Objects.equals(placeName, other.placeName) && Objects.equals(scheduledDate, other.scheduledDate)
				&& Objects.equals(sportName, other.sportName);
	}
}
