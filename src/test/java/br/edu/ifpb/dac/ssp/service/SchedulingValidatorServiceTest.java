package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.exception.RuleViolationException;
import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.Sport;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;

public class SchedulingValidatorServiceTest {
	
	@Mock
	private DateConverterService dateConverter;
	
	@Mock
	private SchedulingService schedulingService;
	
	@Mock
	private PlaceService placeService;
	
	@Mock
	private SportService sportService;
	
	@InjectMocks
	@Spy
	private static SchedulingValidatorService validatorService;
	
	@BeforeEach
	public void setUpBeforeEach() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testValidateSchedulingOk() {
		Scheduling entityMocked = mock(Scheduling.class);
		
		try {
			when(validatorService.validateScheduling(any())).thenReturn(true);
			
			boolean isValid = validatorService.validateScheduling(entityMocked);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	@Test
	public void testValidateSchedulingDtoOk() {
		SchedulingDTO dtoMocked = mock(SchedulingDTO.class);
		
		try {
			when(validatorService.validateScheduling(any())).thenReturn(true);
			
			boolean isValid = validatorService.validateSchedulingDTO(dtoMocked);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testValidatePlaceNameOk() {
		when(placeService.existsByName(anyString())).thenReturn(true);
		
		try {
			boolean isValid = validatorService.validatePlaceName("Pátio");
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testValidatePlaceNameException() {
		when(placeService.existsByName(anyString())).thenReturn(false);
		
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> validatorService.validatePlaceName("Pátio"));
		assertEquals("Could not find Place with name Pátio", exception.getMessage());
	}
	
	@Test
	public void testValidateSportNameOk() {
		when(sportService.existsByName(anyString())).thenReturn(true);
		
		try {
			boolean isValid = validatorService.validateSportName("Futebol");
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testValidateSportNameException() {
		when(sportService.existsByName(anyString())).thenReturn(false);
		
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> validatorService.validateSportName("Golfe"));
		assertEquals("Could not find Sport with name Golfe", exception.getMessage());
	}
	
	@Test
	public void testValidateScheduledTimeOk() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime finishTime = LocalTime.parse("09:00");
		
		try {
			when(validatorService.validateScheduledTime(any(LocalTime.class), any(LocalTime.class))).thenReturn(true);
			
			boolean isValid = validatorService.validateScheduledTime(startTime, finishTime);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	// Esse não consegui mockar muita coisa :(
	public void testValidateScheduledTimeException() {
		LocalTime startTime = LocalTime.parse("05:00");
		LocalTime finishTime = LocalTime.parse("06:00");
		
		try {
			when(dateConverter.stringToTime(anyString())).thenCallRealMethod();
			
			Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateScheduledTime(startTime, finishTime));
			
			assertEquals("Scheduled time should be between 07:00 and 22:00", exception.getMessage());		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Ainda falta montar com mock esses:
	
	@Test
	public void testValidateScheduledDateAndTimeOk() {
		
	}
	
	@Test
	public void testValidateScheduledDateAndTimeException() {
		
	}
	
	@Test
	public void testValidateDurationOfPracticeOk() {
		
	}
	

	@Test
	public void testValidateDurationOfPracticeExceptionFinishTimeBeforeStartTime() {
		
	}
	
	
	@Test
	public void testValidateDurationOfPracticeExceptionMaximumDuration() {
		
	}
	
	@Test
	public void testValidateScheduledDateOk() {
	
	}
	
	@Test
	public void testValidateScheduledDateException() {
		
	}
	
	
}
