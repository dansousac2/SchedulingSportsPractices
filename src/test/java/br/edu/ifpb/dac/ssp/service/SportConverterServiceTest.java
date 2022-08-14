package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.edu.ifpb.dac.ssp.business.service.SportConverterService;
import br.edu.ifpb.dac.ssp.model.entity.Sport;
import br.edu.ifpb.dac.ssp.presentation.dto.SportDTO;

public class SportConverterServiceTest {

	private static SportConverterService converterService;
	private static Sport entity;
	private static SportDTO dto;
	
	
	@BeforeAll
	public static void setUp() {
		converterService = new SportConverterService();
		
		System.out.println("Setting attributtes for entity...");
		entity = new Sport();
		entity.setId(1);
		entity.setName("Basquete");
		
		System.out.println("Setting attributtes for dto...");
		dto = new SportDTO();
		dto.setId(1);
		dto.setName("Basquete");
	}
	
	@Test
	public void testConvertEntityToDto() {
		SportDTO dtoConverted = converterService.sportToDto(entity);
		assertAll("Testing comparing dto and entity field by field",
			() -> assertEquals(dtoConverted.getId(), entity.getId()),
			() -> assertEquals(dtoConverted.getName(), entity.getName())
		);
	}
	
	@Test
	public void testConvertDtoToEntity() {
		Sport entityConverted = converterService.dtoToSport(dto);
		assertAll("Testing comparing entity and dto field by field",
			() -> assertEquals(entityConverted.getId(), dto.getId()),
			() -> assertEquals(entityConverted.getName(), dto.getName())
		);
	}
	
	@Test
	public void testConvertEntityToDtoThrowsNullPointerException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.sportToDto(null));
		assertEquals("Não foi possível converter pois o objeto é nulo", exception.getMessage());
	}
	
	@Test
	public void testConvertDtoToEntityThrowsNullPointerException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.dtoToSport(null));
		assertEquals("Não foi possível converter pois o objeto é nulo", exception.getMessage());
	}
	
}
