package br.edu.ifpb.dac.ssp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
	
	private UserDTO exUserDto;
	
	@BeforeAll
	public static void setup() {
		service = new UserService();
		controller = new UserController();
		userConverter = new UserConverterService();
		ReflectionTestUtils.setField(service, "userRepository", repository);
		ReflectionTestUtils.setField(controller, "userService", service);
		ReflectionTestUtils.setField(controller, "converterService", userConverter);
	}
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.openMocks(this);
		exUserDto = new UserDTO();
		exUserDto.setName("Dansousa");
		exUserDto.setEmail("dansousa@gmail.com");
		exUserDto.setPassword("1234567");
		exUserDto.setRegistration(123456789);
	}
	
	@Test
	public void test() {
		controller.save(exUserDto);
		verify(repository).save(any(User.class));
	}

}
