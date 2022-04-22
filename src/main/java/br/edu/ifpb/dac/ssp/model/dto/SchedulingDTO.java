package br.edu.ifpb.dac.ssp.model.dto;
import br.edu.ifpb.dac.ssp.model.User;

import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

public class SchedulingDTO {

	// Aqui vão ficar os campos com validações do Spring Validation
	// Por enquanto a maioria está só com NotBlank 
	
	@FutureOrPresent(message = "Scheduled date shouldn't be in past")
	@NotBlank(message = "It's mandatory to inform a scheduled date!")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date scheduledDate;
	
	@DateTimeFormat(pattern = "HH:mm:ss")
	@NotBlank(message = "It's mandatory to inform a duration of practice!")
	private LocalTime duration;
	
	@NotBlank(message = "It's mandatory to inform a place for practice!")
	private Integer placeId;
	
	@NotBlank(message = "It's mandatory to inform a sport for practice!")
	private Integer sportId;
	
	/*
	 * Organizar melhor depois:
	@NotBlank
	private User creator;
	*/
	
	@NotBlank
	private Set<User> participants;
	
}
