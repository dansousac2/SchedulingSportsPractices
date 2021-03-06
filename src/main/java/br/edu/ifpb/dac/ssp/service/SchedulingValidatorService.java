package br.edu.ifpb.dac.ssp.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.exception.RuleViolationException;
import br.edu.ifpb.dac.ssp.exception.TimeAlreadyScheduledException;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;

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
	
	@Value("${practice.maximum-duration}")
	private String maximumDurationPracticeTime;
	
	@Value("${institution.opening-time}")
	private String institutionOpeningTime;
	
	@Value("${institution.closing-time}")
	private String institutionClosingTime;
	
	public SchedulingValidatorService() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public boolean validateSchedulingDTO(SchedulingDTO dto) throws Exception {
		Set<ConstraintViolation<SchedulingDTO>> violations = validator.validate(dto);
		
		if (violations.size() != 0) {
			throw new RuleViolationException(violations.stream().findFirst().get().getMessage());
		}
		
		LocalDate scheduledDate = dateConverter.stringToDate(dto.getScheduledDate());
		LocalTime scheduledStartTime = dateConverter.stringToTime(dto.getScheduledStartTime());
		LocalTime scheduledFinishTime = dateConverter.stringToTime(dto.getScheduledFinishTime());
		
		validPlaceId(dto.getPlaceId());
		validSportId(dto.getSportId());
		validateScheduledDateAndTime(scheduledDate, scheduledStartTime);
		validateScheduledTime(scheduledStartTime, scheduledFinishTime);
		validateDurationOfPractice(scheduledStartTime, scheduledFinishTime);
		
		return true;
	}
		
	
	public boolean validateScheduling(Scheduling entity) throws Exception {
		Set<ConstraintViolation<Scheduling>> violations = validator.validate(entity);
		
		if (violations.size() != 0) {
			throw new RuleViolationException(violations.stream().findFirst().get().getMessage());
		}
		
		validPlaceId(entity.getPlace().getId());
		validSportId(entity.getSport().getId());
		validateScheduledDateAndTime(entity.getScheduledDate(), entity.getScheduledStartTime());
		validateScheduledDate(entity);
		validateScheduledTime(entity.getScheduledStartTime(), entity.getScheduledFinishTime());
		validateDurationOfPractice(entity.getScheduledStartTime(), entity.getScheduledFinishTime());
		
		return true;
	}
	
	public boolean validPlaceId(Integer id) throws Exception {
		if (!placeService.existsById(id)) {
			throw new ObjectNotFoundException("local", "id",  id);
		}
		
		return true;
	}
	
	public boolean validSportId(Integer id) throws Exception {
		if (!sportService.existsById(id)) {
			throw new ObjectNotFoundException("esporte", "id", id);
		}
		
		return true;
	}
	
	public boolean validateScheduledTime(LocalTime scheduledStartTime, LocalTime scheduledFinishTime) throws Exception {
		LocalTime openingTime = dateConverter.stringToTime(this.institutionOpeningTime);
		LocalTime closingTime = dateConverter.stringToTime(this.institutionClosingTime);
		
		if (scheduledStartTime.isBefore(openingTime) || scheduledFinishTime.isBefore(openingTime) ||
			scheduledStartTime.isAfter(closingTime) || scheduledFinishTime.isAfter(closingTime)) {
			throw new RuleViolationException("O hor??rio da pr??tica deve ser entre " + this.institutionOpeningTime + " e " + this.institutionClosingTime);
		}
		
		return true;
	}
	
	public boolean validateDurationOfPractice(LocalTime scheduledStartTime, LocalTime scheduledFinishTime) throws Exception {
		long maxDurationPractice = Long.valueOf(this.maximumDurationPracticeTime);
		Duration durationOfPractice = Duration.between(scheduledStartTime, scheduledFinishTime);
		
		if (durationOfPractice.toMinutes() <= 0) {
			throw new RuleViolationException("A dura????o da pr??tica n??o deve ser igual ou menor que 0 minutos!");
		} else if (durationOfPractice.toMinutes() > maxDurationPractice) {
			throw new RuleViolationException("A pr??tica agendada deve ter no m??ximo " + this.maximumDurationPracticeTime + " minutos!");
		}
		
		return true;
	}
	
	public boolean validateScheduledDate(Scheduling entity) throws Exception {
		List<Scheduling> entitiesWithSamePlaceAndDate = schedulingService.findAllByPlaceIdAndScheduledDate(entity.getPlace().getId(), entity.getScheduledDate());
		 
		if (!entitiesWithSamePlaceAndDate.isEmpty()) {
			LocalTime startTime;
			LocalTime finishTime;
			
			// Checando se hor??rios de pr??tica n??o colidem com hor??rios j?? agendados para determinado local
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
	
	public boolean validateScheduledDateAndTime(LocalDate scheduledDate, LocalTime scheduledStartTime) throws Exception {
		String scheduledDateTimeString = scheduledDate.toString() + " " + scheduledStartTime.toString();
		LocalDateTime scheduledDateTime = dateConverter.stringToDateTime(scheduledDateTimeString);
		
		LocalDateTime dateTimeNow = dateConverter.dateTimeNow();
		
		if (scheduledDateTime.isBefore(dateTimeNow)) {
			throw new RuleViolationException("A data da pr??tica n??o pode estar no passado!");
		}
		
		return true;
	}
}
