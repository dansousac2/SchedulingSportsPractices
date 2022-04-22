package br.edu.ifpb.dac.ssp.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class PlaceDTOTest {

	private PlaceDTO dto;
	private Set<ConstraintViolation<PlaceDTO>> violations;
	private static Validator validator;
	
	@BeforeAll
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		dto = new PlaceDTO();
		dto.setId(1);
		dto.setName("Quadra");
		dto.setReference("próximo a quadra");
		dto.setPublic(true);
		dto.setMaximumCapacityParticipants(80);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Laboratory22", "Gymnasium", "33 Floor   ", " Laboratory 33"}) // valid
	public void nameIsValid(String s) {
		dto.setName(s);
		violations = validator.validateProperty(dto, "name");
		
		if(violations.size() != 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		assertEquals(0, violations.size(), "INVALID NAME FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "   ", "\t", "\n\n", "D@n", "- 123 -", " *", "   &LA ", "!"}) // invalid
	public void nameIsInvalid(String s) {
		dto.setName(s);
		violations = validator.validateProperty(dto, "name");
		
		assertNotEquals(0, violations.size(), "VALID NAME FOUND< " + s + " >");
	}
}
