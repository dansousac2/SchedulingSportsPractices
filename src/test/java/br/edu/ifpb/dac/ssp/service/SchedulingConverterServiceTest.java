package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.Sport;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;

public class SchedulingConverterServiceTest {

	private static SchedulingConverterService converterService;
	private static Scheduling entity;
	private static SchedulingDTO dto;
	
	@BeforeAll
	public static void setUp() {
		converterService = new SchedulingConverterService();
		
		System.out.println("Setting attributtes for entity...");
		entity = new Scheduling();
		entity.setId(1);
		entity.setScheduledDate(LocalDate.parse("2022-05-01"));
		entity.setScheduledStartTime(LocalTime.parse("08:00"));
		entity.setScheduledFinishTime(LocalTime.parse("09:00"));
		entity.setPlace(new Place(1, "Ginásio", "Perto do estacionamento", 80, false));
		entity.setSport(new Sport(2, "Futebol"));
		
		System.out.println("Setting attributtes for dto...");
		dto = new SchedulingDTO();
		dto.setId(1);
		dto.setScheduledDate("2022-05-01");
		dto.setScheduledStartTime("08:00");
		dto.setScheduledFinishTime("09:00");
		dto.setPlaceName("Ginásio");
		dto.setSportName("Futebol");
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
			SchedulingDTO dtoConverted = converterService.schedulingToDto(entity);
			
			assertAll("Testing comparing dto and entity field by field",
					() -> assertEquals(dtoConverted.getId(), entity.getId()),
					() -> assertEquals(dtoConverted.getScheduledDate(), entity.getScheduledDate().toString()),
					() -> assertEquals(dtoConverted.getScheduledStartTime(), entity.getScheduledStartTime().toString()),
					() -> assertEquals(dtoConverted.getScheduledFinishTime(), entity.getScheduledFinishTime().toString()),
					() -> assertEquals(dtoConverted.getPlaceName(), entity.getPlace()),
					() -> assertEquals(dtoConverted.getSportName(), entity.getSport()));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testConvertDtoToEntityFields() {
		try {
			Scheduling entityConverted = converterService.dtoToScheduling(dto);
			
			assertAll("Testing comparing dto and entity field by field",
					() -> assertEquals(entityConverted.getId(), dto.getId()),
					() -> assertEquals(entityConverted.getScheduledDate().toString(), dto.getScheduledDate()),
					() -> assertEquals(entityConverted.getScheduledStartTime().toString(), dto.getScheduledStartTime()),
					() -> assertEquals(entityConverted.getScheduledFinishTime().toString(), dto.getScheduledFinishTime()),
					() -> assertEquals(entityConverted.getPlace(), dto.getPlaceName()),
					() -> assertEquals(entityConverted.getSport(), dto.getSportName()));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testConvertEntityToDtoThrowsNullPointerException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.schedulingToDto(null));
		assertEquals("Could not convert because object is null", exception.getMessage());
	}
	
	@Test
	public void testConvertDtoToEntityThrowsNullPointerException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.dtoToScheduling(null));
		assertEquals("Could not convert because object is null", exception.getMessage());
	}
}
