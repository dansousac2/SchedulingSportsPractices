package br.edu.ifpb.dac.ssp.service;

import br.edu.ifpb.dac.ssp.service.PlaceService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.edu.ifpb.dac.ssp.exception.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.repository.PlaceRepository;

public class PlaceServiceTest {

	@InjectMocks
	private PlaceService placeService;
	@Mock
	private PlaceRepository repository;
	private static Place entity = new Place();
	
	@BeforeEach
	public void beforeEach() {
		System.out.println("Initializing classes...");
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(placeService, "placeRepository", repository);
		
		System.out.println("Setting attributtes for Place...");
		entity.setId(1);
		entity.setName("Ginásio");
		entity.setPublic(false);
		entity.setReference("Perto do estacionamento");
		entity.setMaximumCapacityParticipants(80);
		
	}
	
	@Test
	@Order(1)
	public void testFindByIdThrowsObjectNotFoundException() {
		// Testing findById by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> placeService.findById(25));
		assertEquals("Could not find object with id 25", exception.getMessage());
	}
	
	@Test
	@Order(2)
	public void testFindByNameThrowsMissingFieldException() {
		// Testing findByName by passing a null value...
		Throwable exception = assertThrows(MissingFieldException.class, () -> placeService.findByName(null));
		assertEquals("Could not complete action, the field name is missing!", exception.getMessage());
	}
	
	@Test
	@Order(3)
	public void testFindByNameThrowsObjectNotFoundException() {
		// Testing findByName by passing an invalid name...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> placeService.findByName("Pátio"));
		assertEquals("Could not find object with name Pátio", exception.getMessage());
	}
	
	@Test
	@Order(4)
	public void testSaveThrowsMissingFieldException() {
		entity.setName(null);
		
		// Testing save by passing a null value for name...
		Throwable exception = assertThrows(MissingFieldException.class, () -> placeService.save(entity));
		assertEquals("Could not save, the field name is missing!", exception.getMessage());
	}
	
	@Test
	@Order(5)
	public void testUpdateWithoutIdThrowsMissingFieldException() {
		entity.setId(null);
		
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> placeService.update(entity));
		assertEquals("Could not update, the field id is missing!", exception.getMessage());
	}
	
	@Test
	@Order(6)
	public void testUpdateThrowsObjectNotFoundException() {
		entity.setId(25);
		
		// Testing update by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> placeService.update(entity));
		assertEquals("Could not find object with id 25", exception.getMessage());
	}
	
	@Test
	@Order(7)
	public void testUpdateWithoutNameThrowsMissingFieldException() {
		entity.setName(null);
		
		// Testing update by passing a null value for name...
		Throwable exception = assertThrows(MissingFieldException.class, () -> placeService.update(entity));
		assertEquals("Could not update, the field name is missing!", exception.getMessage());
	}
	
	@Test
	@Order(8)
	public void testDeleteWithoutIdThrowsMissingFieldException() {
		entity.setId(null);
		
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> placeService.delete(entity));
		assertEquals("Could not delete, the field id is missing!", exception.getMessage());
	}
	
	@Test
	@Order(9)
	public void testDeleteThrowsObjectNotFoundException() {
		entity.setId(25);
		
		// Testing update by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> placeService.delete(entity));
		assertEquals("Could not find object with id 25", exception.getMessage());
	}
	
	@Test
	@Order(10)
	public void testDeleteByIdWithoutIdThrowsMissingFieldException() {
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> placeService.deleteById(null));
		assertEquals("Could not delete, the field id is missing!", exception.getMessage());
	}
	
	@Test
	@Order(11)
	public void testDeleteByIdThrowsObjectNotFoundException() {		
		// Testing update by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> placeService.deleteById(25));
		assertEquals("Could not find object with id 25", exception.getMessage());
	}
}
