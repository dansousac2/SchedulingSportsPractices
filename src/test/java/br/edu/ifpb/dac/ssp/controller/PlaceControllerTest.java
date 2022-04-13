package br.edu.ifpb.dac.ssp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;	
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.dto.PlaceDTO;
import br.edu.ifpb.dac.ssp.repository.PlaceRepository;
import br.edu.ifpb.dac.ssp.service.PlaceConverterService;
import br.edu.ifpb.dac.ssp.service.PlaceService;

class PlaceControllerTest {

	@InjectMocks
	@Autowired
	private PlaceController controller;
	@Mock
	private PlaceService service;
	@Mock
	private PlaceRepository repository;
	@Mock
	private PlaceConverterService converter;
	private PlaceDTO dto;
	@Captor
	private ArgumentCaptor<Place> capPlace;
	
	@BeforeEach
	public void dtoLauncher() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(controller, "converterService", converter);
		ReflectionTestUtils.setField(service,"placeRepository",repository);
		ReflectionTestUtils.setField(controller,"placeService",service);
		dto = new PlaceDTO();
		dto.setId(1);
		dto.setName("Quadra Esportiva");
		dto.setPublic(false);
		dto.setMaximumCapacityParticipants(20);
		dto.setReference("Próximo à entrada");
	}
	
	/*
	 * Comparações a serem feitas:
	 * 1-objeto no banco e objeto criado são o mesmo
	 * 2-Https = ok
	 * 3-dto não passa dados sensíveis
	 * são individuais oriundas do mesmo "save" por isso acredito que possam estar no mesmo teste
	 */
	
	@Test
	public void saveOK() {
		// Mockito.when in first, when possible.         
		Mockito.when(converter.dtoToPlace(Mockito.any(PlaceDTO.class))).thenCallRealMethod();
		ResponseEntity respEntity = controller.save(dto);
		Mockito.verify(service).save(capPlace.capture());
		Mockito.when(repository.save(Mockito.any())).thenReturn(capPlace.getValue());
		Place sportInDB = capPlace.getValue();
		
		assertEquals(dto.getName(), sportInDB.getName());
		if(!(dto.getReference() == null)) {
			assertEquals(dto.getReference(), sportInDB.getReference());
		}
		assertEquals(HttpStatus.CREATED, respEntity.getStatusCode());
//		assertEquals(null,respEntity.getBody().getDadosSensiveis01());
	}

}
