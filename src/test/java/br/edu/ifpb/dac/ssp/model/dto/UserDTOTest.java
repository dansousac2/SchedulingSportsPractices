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

import br.edu.ifpb.dac.ssp.presentation.dto.UserDTO;

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
	@ValueSource(strings = {"D@n", "@¨%", " U 2   "}) // valid
	public void userNameValid(String s) {
		dto.setName(s);
		violations = validator.validateProperty(dto, "name");
		
		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		assertEquals(0, violations.size(), "INVALID NAME FOUND<" + s + ">");
		
	}

	@ParameterizedTest
	@ValueSource(strings = {"Ex","      Go  ", "", "    ", " \n  ", "\t  "}) // invalid
	public void userNameInvalid(String s) {
		dto.setName(s);
		violations = validator.validateProperty(dto, "name");
		
		assertNotEquals(0, violations.size(), "VALID NAME FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"example@gmail.com", "example_123@yahoo.com.br"}) // valid
	public void userEmailValid(String s) {
		dto.setEmail(s);
		violations = validator.validateProperty(dto, "email");
		
		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		assertEquals(0, violations.size(), "INVALID EMAIL FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"examp e@gmail.com", " example_123@yahoo.com.br", "example@hotmail"}) // invalid
	public void userEmailInvalid(String s) {
		dto.setEmail(s);
		violations = validator.validateProperty(dto, "email");
		
		assertNotEquals(0, violations.size(), "VALID EMAIL FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1234567", "1a  5b ", "      a", " &¨*   "}) // valid
	public void userPasswordValid(String s) {
		dto.setPassword(s);
		violations = validator.validateProperty(dto, "password");
		
		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		assertEquals(0, violations.size(), "INVALID PASSWORD FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"123456", "", "    ", "  \n ", "   \t "}) // invalid
	public void userPasswordInvalid(String s) {
		dto.setPassword(s);
		violations = validator.validateProperty(dto, "password");
		
		assertNotEquals(0, violations.size(), "VALID PASSWORD FOUND<" + s + ">");
	}
}
