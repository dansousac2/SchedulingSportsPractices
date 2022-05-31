package br.edu.ifpb.dac.ssp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectAlreadyExistsException;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.model.dto.UserDTO;
import br.edu.ifpb.dac.ssp.repository.UserRepository;
import br.edu.ifpb.dac.ssp.service.UserConverterService;
import br.edu.ifpb.dac.ssp.service.UserService;

class UserControllerTest {

	@InjectMocks
	private static UserController controller;
	private static UserConverterService userConverter;
	@InjectMocks
	private static UserService service;
	@Mock
	private static UserRepository repository;
	@Captor
	private ArgumentCaptor<User> captor;

	private static UserDTO exUserDto;
	private static User exUser;
	private ResponseEntity resp;

	private Set<ConstraintViolation<UserDTO>> violations;
	private static Validator validator;

	@BeforeAll
	public static void setup() {
		service = new UserService();
		controller = new UserController();
		userConverter = new UserConverterService();
		ReflectionTestUtils.setField(service, "userRepository", repository);
		ReflectionTestUtils.setField(controller, "userService", service);
		ReflectionTestUtils.setField(controller, "converterService", userConverter);

		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);

		exUserDto = new UserDTO();
		exUserDto.setName("Dansousa");
		exUserDto.setEmail("dansousa@gmail.com");
		exUserDto.setPassword("1234567");
		exUserDto.setRegistration(123456789);
		exUserDto.setId(1);

		exUser = userConverter.dtoToUser(exUserDto);
	}

	@Test
	public void saveValidFields() { // save valid - compare fields of Dto and Entity; 201 and body

		resp = controller.save(exUserDto);

		verify(repository).save(captor.capture());

		User captured = captor.getValue();

		assertAll("Verify if atributes between Dto an Entity are equals",
				() -> assertEquals(exUserDto.getName(), captured.getName()),
				() -> assertEquals(exUserDto.getEmail(), captured.getEmail()),
				() -> assertEquals(exUserDto.getPassword(), captured.getPassword()),
				() -> assertEquals(exUserDto.getRegistration(), captured.getRegistration()));

		when(repository.save(any(User.class))).thenReturn(exUser);

		resp = controller.save(exUserDto);

		assertAll("HttpStatus and body", () -> assertEquals(HttpStatus.CREATED, resp.getStatusCode()),
				() -> assertEquals(UserDTO.class, resp.getBody().getClass()),
				() -> assertNotEquals(exUserDto, resp.getBody()));

	}

	@ParameterizedTest
	@ValueSource(strings = { "a", " 12   ", " \n   ", "   ", " bc \t" })
	public void saveInvalidNameJSON(String name) { // save invalid - field name invalid - JSON -> DTO
		try {
			exUserDto.setName(name);

			resp = controller.save(verifyViolations(exUserDto, "name"));

			fail();

		} catch (Exception e) {
			String error01 = "Please enter your name. Is required!";
			String error02 = "The space must be filled with a minimum of 3 characters and a maximum of 255!";

			if (!(e.getMessage().equals(error01) || e.getMessage().equals(error02))) {
				fail();
			}
		}
	}

	@ParameterizedTest
	@ValueSource(strings = { "", " \n  ", " \n \t", "null" }) // save invalid - name blanck or null
	public void saveInvalidNameAfeterJson(String name) {
		if (name.equals("null")) {
			UserDTO novo = new UserDTO(null, "dan@gmail.com", 123456, "yep"); // name null - barred in UserService
			resp = controller.save(novo);
		} else {
			exUserDto.setName(name); // name is blanck - barred in UserService
			resp = controller.save(exUserDto);
		}
		assertEquals("Could not save, the field name is missing!", resp.getBody());
	}

	@Test
	public void saveInvalidNull() { // save invalid - obj is null

		resp = controller.save(null); // barred in ConverterService

		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Could not convert because object is null", resp.getBody());
	}

	@Test
	public void saveInvalidRegistration() { // save invalid - user's registration already exists in DB
		when(repository.existsByRegistration(anyInt())).thenReturn(true);

		resp = controller.save(exUserDto);

		assertEquals("A user with registration 123456789 already exists!", resp.getBody());
	}

	@Test
	public void updateValid() { // update valid - HttpStatus ok and body
		when(repository.existsById(anyInt())).thenReturn(true); // userService
		when(repository.existsByRegistration(123456789)).thenReturn(true); // userService
		when(repository.findByRegistration(123456789)).thenReturn(Optional.of(exUser)); // userService
		when(repository.save(any(User.class))).thenReturn(exUser);

		resp = controller.update(1, exUserDto);

		assertAll("HttpStatus and body - Update", () -> assertEquals(HttpStatus.OK, resp.getStatusCode()),
				() -> assertEquals(UserDTO.class, resp.getBody().getClass()),
				() -> assertNotEquals(exUserDto, resp.getBody()));
	}

	@Test
	public void updateInvalidDtoNull() { // update invalid - obj null

		resp = controller.update(1, null); // controller

		String error = "because \"dto\" is null"; // final message of a NullPointException

		assertTrue(resp.getBody().toString().contains(error));
	}

	@ParameterizedTest
	@ValueSource(strings = { "    ", "\n  ", "  \t  \n \t ", "null" })
	public void updateInvalidName(String name) { // update invalid - Dto's name is blanck or null
		if (!name.equals("null")) {

			exUserDto.setName(name);

			resp = controller.update(1, exUserDto);
			assertEquals("Could not update, the field name is missing!", resp.getBody());
		} else {
			resp = controller.update(1, new UserDTO(null, "example@gmail.com", 13579, "password"));
			assertEquals("Could not update, the field name is missing!", resp.getBody());
		}
	}

	@Test
	public void updateInvalidIdNotFound() { // update invalid - Dto's Id not found in DB
		when(repository.existsById(anyInt())).thenReturn(false);

		resp = controller.update(12, exUserDto);

		assertEquals("Could not find User with id 12", resp.getBody());
	}

	@Test
	public void updateInvalidIdNotMatch() { // update invalid - dto's id in DB not match (same registration)
		User user = new User(2, "Afonso", "affff@gmail.com", 123456789, "passwordloko");

		when(repository.existsById(anyInt())).thenReturn(true);
		when(repository.existsByRegistration(anyInt())).thenReturn(true);
		when(repository.findByRegistration(123456789)).thenReturn(Optional.of(user));

		resp = controller.update(12, exUserDto);

		assertEquals("A user with registration 123456789 already exists!", resp.getBody());
	}

	@Test
	public void deleteValid() { // delete valid - Htttp and body
		when(repository.existsById(1)).thenReturn(true);

		resp = controller.delete(1);

		assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
		assertFalse(resp.hasBody());
	}

	@Test
	public void deleteInvalidIdNull() { // delete invalid - the id is null
		resp = controller.delete(null);

		assertEquals("Could not delete, the field id is missing!", resp.getBody());
	}

	@Test
	public void deleteInvalidIdNotFound() { // delete invalid - id not found in DB
		when(repository.existsById(9)).thenReturn(false);

		resp = controller.delete(9);

		assertEquals("Could not find User with id 9", resp.getBody());
	}

	@Test
	public void findByIDValid() { // findByID valid - Http and body
		when(repository.existsById(1)).thenReturn(true);
		when(repository.getById(1)).thenReturn(exUser);

		resp = controller.findById(1);

		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(UserDTO.class, resp.getBody().getClass());
	}

	@Test
	public void findByIDInvalidNotFound() { // findByID invalid - not found in DB
		when(repository.existsById(9)).thenReturn(false);
		resp = controller.findById(9);

		assertEquals("Could not find User with id 9", resp.getBody());
	}

	@Test
	public void findByIDInvalidNull() { // findByID invalid - id is null
		resp = controller.findById(null);

		assertEquals("Could not find User with id null", resp.getBody());
	}

	@Test
	public void getAllValidWithUsers() { // getAll valid - have User on DB
		User user = new User(2, "Afonso", "affff@gmail.com", 123456710, "passwordloko");
		List<User> list = new ArrayList<>();
		list.add(user);
		list.add(exUser);

		when(repository.findAll()).thenReturn(list);
		resp = controller.getAll();

		assertEquals(HttpStatus.OK, resp.getStatusCode());
	}

	@Test
	public void getAllValidWithoutUsers() { // getAll valid - have no User in DB
		List<User> list = new ArrayList<>();

		when(repository.findAll()).thenReturn(list);
		resp = controller.getAll();
		
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(ArrayList.class, resp.getBody().getClass());
	}
	
	// is the "mock" of Http body request
	private @Valid UserDTO verifyViolations(UserDTO exUserDto2, String field) throws Exception { 
		violations = validator.validateProperty(exUserDto2, field);
		if (violations.size() == 0) {
			return exUserDto2;
		}
		throw new Exception(violations.stream().findFirst().get().getMessage());
	}
}
