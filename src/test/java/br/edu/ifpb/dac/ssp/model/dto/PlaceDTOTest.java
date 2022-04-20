package br.edu.ifpb.dac.ssp.model.dto;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
	
	@Test
	public void nameIsInvalid() {
		dto.setName("");
		violations = validator.validateProperty(dto, "name");
		violations.forEach(e -> assertTrue(e.getMessage().equals("name can't be null or empty")));
	}
}
