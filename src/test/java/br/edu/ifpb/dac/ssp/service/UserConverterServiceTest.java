package br.edu.ifpb.dac.ssp.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.edu.ifpb.dac.ssp.business.service.UserConverterService;
import br.edu.ifpb.dac.ssp.model.entity.User;
import br.edu.ifpb.dac.ssp.presentation.dto.UserDTO;

public class UserConverterServiceTest {
	
	private static UserConverterService converterService;
	private static User user;
	private static UserDTO dto;
	
	@BeforeAll
	public static void setUp() {
		converterService = new UserConverterService();
		user = new User();
		
		user.setId(1);
		user.setName("Maria Souza");
		user.setEmail("mariasouza@gmail.com");
		user.setRegistration(Long.valueOf(1234));
		
		dto = new UserDTO();
		dto.setId(1);
		dto.setName("Maria Souza");
		dto.setEmail("mariasouza@gmail.com");
		dto.setRegistration(Long.valueOf(1234));
			
	}
	
	
	@Test
	public void testConvertingDtoToEntity() {
		User userConverted = converterService.dtoToUser(dto);
		assertAll("Testing comparing dto and entity field by field",
				() -> assertEquals(userConverted.getId(), dto.getId()),
				() -> assertEquals(userConverted.getName(), dto.getName()),
				() -> assertEquals(userConverted.getEmail(), dto.getEmail()),
				() -> assertEquals(userConverted.getRegistration(), dto.getRegistration())
		);
	}
	
	
	@Test
	public void testConvertingEntityToDto() {
		UserDTO dtoConverted = converterService.userToDto(user);
		assertAll("Testing comparing dto and entity field by field",
				() -> assertEquals(dtoConverted.getId(), user.getId()),
				() -> assertEquals(dtoConverted.getName(), user.getName()),
				() -> assertEquals(dtoConverted.getEmail(), user.getEmail()),
				() -> assertEquals(dtoConverted.getRegistration(), user.getRegistration()));		
	}
	
	@Test
	public void testConvertingEntityToDtoNullPointer() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.userToDto(null));
		assertEquals("Não foi possível converter pois o objeto é nulo", exception.getMessage());
	}
	
	@Test
	public void testConvertingDtoToEntityNullPointer() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> converterService.dtoToUser(null));
		assertEquals("Não foi possível converter pois o objeto é nulo", exception.getMessage());
	}
	


}
