package br.edu.ifpb.dac.ssp.service;

import java.time.LocalTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;
import br.edu.ifpb.dac.ssp.util.Constants;

@Service
public class SchedulingValidatorService {
	
	private Validator validator;
	
	@Autowired
	private DateConverterService dateConverter;
	
	public SchedulingValidatorService() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public boolean validateSchedulingDTO(SchedulingDTO dto) throws Exception {
		return (dtoFieldsAreValid(dto) && durationOfPracticeIsValid(dto.getDuration()));
	}

	private boolean dtoFieldsAreValid (SchedulingDTO dto) {
		Set<ConstraintViolation<SchedulingDTO>> violations = validator.validate(dto);
		
		return (violations.size() == 0);	
	}
	
	private boolean durationOfPracticeIsValid(String duration) throws Exception {
		LocalTime durationTime = dateConverter.stringToTime(duration);
		LocalTime maxDuration = dateConverter.stringToTime(Constants.MAXIMUM_DURATION_PRACTICE);
		
		return durationTime.isBefore(maxDuration);
	}	
	
	public boolean validateScheduling(Scheduling entity) {
		return entityFieldsAreValid(entity);
	}
	
	private boolean entityFieldsAreValid(Scheduling entity) {
		Set<ConstraintViolation<Scheduling>> violations = validator.validate(entity);
		
		return (violations.size() == 0);	
	}
}
