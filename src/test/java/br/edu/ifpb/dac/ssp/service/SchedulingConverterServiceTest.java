package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.edu.ifpb.dac.ssp.business.service.DateConverterService;
import br.edu.ifpb.dac.ssp.business.service.PlaceService;
import br.edu.ifpb.dac.ssp.business.service.SchedulingConverterService;
import br.edu.ifpb.dac.ssp.business.service.SportService;
import br.edu.ifpb.dac.ssp.model.entity.Place;
import br.edu.ifpb.dac.ssp.model.entity.Scheduling;
import br.edu.ifpb.dac.ssp.model.entity.Sport;
import br.edu.ifpb.dac.ssp.presentation.dto.SchedulingDTO;

public class SchedulingConverterServiceTest {

	@Mock
	private DateConverterService dateConverter;
	
	@Mock
	private PlaceService placeService;
	
	@Mock
	private SportService sportService;
	
	@InjectMocks
	@Spy
	private static SchedulingConverterService converterService;
	
	private static Scheduling entity;
	private static SchedulingDTO dto;
	private static Optional<Place> place;
	private static Optional<Sport> sport;
	
	@BeforeAll
	public static void setUp() {
		place = Optional.of(new Place(1, "Ginásio", "Perto do estacionamento", 80, false));
		sport = Optional.of(new Sport(2, "Futebol"));
		
		System.out.println("Setting attributtes for entity...");
		entity = new Scheduling();
		entity.setId(1);
		entity.setScheduledDate(LocalDate.parse("2022-05-01"));
		entity.setScheduledStartTime(LocalTime.parse("08:00"));
		entity.setScheduledFinishTime(LocalTime.parse("09:00"));
		entity.setPlace(place.get());
		entity.setSport(sport.get());
		
		System.out.println("Setting attributtes for dto...");
		dto = new SchedulingDTO();
		dto.setId(1);
		dto.setScheduledDate("2022-05-01");
		dto.setScheduledStartTime("08:00");
		dto.setScheduledFinishTime("09:00");
		dto.setPlaceId(1);
		dto.setSportId(2);	
	}
	
	@BeforeEach
	public void setUpBeforeEach() {
		MockitoAnnotations.openMocks(this);

	}
	
	@Test
	public void testConvertEntityToDtoInstanceOf() {
		try {
			SchedulingDTO dtoConverted = converterService.schedulingToDto(entity);
			assertTrue(dtoConverted instanceof SchedulingDTO);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testConvertDtoToEntityInstanceOf() {
		try {
			Scheduling entityConverted = converterService.dtoToScheduling(dto);
			
			assertTrue(entityConverted instanceof Scheduling);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	@Test
	public void testConvertEntityToDtoFields() {
		try {
			when(dateConverter.timeToString(any(LocalTime.class))).thenCallRealMethod();
			when(dateConverter.dateToString(any(LocalDate.class))).thenCallRealMethod();
			
			SchedulingDTO dtoConverted = converterService.schedulingToDto(entity);
			
			assertAll("Testing comparing dto and entity field by field",
					() -> assertEquals(dtoConverted.getId(), entity.getId()),
					() -> assertEquals(dtoConverted.getScheduledDate(), entity.getScheduledDate().toString()),
					() -> assertEquals(dtoConverted.getScheduledStartTime(), entity.getScheduledStartTime().toString()),
					() -> assertEquals(dtoConverted.getScheduledFinishTime(), entity.getScheduledFinishTime().toString()),
					() -> assertEquals(dtoConverted.getPlaceId(), entity.getPlace().getId()),
					() -> assertEquals(dtoConverted.getSportId(), entity.getSport().getId()));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testConvertDtoToEntityFields() {
		try {
			when(dateConverter.stringToTime(anyString())).thenCallRealMethod();
			when(dateConverter.stringToDate(anyString())).thenCallRealMethod();
			
			when(placeService.findById(anyInt())).thenReturn(place.get());
			when(sportService.findById(anyInt())).thenReturn(sport.get());
			
			Scheduling entityConverted = converterService.dtoToScheduling(dto);
			
			assertAll("Testing comparing dto and entity field by field",
					() -> assertEquals(entityConverted.getId(), dto.getId()),
					() -> assertEquals(entityConverted.getScheduledDate().toString(), dto.getScheduledDate()),
					() -> assertEquals(entityConverted.getScheduledStartTime().toString(), dto.getScheduledStartTime()),
					() -> assertEquals(entityConverted.getScheduledFinishTime().toString(), dto.getScheduledFinishTime()),
					() -> assertEquals(entityConverted.getPlace().getId(), dto.getPlaceId()),
					() -> assertEquals(entityConverted.getSport().getId(), dto.getSportId()));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testConvertEntityToDtoThrowsNullPointerException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.schedulingToDto(null));
		assertEquals("Não foi possível converter pois o objeto é nulo", exception.getMessage());
	}
	
	@Test
	public void testConvertDtoToEntityThrowsNullPointerException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.dtoToScheduling(null));
		assertEquals("Não foi possível converter pois o objeto é nulo", exception.getMessage());
	}
}
