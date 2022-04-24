package br.edu.ifpb.dac.ssp.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.RuleViolationException;
import br.edu.ifpb.dac.ssp.exception.TimeAlreadyScheduledException;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;
import br.edu.ifpb.dac.ssp.util.Constants;

@Service
public class SchedulingValidatorService {
	
	// TODO: Montar validações para nomes de local e esporte (validar se existem no banco)
	
	private Validator validator;
	
	@Autowired
	private DateConverterService dateConverter;
	
	@Autowired
	private SchedulingService schedulingService;
	
	public SchedulingValidatorService() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public boolean validateSchedulingDTO(SchedulingDTO dto) throws Exception {
		Set<ConstraintViolation<SchedulingDTO>> violations = validator.validate(dto);
		
		if (violations.size() != 0) {
			throw new RuleViolationException(violations.stream().findFirst().get().getMessage());
		}
		
		if (!durationOfPracticeIsValid(dto.getDuration())) {
			throw new RuleViolationException("Duration of practice should be less than " + Constants.MAXIMUM_DURATION_PRACTICE + "!");
		}

		return true;
	}
	
	private boolean durationOfPracticeIsValid(String duration) throws Exception {
		LocalTime durationTime = dateConverter.stringToTime(duration);
		LocalTime maxDuration = dateConverter.stringToTime(Constants.MAXIMUM_DURATION_PRACTICE);
		
		return durationTime.isBefore(maxDuration);
	}	
	
	public boolean validateScheduling(Scheduling entity) throws Exception {
		Set<ConstraintViolation<Scheduling>> violations = validator.validate(entity);
		if (violations.size() != 0) {
			throw new RuleViolationException(violations.stream().findFirst().get().getMessage());
		}
		
		if (!entityScheduledDateIsValid(entity.getScheduledDate())) {
			throw new TimeAlreadyScheduledException();
		}
		
		return true;
	}
	
	private boolean entityScheduledDateIsValid(LocalDateTime scheduledDate) {
		return (!schedulingService.existsByScheduledDate(scheduledDate));
	}
}
