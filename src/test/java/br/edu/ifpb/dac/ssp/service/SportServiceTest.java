package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;

import br.edu.ifpb.dac.ssp.exception.*;
import br.edu.ifpb.dac.ssp.model.Sport;
import br.edu.ifpb.dac.ssp.repository.SportRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

public class SportServiceTest {

	@InjectMocks
	private static SportService service;
	@Mock
	private static SportRepository repository;
	private Sport entity = new Sport();
	
	@BeforeAll
	public static void setup() {
		service = new SportService();
		ReflectionTestUtils.setField(service, "sportRepository", repository);
	}
	
	@BeforeEach
	public void beforeEach() {
		System.out.println("Initializing classes...");
		MockitoAnnotations.openMocks(this);
		
		System.out.println("Setting attributtes for Sport...");
		entity.setId(1);
		entity.setName("Basquete");
	}
	
	@Test
	@Order(1)
	public void testFindByIdThrowsObjectNotFoundException() { // id invalid
		// Testing findById by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(25));
		assertEquals("Could not find Sport with id 25", exception.getMessage());
	}
	
	@Test
	public void findByIdValid() { // id invalid
		try {
			when(repository.existsById(anyInt())).thenReturn(true);
			
			service.findById(1);
			
			verify(repository).getById(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(2)
	public void testFindByNameThrowsMissingFieldException() {
		// Testing findByName by passing a null value...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.findByName(null));
		assertEquals("Could not complete action, the field name is missing!", exception.getMessage());
	}
	
	@Test
	@Order(3)
	public void testFindByNameThrowsObjectNotFoundException() {
		// Testing findByName by passing an invalid name...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.findByName("Golfe"));
		assertEquals("Could not find Sport with name Golfe", exception.getMessage());
	}
	
	@Test
	@Order(4)
	public void testSaveThrowsMissingFieldException() {
		entity.setName(null);
		
		// Testing save by passing a null value for name...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.save(entity));
		assertEquals("Could not save, the field name is missing!", exception.getMessage());
	}
	
	@Test
	@Order(5)
	public void testUpdateWithoutIdThrowsMissingFieldException() {
		entity.setId(null);
		
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.update(entity));
		assertEquals("Could not update, the field id is missing!", exception.getMessage());
	}
	
	@Test
	@Order(6)
	public void testUpdateThrowsObjectNotFoundException() {
		entity.setId(25);
		
		// Testing update by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.update(entity));
		assertEquals("Could not find Sport with id 25", exception.getMessage());
	}
	
	@Test
	@Order(7)
	public void testUpdateWithoutNameThrowsMissingFieldException() {
		entity.setName(null);
		
		// Testing update by passing a null value for name...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.update(entity));
		assertEquals("Could not update, the field name is missing!", exception.getMessage());
	}
	
	@Test
	@Order(8)
	public void testDeleteWithoutIdThrowsMissingFieldException() {
		entity.setId(null);
		
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.delete(entity));
		assertEquals("Could not delete, the field id is missing!", exception.getMessage());
	}
	
	@Test
	@Order(9)
	public void testDeleteThrowsObjectNotFoundException() {
		entity.setId(25);
		
		// Testing update by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.delete(entity));
		assertEquals("Could not find Sport with id 25", exception.getMessage());
	}
	
	@Test
	@Order(10)
	public void testDeleteByIdWithoutIdThrowsMissingFieldException() {
		// Testing update by passing a null value for id...
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.deleteById(null));
		assertEquals("Could not delete, the field id is missing!", exception.getMessage());
	}
	
	@Test
	@Order(11)
	public void testDeleteByIdThrowsObjectNotFoundException() {		
		// Testing update by passing an invalid id...
		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.deleteById(25));
		assertEquals("Could not find Sport with id 25", exception.getMessage());
	}
}
