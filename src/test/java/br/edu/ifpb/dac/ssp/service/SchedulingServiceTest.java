package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectAlreadyExistsException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.repository.SchedulingRepository;

public class SchedulingServiceTest {
	
	@InjectMocks
	private static SchedulingService service = new SchedulingService();
	
	@Mock
	private static SchedulingRepository repository;
	private static Scheduling scheduling  = new Scheduling();
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(service, "schedulingRepository", repository);
		
	}
	
	@DisplayName("Id Valid")
	@Test
	public void testFindByIdObjectValid() {

		when(repository.existsById(anyInt())).thenReturn(true);

		assertDoesNotThrow(() -> service.findById(2));
		verify(repository.getById(2));
	}
	
	@DisplayName("Id Invalid")
	@Test
	public void testFindByIdObjectInalid() {
		when(repository.existsById(anyInt())).thenReturn(false);

		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(2));
		assertEquals("No scheduling were found with Id 2", exception.getMessage());
		
	}
	
	@DisplayName("Save Valid")
	@Test
	public void testSaveObjectValid() {
		
		scheduling.setId(2);
		
		when(repository.existsById(anyInt())).thenReturn(false);

		assertDoesNotThrow(() -> service.save(scheduling));
		verify(repository).save(scheduling);
		
	}
	
	@DisplayName("Save Invalid")
	@Test
	public void testSaveObjectInvalid() {
		
		scheduling.setId(2);
		
		when(repository.existsById(anyInt())).thenReturn(true);
		
		Throwable exception = assertThrows(ObjectAlreadyExistsException.class, () -> service.save(scheduling));
		assertEquals("A schedule already exists with id 2", exception.getMessage());
	}
	
	@DisplayName("Delete Valid User")
	@Test
	public void testDeleteValidUser() {
		
		when(repository.existsById(2)).thenReturn(true);

		assertDoesNotThrow(() -> service.deleteById(2));
		verify(repository).deleteById(2);
	}
	
	@DisplayName("Delete Invalid User")
	@Test
	public void testDeleteInvalidUser() {
		scheduling.setId(2);

		when(repository.existsById(2)).thenReturn(false);

		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.delete(scheduling));
		assertEquals("The schedule with id 2 was not found", exception.getMessage());
	}
	
	@DisplayName("Delete Valid Id")
	@Test
	public void testDeleteValidById() {
		
		when(repository.existsById(2)).thenReturn(true);

		assertDoesNotThrow(() -> service.deleteById(2));
		verify(repository).deleteById(2);
	}
	
	@DisplayName("Delete null id")
	@Test
	public void testDeleteInvalidByIdField() {
		Throwable exception = assertThrows(MissingFieldException.class, () -> service.deleteById(null));
		assertEquals("Cannot delete a null id!", exception.getMessage());
		
	}
	
	@DisplayName("Delete non-existent ID")
	@Test
	public void testDeleteInvalidByIdNotFound() {
		when(repository.existsById(2)).thenReturn(false);

		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.deleteById(2));
		assertEquals("The schedule with ID 2 was not found", exception.getMessage());
	}
	

}
