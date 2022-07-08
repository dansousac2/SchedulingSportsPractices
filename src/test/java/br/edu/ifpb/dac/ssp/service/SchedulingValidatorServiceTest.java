package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
	public void testValidateEntityAndDtoOk() {
		Scheduling entityMocked = mock(Scheduling.class);
		SchedulingDTO dtoMocked = mock(SchedulingDTO.class);
		
		try {
			when(validatorService.validateScheduling(any())).thenReturn(true);
			when(validatorService.validateSchedulingDTO(any())).thenReturn(true);
			
			assertTrue(validatorService.validateSchedulingDTO(dtoMocked));
			assertTrue(validatorService.validateScheduling(entityMocked));
			
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
	public void testValidatePlaceIdOk() {
		when(placeService.existsById(anyInt())).thenReturn(true);
		
		try {
			boolean isValid = validatorService.validPlaceId(2);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	@Test
	public void testValidatePlaceIdException() {
		when(placeService.existsById(anyInt())).thenReturn(false);
		
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> validatorService.validPlaceId(12));
		assertEquals("Não foi encontrado local com id 12", exception.getMessage());
	}
	
	@Test
	public void testValidateSportIdOk() {
		when(sportService.existsById(anyInt())).thenReturn(true);
		
		try {
			boolean isValid = validatorService.validSportId(5);
			assertTrue(isValid);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testValidateSportIdException() {
		when(sportService.existsById(anyInt())).thenReturn(false);
		
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> validatorService.validSportId(7));
		assertEquals("Não foi encontrado esporte com id 7", exception.getMessage());
	}
	
	@Test
	public void testValidateScheduledTimeOk() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime finishTime = LocalTime.parse("09:00");
		
		try {
			when(validatorService.validateScheduledTime(any(LocalTime.class), any(LocalTime.class))).thenReturn(true);
				
			assertTrue(validatorService.validateScheduledTime(startTime, finishTime));
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
			fail();
		}
			
		Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateScheduledTime(startTime, finishTime));	
		assertEquals("O horário da prática deve ser entre 07:00 e 22:00", exception.getMessage());		
	}
	
	@Test
	public void testValidateScheduledDateAndTimeOk() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		
		LocalTime scheduledStartTime = LocalTime.parse(LocalTime.now().format(dtf)).plusMinutes(5);
		LocalDate scheduledDate = LocalDate.now();
		
		try {
			when(dateConverter.dateTimeNow()).thenCallRealMethod();
			when(dateConverter.stringToDateTime(anyString())).thenCallRealMethod();
			
			assertTrue(validatorService.validateScheduledDateAndTime(scheduledDate, scheduledStartTime));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	@Test
	public void testValidateScheduledDateAndTimeException() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		
		LocalTime scheduledStartTime = LocalTime.parse(LocalTime.now().format(dtf)).minusMinutes(5);
		LocalDate scheduledDate = LocalDate.now();
		try {
			when(dateConverter.dateTimeNow()).thenCallRealMethod();
			when(dateConverter.stringToDateTime(anyString())).thenCallRealMethod();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
		
		Throwable exception = assertThrows(RuleViolationException.class, () -> validatorService.validateScheduledDateAndTime(scheduledDate, scheduledStartTime));
		assertEquals("A data da prática não pode estar no passado!", exception.getMessage());
	}
	
	@Test
	public void testValidateDurationOfPracticeOk() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime finishTime = LocalTime.parse("10:00");
		
		try {
			assertTrue(validatorService.validateDurationOfPractice(startTime, finishTime));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	@Test
	public void testValidateDurationOfPracticeException() {
		LocalTime startTime = LocalTime.parse("08:00");
		LocalTime finishTime = LocalTime.parse("13:00");
		
		try {
			// Finish time before start time
			Throwable exceptionNegative = assertThrows(RuleViolationException.class, () -> validatorService.validateDurationOfPractice(finishTime, startTime));
			assertEquals("A duração da prática não deve ser igual ou menor que 0 minutos!", exceptionNegative.getMessage());
			
			// Duration above maximum
			Throwable exceptionMaximum = assertThrows(RuleViolationException.class, () -> validatorService.validateDurationOfPractice(startTime, finishTime));
			assertEquals("A prática agendada deve ter no máximo 180 minutos!", exceptionMaximum.getMessage());
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
		
		when(schedulingService.findAllByPlaceIdAndScheduledDate(anyInt(), any(LocalDate.class))).thenReturn(listEntity);
		
		try {
			assertTrue(validatorService.validateScheduledDate(entityMocked2));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
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
		when(entityMocked2.getScheduledDate()).thenReturn(LocalDate.parse("2022-12-30"));
		
		List<Scheduling> listEntity = new ArrayList<>();
		listEntity.add(entityMocked1);
		
		when(schedulingService.findAllByPlaceIdAndScheduledDate(anyInt(), any(LocalDate.class))).thenReturn(listEntity);
		
		try {
			Throwable exception = assertThrows(TimeAlreadyScheduledException.class, () -> validatorService.validateScheduledDate(entityMocked2));
			assertEquals("Já existe uma prática agendada para esse horário!", exception.getMessage());
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
