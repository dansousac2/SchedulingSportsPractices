package br.edu.ifpb.dac.ssp.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.exception.RuleViolationException;
import br.edu.ifpb.dac.ssp.exception.TimeAlreadyScheduledException;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;
import br.edu.ifpb.dac.ssp.util.Constants;

@Service
public class SchedulingValidatorService {
	
	private Validator validator;
	
	@Autowired
	private DateConverterService dateConverter;
	
	@Autowired
	private SchedulingService schedulingService;
	
	@Autowired
	private PlaceService placeService;
	
	@Autowired 
	private SportService sportService;
	
	public SchedulingValidatorService() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public boolean validateSchedulingDTO(SchedulingDTO dto) throws Exception {
		Set<ConstraintViolation<SchedulingDTO>> violations = validator.validate(dto);
		
		if (violations.size() != 0) {
			throw new RuleViolationException(violations.stream().findFirst().get().getMessage());
		}
		
		LocalTime scheduledStartDate = dateConverter.stringToTime(dto.getScheduledStartTime());
		LocalTime scheduledFinishDate = dateConverter.stringToTime(dto.getScheduledFinishTime());
		
		validatePlaceName(dto.getPlaceName());
		validateSportName(dto.getSportName());
		validateScheduledTime(scheduledStartDate, scheduledFinishDate);
		validateDurationOfPractice(scheduledStartDate, scheduledFinishDate);
		
		return true;
	}
		
	
	public boolean validateScheduling(Scheduling entity) throws Exception {
		Set<ConstraintViolation<Scheduling>> violations = validator.validate(entity);
		
		if (violations.size() != 0) {
			throw new RuleViolationException(violations.stream().findFirst().get().getMessage());
		}
		
		validatePlaceName(entity.getPlace().getName());
		validateSportName(entity.getSport().getName());
		validateScheduledDate(entity);
		validateScheduledTime(entity.getScheduledStartTime(), entity.getScheduledFinishTime());
		validateDurationOfPractice(entity.getScheduledStartTime(), entity.getScheduledFinishTime());
		
		return true;
	}
	
	public boolean validatePlaceName(String placeName) throws Exception {
		if (!placeService.existsByName(placeName)) {
			throw new ObjectNotFoundException("Place", "name",  placeName);
		}
		
		return true;
	}
	
	public boolean validateSportName(String sportName) throws Exception {
		if (!sportService.existsByName(sportName)) {
			throw new ObjectNotFoundException("Sport", "name", sportName);
		}
		
		return true;
	}
	
	public boolean validateScheduledTime(LocalTime scheduledStartTime, LocalTime scheduledFinishTime) throws Exception {
		LocalTime openingTime = dateConverter.stringToTime(Constants.INSTITUTION_OPENING_TIME);
		LocalTime closingTime = dateConverter.stringToTime(Constants.INSTITUTION_CLOSING_TIME);
		
		if (scheduledStartTime.isBefore(openingTime) || scheduledFinishTime.isBefore(openingTime) ||
			scheduledStartTime.isAfter(closingTime) || scheduledFinishTime.isAfter(closingTime)) {
			throw new RuleViolationException("Scheduled time should be between " + Constants.INSTITUTION_OPENING_TIME + " and " + Constants.INSTITUTION_CLOSING_TIME);
		}
		
		return true;
	}
	
	public boolean validateDurationOfPractice(LocalTime scheduledStartTime, LocalTime scheduledFinishTime) throws Exception {
		Duration durationOfPractice = Duration.between(scheduledStartTime, scheduledFinishTime);
		
		if (durationOfPractice.toMinutes() <= 0) {
			throw new RuleViolationException("Duration of practice shouldn't be negative or 0!");
		} else if (durationOfPractice.toMinutes() > Constants.MAXIMUM_DURATION_PRACTICE_MINUTES) {
			throw new RuleViolationException("Duration of practice should be a maximum of " + Constants.MAXIMUM_DURATION_PRACTICE_MINUTES + " minutes!");
		}
		
		return true;
	}
	
	public boolean validateScheduledDate(Scheduling entity) throws Exception {
		List<Scheduling> entitiesWithSamePlaceAndDate = schedulingService.findAllByPlaceNameAndScheduledDate(entity.getPlace().getName(), entity.getScheduledDate());
		 
		if (!entitiesWithSamePlaceAndDate.isEmpty()) {
			LocalTime startTime;
			LocalTime finishTime;
			
			// Checando se horários de prática não colidem com horários já agendados para determinado local
			for (Scheduling savedEntity: entitiesWithSamePlaceAndDate) {
				startTime = savedEntity.getScheduledStartTime(); 
				finishTime = savedEntity.getScheduledFinishTime(); 
				
				if (entity.getScheduledStartTime().plusSeconds(1).isAfter(startTime) && entity.getScheduledStartTime().minusSeconds(1).isBefore(finishTime) ||
					entity.getScheduledFinishTime().plusSeconds(1).isAfter(startTime) && entity.getScheduledFinishTime().minusSeconds(1).isBefore(finishTime) ||
					startTime.isAfter(entity.getScheduledStartTime()) && finishTime.isBefore(entity.getScheduledFinishTime())) {
					throw new TimeAlreadyScheduledException();
				}
			}
		}
		
		return true;
	}
}
