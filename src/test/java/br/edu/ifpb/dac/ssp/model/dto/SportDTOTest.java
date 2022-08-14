package br.edu.ifpb.dac.ssp.model.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import br.edu.ifpb.dac.ssp.presentation.dto.SportDTO;

class SportDTOTest {

	private SportDTO dto;
	private Set<ConstraintViolation<SportDTO>> violations;
	private static Validator validator;
	
	@BeforeAll
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		dto = new SportDTO();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Voleyball", " Football", "Basketball   "}) // valid
	public void nameIsValid(String s) {
		dto.setName(s);
		violations = validator.validateProperty(dto, "name");
		
		if(violations.size() != 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		assertEquals(0, violations.size(), "INVALID NAME FOUND<" + s + ">");
	}

	@ParameterizedTest
	@ValueSource(strings = {"123", "Fu", "", "   ", "\t", "\n\n", " Futb@ll  ", " *", "   &LA ", ","}) // invalid
	public void nameIsInvalid(String s) {
		dto.setName(s);
		violations = validator.validateProperty(dto, "name");
		
		assertNotEquals(0, violations.size(), "VALID NAME FOUND< " + s + " >");
	}
	
}
