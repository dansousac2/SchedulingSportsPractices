package br.edu.ifpb.dac.ssp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

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

import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.dto.PlaceDTO;
import br.edu.ifpb.dac.ssp.repository.PlaceRepository;
import br.edu.ifpb.dac.ssp.service.PlaceConverterService;
import br.edu.ifpb.dac.ssp.service.PlaceService;


class PlaceControllerTest {

	@InjectMocks
	private PlaceController controller;
	@Mock
	private PlaceService service;
	@Mock
	private PlaceRepository repository;
	@Captor
	private ArgumentCaptor<Place> capPlace;
	private PlaceConverterService converter = new PlaceConverterService();
	private PlaceDTO exDto;
	private Place exPlace;
	private ResponseEntity respEntity;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(service, "placeRepository", repository);
		ReflectionTestUtils.setField(controller, "placeService",service);
		ReflectionTestUtils.setField(controller, "converterService", converter);
		
		exPlace = new Place();
		exPlace.setId(1);
		exPlace.setName("Campo de Voleibol");
		exPlace.setPublic(false);
		exPlace.setReference("Perto da Entrada");
		exPlace.setMaximumCapacityParticipants(20);
		
		exDto = new PlaceDTO();
		exDto.setId(1);
		exDto.setName("Campo de Voleibol");
		exDto.setPublic(false);
		exDto.setReference("Perto da Entrada");
		exDto.setMaximumCapacityParticipants(20);
	}
	
	@Test
	public void saveObjectInDb() {
		try {
			respEntity = controller.save(exDto);
			Mockito.verify(service).save(capPlace.capture());
			Place placeDB = capPlace.getValue();
			
			assertAll("Testing info of dto and entity saved in DB",
					() -> assertEquals(exDto.getId(), placeDB.getId()),
					() -> assertEquals(exDto.getName(), placeDB.getName())
			);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void saveStatusCreated() {
		
		try {
			Mockito.when(service.save(Mockito.any())).thenReturn(exPlace);
			respEntity = controller.save(exDto);
			
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
			respEntity = controller.save(exDto);
			String sResp = String.valueOf(respEntity.getBody()); // Exception is throwed and passed to de ResponseEntity 
			
			assertAll("Testing message and https of save method in fail case",
					() -> assertTrue(sResp.contains("name is missing") && sResp.contains("save")),
					() -> assertEquals(HttpStatus.BAD_REQUEST, respEntity.getStatusCode())
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateInDB() {
		try {
			respEntity = controller.update(1,exDto);
			Mockito.verify(service).update(capPlace.capture());
			Place placeDB = capPlace.getValue();
			
			assertEquals(exDto.getId(), placeDB.getId());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@Test
	public void updateFailIdNotFounded() {
		try {
			Mockito.when(service.update(exPlace)).thenThrow(new ObjectNotFoundException(exPlace.getId()));
			respEntity = controller.update(1, exDto); // body contains message error
			String s = String.valueOf(respEntity.getBody());
			assertAll("Test message and https in fail case",
					() -> assertTrue(s.contains("Could not find object with id 1")),
					() -> assertEquals(HttpStatus.BAD_REQUEST, respEntity.getStatusCode())
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateHttpsStatusOK() {
		try {
			Mockito.when(service.update(Mockito.any())).thenReturn(exPlace);
			respEntity = controller.update(1,exDto);
			
			assertEquals(HttpStatus.OK, respEntity.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Disabled
	public void updateSomeAtributeNotMustBeShown() {
		// verify business rule in the ConverterService
	}
	
	@Test
	public void deleteHttpStatusAndBody() {
		respEntity = controller.delete(1);
		
		assertAll("Test body and https in fail case",
				() -> assertEquals(HttpStatus.NO_CONTENT, respEntity.getStatusCode()),
				() -> assertEquals(null, respEntity.getBody())
		);
	}
	
	@Test
	public void findByIdHttpStatus() {
		try {
			Mockito.when(service.findById(1)).thenReturn(exPlace);
			respEntity = controller.findById(1);
			
			assertEquals(HttpStatus.OK, respEntity.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findByIdNotFounded() {
		try {
			Mockito.when(service.findById(1)).thenCallRealMethod();
			Mockito.when(repository.existsById(1)).thenReturn(false);
			respEntity = controller.findById(1); // with message error of ObjectnotFoundException

			assertAll("Test when id is not founded",
					() -> assertTrue(respEntity.getBody().equals("Could not find object with id 1")),
					() -> assertEquals(HttpStatus.BAD_REQUEST, respEntity.getStatusCode())
			);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void getAllHttpStatusOK() {
		try {
			List<Place> list = new ArrayList<>();
			for(int i = 1; i < 4; i++) {
				String nameComp = String.valueOf(i);
				exPlace = new Place();
				exPlace.setId(i);
				exPlace.setName("lugar" + nameComp);
				exPlace.setPublic(true);
				list.add(exPlace);
			}
			Mockito.when(service.findAll()).thenReturn(list);
			respEntity = controller.getAll();
			
			assertEquals(HttpStatus.OK, respEntity.getStatusCode());
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
