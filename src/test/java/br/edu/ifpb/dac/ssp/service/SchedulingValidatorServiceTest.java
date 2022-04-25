package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.exception.RuleViolationException;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;

public class SchedulingValidatorServiceTest {

	private static SchedulingValidatorService validatorService;
	private static Scheduling entity;
	private static SchedulingDTO dto;
	
	@BeforeAll
	public static void setUp() {
		validatorService = new SchedulingValidatorService();
		entity = new Scheduling();
		dto = new SchedulingDTO();
	}
	
	@BeforeEach
	public void setUpBeforeEach() {
		System.out.println("Setting attributtes for entity...");
		entity = new Scheduling();
		entity.setId(1);
		entity.setScheduledDate(LocalDate.parse("2022-05-01"));
		entity.setScheduledStartTime(LocalTime.parse("08:00"));
		entity.setScheduledFinishTime(LocalTime.parse("09:00"));
		entity.setPlaceName("Ginásio");
		entity.setSportName("Futebol");
		
		System.out.println("Setting attributtes for dto...");
		dto = new SchedulingDTO();
		dto.setId(1);
		dto.setScheduledDate("2022-05-01");
		dto.setScheduledStartTime("08:00");
		dto.setScheduledFinishTime("09:00");
		dto.setPlaceName("Ginásio");
		dto.setSportName("Futebol");
	}
	
	// Avaliar testValidateSchedulingOk() e testValidateSchedulingDtoOk() depois
	@Test
	public void testValidateSchedulingOk() {
		try {
			boolean isValid = validatorService.validateScheduling(entity);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	@Test
	public void testValidateSchedulingDtoOk() {
		try {
			boolean isValid = validatorService.validateSchedulingDTO(dto);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Esse precisa do placeService e do acesso ao banco
	@Test
	@Disabled
	public void testValidatePlaceNameOk() {
		
	}
	
	@Test
	@Disabled
	public void testValidatePlaceNameException() {
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> validatorService.validatePlaceName("Rua"));
		assertEquals("Could not find Place with name Rua", exception.getMessage());
	}
	
	// Esse precisa do sportService e do acesso ao banco
	@Test
	@Disabled
	public void testValidateSportNameOk() {
		
	}
	
	@Test
	@Disabled
	public void testValidateSportNameException() {
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> validatorService.validateSportName("Golfe"));
		assertEquals("Could not find Sport with name Golfe", exception.getMessage());
	}
	
	@Test
	public void testValidateScheduledTimeOk() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime finishTime = LocalTime.parse("09:00");
		
		try {
			boolean isValid = validatorService.validateScheduledTime(startTime, finishTime);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Adicionar mais valores depois para testar as bordas
	// Aqui quebra porque o método faz uso do DateConverterService
	@Test
	@Disabled
	public void testValidateScheduledTimeException() {
		LocalTime startTime = LocalTime.parse("06:00");
		LocalTime finishTime = LocalTime.parse("07:00");
		
		Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateScheduledTime(startTime, finishTime));
		assertEquals("Scheduled time should be between 07:00 and 22:00", exception.getMessage());
	}
	
	@Test
	public void testValidateDurationOfPracticeOk() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime finishTime = LocalTime.parse("09:00");
		
		try {
			boolean isValid = validatorService.validateDurationOfPractice(startTime, finishTime);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Adicionar mais valores depois para testar as bordas
	@Test
	public void testValidateDurationOfPracticeExceptionFinishTimeBeforeStartTime() {
		LocalTime startTime = LocalTime.parse("09:00");
		LocalTime finishTime = LocalTime.parse("08:00");
		
		Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateDurationOfPractice(startTime, finishTime));
		assertEquals("Duration of practice shouldn't be negative or 0!", exception.getMessage());
	}
	
	// Adicionar mais valores depois para testar as bordas
	@Test
	public void testValidateDurationOfPracticeExceptionMaximumDuration() {
		LocalTime startTime = LocalTime.parse("09:00");
		LocalTime finishTime = LocalTime.parse("20:00");
			
		Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateDurationOfPractice(startTime, finishTime));
		assertEquals("Duration of practice should be a maximum of 180 minutes!", exception.getMessage());
	}
	
	@Test
	public void testValidateScheduledDateOk() {
		try {
			boolean isValid = validatorService.validateScheduledDate(entity);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Esse também precisa do acesso ao banco, para comparar com os agendamentos salvos
	// Podemos adicionar vários valores para testar as bordas desse também
	@Test
	@Disabled
	public void testValidateScheduledDateException() {
		
	}
	
	
}
