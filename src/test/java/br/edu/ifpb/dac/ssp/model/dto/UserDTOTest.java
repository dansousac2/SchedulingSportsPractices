package br.edu.ifpb.dac.ssp.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserDTOTest {

	private UserDTO dto;
	private Set<ConstraintViolation<UserDTO>> violations;
	private static Validator validator;
	
	@BeforeAll
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		dto = new UserDTO();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"D@n", "  1", "@¨%"}) // valid
	public void schduledDateValid(String s) {
		dto.setName(s);
		violations = validator.validateProperty(dto, "name");
		
		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		assertEquals(0, violations.size(), "INVALID DATE FOUND<" + s + ">");
	}

}
