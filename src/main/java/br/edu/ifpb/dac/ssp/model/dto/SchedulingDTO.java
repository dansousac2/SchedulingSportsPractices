package br.edu.ifpb.dac.ssp.model.dto;
import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.Sport;

import java.time.LocalTime;
import java.util.Date;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

public class SchedulingDTO {

	// Aqui vão ficar os campos com validações do Spring Validation
	@FutureOrPresent
	@NotBlank
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date scheduledDate;
	
	@NotBlank
	private LocalTime durationOfPractice;
	
	@NotBlank
	private Place place;
	
	@NotBlank
	private Sport sport;
	
	// ...
}
