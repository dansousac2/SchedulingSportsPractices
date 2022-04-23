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

class SchedulingDTOTest {

	private SchedulingDTO dto;
	private Set<ConstraintViolation<SchedulingDTO>> violations;
	private static Validator validator;
	
	@BeforeAll
	public static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@BeforeEach
	public void beforeEach() {
		dto = new SchedulingDTO();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"99/99/9999 99:99:99", "00/00/0000 00:00:00", "  \n   00/00/0000 00:00:00  \n "}) // valid
	public void schduledDateValid(String s) {
		dto.setScheduledDate(s);
		violations = validator.validateProperty(dto, "scheduledDate");
		
		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		assertEquals(0, violations.size(), "INVALID DATE FOUND<" + s + ">");
	}

	@ParameterizedTest
	@ValueSource(strings = {"", "    ", " \n ", "\t", "100/00/0000 00:00:00", "00/100/0000 00:00:00", "00/00/10000 00:00:00", "00/00/0000 100:00:00",
			"00/00/0000 00:100:00", "00/00/0000 00:00:100", "00/00/00 00 00:00:00", "00/00/000 00:00:00",
			"00-00/0000 00:00:00", "00/00/0000-00:00:00"}) // invalid
	public void schduledDateInvalid(String s) {
		dto.setScheduledDate(s);
		violations = validator.validateProperty(dto, "scheduledDate");

		assertNotEquals(0, violations.size(), "VALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "99:99:99", "00:00:00", "  99:99:99  "}) // duration valid
	public void schduledDurationValid(String s) {
		dto.setDuration(s);
		violations = validator.validateProperty(dto, "duration");

		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		
		assertEquals(0, violations.size(), "INVALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "    ", " \n  \n  ", " \t ", "100:00:00", "00:100:00", "00:00:100", "00:0 0:00",
			"00:00:0", "00:00-00"}) // duration invalid
	public void schduledDurationInvalid(String s) {
		dto.setDuration(s);
		violations = validator.validateProperty(dto, "duration");

		assertNotEquals(0, violations.size(), "VALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1234", "Lab. c2", "  Laboratory-C2  ", "   La b   "}) // Place's name valid
	public void schduledPlaceNameValid(String s) {
		dto.setPlaceName(s);
		violations = validator.validateProperty(dto, "placeName");

		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		
		assertEquals(0, violations.size(), "INVALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"123", "Lab        ", "    Lab", "    Lab   ", "", "     ", "  \n ", "\t    ", "&"}) // Place's name invalid
	public void schduledPlaceNameInvalid(String s) {
		dto.setPlaceName(s);
		violations = validator.validateProperty(dto, "placeName");

		assertNotEquals(0, violations.size(), "VALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1234", "basebool 2.0", "  Hugby  ", "   Hu g   "}) // Place's name valid
	public void schduledSportNameValid(String s) {
		dto.setSportName(s);
		violations = validator.validateProperty(dto, "sportName");

		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		
		assertEquals(0, violations.size(), "INVALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"123", "Bas        ", "    Bol", "    Bas   ", "", "     ", "  \n ", "\t    ", "&"}) // Place's name invalid
	public void schduledSportNameInvalid(String s) {
		dto.setSportName(s);
		violations = validator.validateProperty(dto, "sportName");

		assertNotEquals(0, violations.size(), "VALID DATE FOUND<" + s + ">");
	}
}
