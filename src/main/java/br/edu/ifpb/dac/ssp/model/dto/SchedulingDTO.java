package br.edu.ifpb.dac.ssp.model.dto;
import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.Sport;
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
	
	@FutureOrPresent
	@NotBlank
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date scheduledDate;
	
	@NotBlank
	private LocalTime duration;
	
	@NotBlank
	private Place place;
	
	@NotBlank
	private Sport sport;
	
	/*
	 * Organizar melhor depois:
	@NotBlank
	private User creator;
	*/
	
	@NotBlank
	private Set<User> participants;
	
}
