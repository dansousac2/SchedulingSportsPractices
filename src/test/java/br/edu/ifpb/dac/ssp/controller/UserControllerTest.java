package br.edu.ifpb.dac.ssp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

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
	
	@BeforeAll
	public static void setup() {
		service = new UserService();
		controller = new UserController();
		userConverter = new UserConverterService();
		ReflectionTestUtils.setField(service, "userRepository", repository);
		ReflectionTestUtils.setField(controller, "userService", service);
		ReflectionTestUtils.setField(controller, "converterService", userConverter);
		
		exUserDto = new UserDTO();
		exUserDto.setName("Dansousa");
		exUserDto.setEmail("dansousa@gmail.com");
		exUserDto.setPassword("1234567");
		exUserDto.setRegistration(123456789);
		
		exUser = userConverter.dtoToUser(exUserDto);
	}
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void saveValidFields() { // save valid - compare fields of Dto and Entity
		
		resp = controller.save(exUserDto);
		
		verify(repository).save(captor.capture());
		
		User captured = captor.getValue();
		
		assertAll("Verify if atributes between Dto an Entity are equals",
			() -> assertEquals(exUserDto.getName(), captured.getName()),
			() -> assertEquals(exUserDto.getEmail(), captured.getEmail()),
			() -> assertEquals(exUserDto.getPassword(), captured.getPassword()),
			() -> assertEquals(exUserDto.getRegistration(), captured.getRegistration())
		);
	}
	
	@Test
	public void saveValidRespEnt() { // save valid - COD 201 and body
		
		when(repository.save(any(User.class))).thenReturn(exUser);
		
		resp = controller.save(exUserDto);
		
		assertAll("HttpStatus and body",
				() -> assertEquals(HttpStatus.CREATED, resp.getStatusCode()),
				() -> assertEquals(UserDTO.class, resp.getBody().getClass()),
				() -> assertNotEquals(exUserDto, resp.getBody())
		);
	}
	
	@Test
	public void saveInvalidNull() { // save invalid - obj is null

		resp = controller.save(null);
		
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		assertEquals("Could not convert because object is null", resp.getBody());
	}

	@Test
	public void saveInvalidNameNull() {
		exUserDto.setName("2");
		resp = controller.save(exUserDto);
		
		verify(repository).save(any(User.class));
	}
}
