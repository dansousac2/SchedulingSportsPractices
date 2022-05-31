package br.edu.ifpb.dac.ssp.model.dto;

import java.time.LocalTime;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	
	@NotNull(message = "\"Place ID\" not must be null!")
	private Integer placeId;
	
	@NotNull(message = "\"Sport ID\" not must be null!")
	private Integer sportId;
	
	public SchedulingDTO() {
		
	}
	
	public SchedulingDTO(Integer id, String scheduledDate, String scheduledStartTime, String scheduledFinishTime, LocalTime startTime,  
			Integer placeId, Integer sportId) {
		this.id = id;
		this.scheduledDate = scheduledDate;
		this.scheduledStartTime = scheduledStartTime;
		this.scheduledFinishTime = scheduledFinishTime;
		this.placeId = placeId;
		this.sportId = sportId;
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

	@Override
	public int hashCode() {
		return Objects.hash(id, placeId, scheduledDate, scheduledFinishTime, scheduledStartTime, sportId);
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
		return Objects.equals(id, other.id) && Objects.equals(placeId, other.placeId)
				&& Objects.equals(scheduledDate, other.scheduledDate)
				&& Objects.equals(scheduledFinishTime, other.scheduledFinishTime)
				&& Objects.equals(scheduledStartTime, other.scheduledStartTime)
				&& Objects.equals(sportId, other.sportId);
	}
}
