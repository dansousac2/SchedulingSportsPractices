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
	@ValueSource(strings = {"9999-99-99", "0000-00-00"}) // valid
	public void schduledDateValid(String s) {
		dto.setScheduledDate(s);
		violations = validator.validateProperty(dto, "scheduledDate");
		
		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		assertEquals(0, violations.size(), "INVALID DATE FOUND<" + s + ">");
	}

	@ParameterizedTest
	@ValueSource(strings = {"0000-00-00\n", "10000-00-00", "0000-100-00", "0000-00-100", "0000-0 0-00", "0000-00:00",
			"\n", "  \n ", "", "   "}) // invalid
	public void schduledDateInvalid(String s) {
		dto.setScheduledDate(s);
		violations = validator.validateProperty(dto, "scheduledDate");

		assertNotEquals(0, violations.size(), "VALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "99:99", "00:00", "99:99"}) // start time valid
	public void schduledStartTimeValid(String s) {
		dto.setScheduledStartTime(s);
		violations = validator.validateProperty(dto, "scheduledStartTime");

		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		
		assertEquals(0, violations.size(), "INVALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "    ", " \n  \n  ", " \t ", " 99:99", "099:99", "99:099", "99-99"}) // start time invalid
	public void schduledStartTimeInvalid(String s) {
		dto.setScheduledStartTime(s);
		violations = validator.validateProperty(dto, "scheduledStartTime");

		assertNotEquals(0, violations.size(), "VALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "99:99", "00:00", "99:99"}) // finish time valid
	public void schduledFinishTimeValid(String s) {
		dto.setScheduledFinishTime(s);
		violations = validator.validateProperty(dto, "scheduledFinishTime");

		if(violations.size() > 0) {
			System.out.println(s + " => " + violations.stream().findFirst().get().getMessage());	
		}
		
		assertEquals(0, violations.size(), "INVALID DATE FOUND<" + s + ">");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"", "    ", " \n  \n  ", " \t ", "99:bb", " 99:99", "099:99", "99:099", "99:99\n"}) // finish time invalid
	public void schduledFinishTimeInvalid(String s) {
		dto.setScheduledFinishTime(s);
		violations = validator.validateProperty(dto, "scheduledFinishTime");

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
