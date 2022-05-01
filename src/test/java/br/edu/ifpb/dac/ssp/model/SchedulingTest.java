package br.edu.ifpb.dac.ssp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("unchecked")
public class SchedulingTest {

	private static Scheduling entity;
	private Set<ConstraintViolation<Scheduling>> violations;
	private static Validator validator;
	
	@BeforeAll
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		
		entity = spy(Scheduling.class);
	}
	
	@Test
	public void testScheduledDateValid() {
		LocalDate date = LocalDate.now().plusDays(1);
		entity.setScheduledDate(date);
		
		violations = validator.validateProperty(entity, "scheduledDate");
		assertEquals(0, violations.size());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"2020-04-27", "2005-01-01", "1990-12-12", "2022-04-26"})
	public void testScheduledDateInvalid(String dateString) {
		LocalDate date = LocalDate.parse(dateString);
		entity.setScheduledDate(date);
		
		violations = validator.validateProperty(entity, "scheduledDate");
		
		assertNotEquals(0, violations.size());
		assertEquals("Scheduled date shouldn't be in past!", violations.stream().findFirst().get().getMessage());
	}
	
	@Test
	public void testAddParticipantValid() {
		Place placeMock = mock(Place.class);
		User userMock = mock(User.class);
		Set<User> setMock = mock(Set.class);
		
		when(placeMock.getMaximumCapacityParticipants()).thenReturn(5);
		when(setMock.add(any(User.class))).thenReturn(true);
		when(setMock.size()).thenReturn(0);
		
		entity.setPlace(placeMock);
		entity.setParticipants(setMock);
		
		entity.addParticipant(userMock);
		
		verify(placeMock).getMaximumCapacityParticipants();
		verify(setMock).add(any(User.class));	
		
		assertTrue(setMock.add(userMock));
	}
	
	@Test
	public void testAddParticipantInvalid() {
		Place placeMock = mock(Place.class);
		User userMock = mock(User.class);
		Set<User> setMock = mock(Set.class);
		
		when(placeMock.getMaximumCapacityParticipants()).thenReturn(5);
		when(setMock.size()).thenReturn(5);
		when(setMock.add(any(User.class))).thenReturn(true);
		
		entity.setPlace(placeMock);
		entity.setParticipants(setMock);
		
		entity.addParticipant(userMock);
		
		verify(placeMock).getMaximumCapacityParticipants();
		verify(setMock, never()).add(any(User.class));
		
		assertEquals(5, entity.getQuantityOfParticipants());
	}
	
	@Test
	public void testRemoveParticipantValid() {
		User userMock = mock(User.class);
		Set<User> setMock = mock(Set.class);
		
		when(setMock.size()).thenReturn(1);
		when(setMock.remove(any(User.class))).thenReturn(true);
		
		entity.setParticipants(setMock);
		
		entity.removeParticipant(userMock);
		
		verify(setMock).remove(any(User.class));	
		
		assertTrue(setMock.remove(userMock));
	}
	
	@Test
	public void testRemoveParticipantInvalid() {
		User userMock = mock(User.class);
		Set<User> setMock = mock(Set.class);
		
		when(setMock.size()).thenReturn(0);
		when(setMock.remove(any(User.class))).thenReturn(true);
		
		entity.setParticipants(setMock);
		entity.removeParticipant(userMock);
		
		verify(setMock, never()).remove(any(User.class));
		
		assertEquals(0, entity.getQuantityOfParticipants());
	}
}
