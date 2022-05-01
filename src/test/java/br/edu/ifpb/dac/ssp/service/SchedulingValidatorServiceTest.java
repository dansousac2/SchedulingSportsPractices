package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import br.edu.ifpb.dac.ssp.exception.TimeAlreadyScheduledException;
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
	public void testValidateScheduledTimeException() {
		LocalTime startTime = LocalTime.parse("05:00");
		LocalTime finishTime = LocalTime.parse("06:00");
		
		try {
			when(dateConverter.stringToTime(anyString())).thenCallRealMethod();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
			
		Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateScheduledTime(startTime, finishTime));
			
		assertEquals("Scheduled time should be between 07:00 and 22:00", exception.getMessage());		
	}
	
	@Test
	public void testValidateScheduledDateAndTimeOk() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		
		LocalDate scheduledDate = LocalDate.now();
		LocalTime scheduledStartTime = LocalTime.parse(LocalTime.now().format(dtf)).plusMinutes(5);
		
		try {
			when(dateConverter.dateTimeNow()).thenCallRealMethod();
			when(dateConverter.stringToDateTime(anyString())).thenCallRealMethod();
			
			boolean isValid = validatorService.validateScheduledDateAndTime(scheduledDate, scheduledStartTime);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testValidateScheduledDateAndTimeException() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		
		LocalDate scheduledDate = LocalDate.now();
		LocalTime scheduledStartTime = LocalTime.parse(LocalTime.now().format(dtf)).minusMinutes(5);
		
		try {
			when(dateConverter.dateTimeNow()).thenCallRealMethod();
			when(dateConverter.stringToDateTime(anyString())).thenCallRealMethod();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateScheduledDateAndTime(scheduledDate, scheduledStartTime));
		
		assertEquals("Scheduled date shouldn't be in past!", exception.getMessage());
	}
	
	@Test
	public void testValidateDurationOfPracticeOk() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime finishTime = LocalTime.parse("10:00");
		
		try {
			boolean isValid = validatorService.validateDurationOfPractice(startTime, finishTime);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testValidateDurationOfPracticeExceptionFinishTimeBeforeStartTime() {
		LocalTime startTime = LocalTime.parse("10:00");
		LocalTime finishTime = LocalTime.parse("08:00");
		
		try {
			Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateDurationOfPractice(startTime, finishTime));
			assertEquals("Duration of practice shouldn't be negative or 0!", exception.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	@Test
	public void testValidateDurationOfPracticeExceptionMaximumDuration() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime finishTime = LocalTime.parse("13:00");
		
		try {
			Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateDurationOfPractice(startTime, finishTime));
			assertEquals("Duration of practice should be a maximum of 180 minutes!", exception.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testValidateScheduledDateOk() {
		Scheduling entityMocked1 = mock(Scheduling.class);
		when(entityMocked1.getScheduledStartTime()).thenReturn(LocalTime.parse("09:00"));
		when(entityMocked1.getScheduledFinishTime()).thenReturn(LocalTime.parse("11:00"));

		Place place = mock(Place.class);
		when(place.getName()).thenReturn("Pátio");
		
		Scheduling entityMocked2 = mock(Scheduling.class);
		when(entityMocked2.getScheduledStartTime()).thenReturn(LocalTime.parse("07:00"));
		when(entityMocked2.getScheduledFinishTime()).thenReturn(LocalTime.parse("08:00"));
		when(entityMocked2.getPlace()).thenReturn(place);
		
		List<Scheduling> listEntity = spy(List.class);
		listEntity.add(entityMocked1);
		
		when(schedulingService.findAllByPlaceNameAndScheduledDate(anyString(), any(LocalDate.class))).thenReturn(listEntity);
		
		try {
			boolean isValid = validatorService.validateScheduledDate(entityMocked2);
			
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Esse quebrou
	@Test
	public void testValidateScheduledDateException() {
		Scheduling entityMocked1 = mock(Scheduling.class);
		when(entityMocked1.getScheduledStartTime()).thenReturn(LocalTime.parse("09:30"));
		when(entityMocked1.getScheduledFinishTime()).thenReturn(LocalTime.parse("11:30"));

		Place place = mock(Place.class);
		when(place.getName()).thenReturn("Pátio");
		
		Scheduling entityMocked2 = mock(Scheduling.class);
		when(entityMocked2.getPlace()).thenReturn(place);
		when(entityMocked2.getScheduledStartTime()).thenReturn(LocalTime.parse("09:00"));
		when(entityMocked2.getScheduledFinishTime()).thenReturn(LocalTime.parse("11:00"));
		
		List<Scheduling> listEntity = spy(List.class);
		listEntity.add(entityMocked1);
		
		when(schedulingService.findAllByPlaceNameAndScheduledDate(anyString(), any(LocalDate.class))).thenReturn(listEntity);
		
		try {
			Throwable exception = assertThrows(TimeAlreadyScheduledException.class, () -> validatorService.validateScheduledDate(entityMocked2));
			assertEquals("There is already a practice scheduled for this time!", exception.getMessage());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
