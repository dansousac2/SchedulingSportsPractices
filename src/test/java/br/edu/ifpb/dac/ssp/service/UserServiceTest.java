package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.repository.UserRepository;

class UserServiceTest {
	
	private static UserService service;
	private User exUser;
	
	@Mock
	private static UserRepository repository;

	@BeforeAll
	public static void setup() {
		service = new UserService();
	}
	
	@BeforeEach
	public void beforeEach() {
		openMocks(this);
		exUser = new User();
	}

	@Test
	public void findByIdValid() { // valid
		try {
			when(repository.existsById(anyInt())).thenReturn(true);
			assertDoesNotThrow(() -> service.findById(1));
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
//	@ParameterizedTest
//	@ValueSource(ints = {1,10,99})
//	public void findByIdValid(int id) { // invalid
//		try {
//			when(repository.existsById(anyInt())).thenReturn(true);
//			assertNotEquals(ObjectNotFoundException.class, service.findById(id));
//		} catch (Exception e) {
//			e.getMessage();
//		}
//	}
}
