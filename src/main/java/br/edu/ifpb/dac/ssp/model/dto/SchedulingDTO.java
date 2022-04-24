package br.edu.ifpb.dac.ssp.model.dto;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SchedulingDTO {
	
	private Integer id;
	
	@NotBlank(message = "It's mandatory to inform a scheduled start date!")
	@Pattern(regexp = "^\\d\\d\\d\\d-\\d\\d-\\d\\d$",
			message = "Scheduled start date should be formatted as 'yyyy-MM-dd'")
	private String scheduledDate;
	
	@NotBlank(message = "It's mandatory to inform a scheduled start time!")
	@Pattern(regexp = "^\\d\\d:\\d\\d$",
			message = "Scheduled start time should be formatted as 'HH:mm'")
	private String scheduledStartTime;
	
	@NotBlank(message = "It's mandatory to inform a scheduled finish time!")
	@Pattern(regexp = "^\\d\\d:\\d\\d$",
			message = "Scheduled finish time should be formatted as 'HH:mm'")
	private String scheduledFinishTime;
	
	@NotBlank(message = "It's mandatory to inform a place for practice with 4 characters or more!")
	@Size(min = 4, max = 255)
	private String placeName;
	
	@NotBlank(message = "It's mandatory to inform a sport for practice!")
	@Size(min = 4, max = 255)
	private String sportName;
	
	// Organizar depois:
	// private Integer creatorId;
	
	public SchedulingDTO() {
		
	}
	
	public SchedulingDTO(Integer id, String scheduledDate, String scheduledStartTime, String scheduledFinishTime, String placeName, String sportName) {
		this.id = id;
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

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getScheduledStartTime() {
		return scheduledStartTime;
	}

	public void setScheduledStartTime(String scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}

	public String getScheduledFinishTime() {
		return scheduledFinishTime;
	}

	public void setScheduledFinishTime(String scheduledFinishTime) {
		this.scheduledFinishTime = scheduledFinishTime;
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
		return Objects.hash(id, placeName, scheduledDate, scheduledFinishTime, scheduledStartTime, sportName);
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
		return Objects.equals(id, other.id) && Objects.equals(placeName, other.placeName)
				&& Objects.equals(scheduledDate, other.scheduledDate)
				&& Objects.equals(scheduledFinishTime, other.scheduledFinishTime)
				&& Objects.equals(scheduledStartTime, other.scheduledStartTime)
				&& Objects.equals(sportName, other.sportName);
	}
}
