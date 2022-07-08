package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.print.attribute.SetOfIntegerSyntax;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectAlreadyExistsException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.repository.SchedulingRepository;

public class SchedulingServiceTest {

	@InjectMocks
	private static SchedulingService service;

	@Mock
	private static SchedulingRepository repository;
	
	private static Scheduling schedulingExp;
	private static Place placeExp;
	private static User userExp;

	@BeforeAll
	public static void setup() {
		service = new SchedulingService();
		schedulingExp = new Scheduling();
		
		placeExp = new Place();
		userExp = new User();
		userExp.setId(3);
		
		ReflectionTestUtils.setField(service, "schedulingRepository", repository);
	}

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		
		User user = new User();
		user.setId(1);
		User user02 = new User();
		user02.setId(2);
		Set<User> users = Set.of(user, user02);
		schedulingExp.setParticipants(users);
	}

	@DisplayName("Id Valid")
	@Test
	public void testFindByIdObjectValid() {

		when(repository.existsById(anyInt())).thenReturn(true);

		assertDoesNotThrow(() -> service.findById(2));
		verify(repository).getById(2);
	}

	@DisplayName("Id Invalid")
	@Test
	public void testFindByIdObjectInalid() {
		when(repository.existsById(anyInt())).thenReturn(false);

		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(2));
		assertEquals("Não foi encontrado agendamento com id 2", exception.getMessage());

	}

	@DisplayName("Save Valid")
	@Test
	public void testSaveObjectValid() {

		schedulingExp.setId(2);

		when(repository.existsById(anyInt())).thenReturn(false);

		assertDoesNotThrow(() -> service.save(schedulingExp));
		verify(repository).save(schedulingExp);

	}

	@DisplayName("Delete Valid User")
	@Test
	public void testDeleteValidUser() {

		when(repository.existsById(2)).thenReturn(true);

		assertDoesNotThrow(() -> service.deleteById(2));
		verify(repository).deleteById(2);
	}

	@DisplayName("Delete Invalid User and null id")
	@Test
	public void testDeleteInvalidUser() {
		schedulingExp.setId(2);

		when(repository.existsById(2)).thenReturn(false);

		Throwable exception = assertThrows(ObjectNotFoundException.class, () -> service.delete(schedulingExp));
		assertEquals("Não foi encontrado agendamento com id 2", exception.getMessage());

		Throwable exception02 = assertThrows(MissingFieldException.class, () -> service.deleteById(null));
		assertEquals("Não foi possível usar delete, o campo id está faltando!", exception02.getMessage());
	}

	@DisplayName("Quantity of participants and participants valids")
	@Test
	public void getSchedulingQuantityOfParticipantsValid() {
		try {
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedulingExp);
			
			//quantity os particiapnts
			int qtdPart = service.getSchedulingQuantityOfParticipants(1);

			assertEquals(2, qtdPart);
			
			//participants
			Set<User> participants = service.getSchedulingParticipants(1);

			assertTrue(participants.containsAll(schedulingExp.getParticipants()));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@DisplayName("Quantity of participants and participants invalid")
	@Test
	public void getSchedulingQuantityOfParticipantsInvalid() {
		// ID is null - quantity of participants
		Throwable excep = assertThrows(MissingFieldException.class,
				() -> service.getSchedulingQuantityOfParticipants(null));
		assertEquals("Não foi possível concluir a ação, o campo id está faltando!", excep.getMessage());

		// ID is null - participants
		Throwable excep03 = assertThrows(MissingFieldException.class,
				() -> service.getSchedulingParticipants(null));
		assertEquals("Não foi possível concluir a ação, o campo id está faltando!", excep03.getMessage());
		
		when(repository.existsById(1)).thenReturn(false);
		
		// ID not finded in DB - quantity of participants
		Throwable excep02 = assertThrows(ObjectNotFoundException.class,
				() -> service.getSchedulingQuantityOfParticipants(1));
		assertEquals("Não foi encontrado agendamento com id 1", excep02.getMessage());
		
		// ID not finded in DB - participants
		Throwable excep04 = assertThrows(ObjectNotFoundException.class,
				() -> service.getSchedulingParticipants(1));
		assertEquals("Não foi encontrado agendamento com id 1", excep04.getMessage());
	}
	
	@DisplayName("add participant valid")
	@Test
	public void addSchedulingParticipantValid() {
		try {
			placeExp.setMaximumCapacityParticipants(3);
			Scheduling schedSpy = spy(schedulingExp);
			schedSpy.setPlace(placeExp);
			
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedSpy);
			
			service.addSchedulingParticipant(1, userExp);
			verify(schedSpy).setParticipants(anySet());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@DisplayName("add participant invalid")
	@Test
	public void addSchedulingParticipantInvalid() {
		
		//ID is null
		Throwable excep = assertThrows(MissingFieldException.class, () -> service.addSchedulingParticipant(null, userExp));
		assertEquals("Não foi possível concluir a ação, o campo id está faltando!", excep.getMessage());
		
		when(repository.existsById(1)).thenReturn(false);
		
		//ID is not present in DB 
		Throwable excep02 = assertThrows(ObjectNotFoundException.class, () -> service.addSchedulingParticipant(1, userExp));
		assertEquals("Não foi encontrado agendamento com id 1", excep02.getMessage());
		
		//no more confirmations avaliable to this scheduling - atcualy we have 2 confirmed, and place capacity 3
		placeExp.setMaximumCapacityParticipants(2);
		Scheduling schedSpy = spy(schedulingExp);
		schedSpy.setPlace(placeExp);
		
		try {
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedSpy);
			
			service.addSchedulingParticipant(1, userExp);
			
			assertAll("quantity of scheduling participants must be 2, and setParticipants never called",
					() -> assertEquals(2, schedSpy.getParticipants().size()),
					() -> verify(schedSpy,never()).setParticipants(anySet())
			);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@DisplayName("removing participant valid")
	@Test
	public void removeSchedulingParticipantValid() {
		try {
			Scheduling schedSpy = spy(schedulingExp);
			Set<User> set = new HashSet<>(schedSpy.getParticipants());
			set.add(userExp);
			schedSpy.setParticipants(set);
			
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedSpy);
			
			service.removeSchedulingParticipant(1, userExp);
			assertAll("tests with removing interested user of scheduling",
					() -> verify(schedSpy, times(3)).getParticipants(),
					() -> assertEquals(2, schedSpy.getParticipants().size()),
					() -> assertFalse(schedSpy.getParticipants().contains(userExp))
			);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@DisplayName("removing participant invalid")
	@Test
	public void removeSchedulingParticipantInvalid() {
		try {
			//ID is null
			Throwable excep = assertThrows(MissingFieldException.class, () -> service.addSchedulingParticipant(null, userExp));
			assertEquals("Não foi possível concluir a ação, o campo id está faltando!", excep.getMessage());
			
			when(repository.existsById(1)).thenReturn(false);
			
			//ID is not present in DB 
			Throwable excep02 = assertThrows(ObjectNotFoundException.class, () -> service.addSchedulingParticipant(1, userExp));
			assertEquals("Não foi encontrado agendamento com id 1", excep02.getMessage());
			
			//list participant empty on scheduling
			Set<User> listEmpty = Set.of();
			schedulingExp.setParticipants(listEmpty);
			Scheduling schedSpy = spy(schedulingExp);
			schedSpy.setParticipants(listEmpty);
			
			when(repository.existsById(1)).thenReturn(true);
			when(repository.getById(1)).thenReturn(schedSpy);
			
			service.removeSchedulingParticipant(1, userExp);
			
			verify(schedSpy,times(1)).getParticipants();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
