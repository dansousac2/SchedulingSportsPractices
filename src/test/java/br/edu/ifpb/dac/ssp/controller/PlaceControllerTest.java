package br.edu.ifpb.dac.ssp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;	
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.dto.PlaceDTO;
import br.edu.ifpb.dac.ssp.service.PlaceService;

class PlaceControllerTest {
/*
	@InjectMocks
	private PlaceController controller = new PlaceController();
	@Mock
	private PlaceService service;
	private PlaceDTO dto;
	
	private ArgumentCaptor<Place> capPlace;
	
	@BeforeAll
	public void beforeAllDo() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(controller,"placeService",service);
	}
	
	@BeforeEach
	public void dtoLauncher() {
		dto = new PlaceDTO();
		dto
		// realizar sets no dto
	}
	
	/*
	 * Comparações a serem feitas:
	 * 1-objeto no banco e objeto criado são o mesmo
	 * 2-Https = ok
	 * 3-dto não passa dados sensíveis
	 * são individuais oriundas do mesmo "save" por isso acredito que possam estar no mesmo teste
	 */
	
	/*
	@Test
	public void saveOK() {
		Mockito.verify(service).save(capPlace.capture());
		ResponseEntity respEntity = controller.save(dto);
		Place sportInDB = capPlace.getValue();
		
		assertEquals(dto.getName(), sportInDB.getName());
		if(!(dto.getReference() == null)) {
			assertEquals(dto.getReference(), sport.getReference());
		}
		assertEquals(HttpStatus.CREATED, respEntity.getStatusCode());
		assertEquals(null,respEntity.getBody().getDadosSensiveis01());
		assertEquals(null,respEntity.getBody().getDadosSensiveis02());
	}
	*/

}
