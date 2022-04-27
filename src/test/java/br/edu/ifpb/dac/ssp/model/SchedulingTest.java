package br.edu.ifpb.dac.ssp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import br.edu.ifpb.dac.ssp.model.Scheduling;

public class SchedulingTest {

	private Scheduling entity;
	private Set<ConstraintViolation<Scheduling>> violations;
	private static Validator validator;
	
	@BeforeAll
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@BeforeEach
	public void setUpBeforeEach() {
		entity = new Scheduling();
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
		assertEquals("Scheduled date shouldn't be in past", violations.stream().findFirst().get().getMessage());
	}
	
	@ParameterizedTest
	@ValueSource(ints = {10, 1, 0, 5, 20})
	public void testQuantityOfParticipantsValid(int quantity) {
		entity.setQuantityOfParticipants(quantity);
		
		violations = validator.validateProperty(entity, "quantityOfParticipants");
		assertEquals(0, violations.size());
	}
	
	@ParameterizedTest
	@ValueSource(ints = {-1, -5, -10, -20})
	public void testQuantityOfParticipantsInvalid(int quantity) {
		entity.setQuantityOfParticipants(quantity);
		
		violations = validator.validateProperty(entity, "quantityOfParticipants");
		
		assertNotEquals(0, violations.size());
		assertEquals("Quantity of participants shouldn't be a negative number!", violations.stream().findFirst().get().getMessage());
	}
	
}
