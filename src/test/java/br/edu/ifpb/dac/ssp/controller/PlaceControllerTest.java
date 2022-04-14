package br.edu.ifpb.dac.ssp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.dto.PlaceDTO;
import br.edu.ifpb.dac.ssp.service.PlaceConverterService;
import br.edu.ifpb.dac.ssp.service.PlaceService;

class PlaceControllerTest {

	@InjectMocks
	private PlaceController controller;
	@Mock
	private PlaceService service;
	private PlaceConverterService converter = new PlaceConverterService();
	private PlaceDTO exDto;
	private Place exPlace;
	@Captor
	private ArgumentCaptor<Place> capPlace;
	
	@BeforeEach
	public void dtoLauncher() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(controller, "converterService", converter);
		ReflectionTestUtils.setField(controller, "placeService",service);
		
		exPlace = new Place();
		exPlace.setId(1);
		exPlace.setName("Campo de Voleibol");
		exPlace.setPublic(false);
		exPlace.setReference("Perto da Entrada");
		exPlace.setMaximumCapacityParticipants(20);
		
		exDto = new PlaceDTO();
		exDto.setId(1);
		exDto.setName("Quadra Esportiva");
		exDto.setPublic(false);
		exDto.setMaximumCapacityParticipants(20);
		exDto.setReference("Próximo à entrada");
	}
	
	/*
	 * Comparações a serem feitas:
	 * 1-objeto no banco e objeto criado são o mesmo
	 * 2-Https = ok
	 * 3-dto não passa dados sensíveis
	 * são individuais oriundas do mesmo "save" por isso acredito que possam estar no mesmo teste
	 */
	
	/*
	 * Object in DB have the same atributes (id and name, for example) as the 
	 * DTO of the parameter's method
	 */
	@Test
	public void saveObjectInDb() {
		ResponseEntity respEntity = controller.save(exDto);
		try {
			Mockito.verify(service).save(capPlace.capture());
			Place placeDB = capPlace.getValue();
			assertEquals(exDto.getId(), placeDB.getId());
			assertEquals(exDto.getName(), placeDB.getName());
			if(!(exDto.getReference() == null)) {
				assertEquals(exDto.getReference(), placeDB.getReference());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void saveStatusCreated() {
		
		try {
			Mockito.when(service.save(Mockito.any())).thenReturn(exPlace);
			ResponseEntity respEntity = controller.save(exDto);
			assertEquals(HttpStatus.CREATED, respEntity.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Disabled
	public void saveSomeAtributeNotMustBeShown() {
		// verify business rule in the ConverterService
	}
	
	@Test
	public void saveFailWithoutName() {
		exDto.setName(null);
		try {
			Mockito.when(service.save(Mockito.any())).thenCallRealMethod();
			ResponseEntity resp = controller.save(exDto);
			String sResp = String.valueOf(resp.getBody());
			
			assertTrue(sResp.contains("name is missing") && sResp.contains("save"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
