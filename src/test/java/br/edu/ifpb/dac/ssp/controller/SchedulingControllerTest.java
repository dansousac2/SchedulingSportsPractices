package br.edu.ifpb.dac.ssp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.exception.RuleViolationException;
import br.edu.ifpb.dac.ssp.exception.TimeAlreadyScheduledException;
import br.edu.ifpb.dac.ssp.model.Place;
import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.Sport;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;
import br.edu.ifpb.dac.ssp.model.dto.UserDTO;
import br.edu.ifpb.dac.ssp.service.SchedulingConverterService;
import br.edu.ifpb.dac.ssp.service.SchedulingService;
import br.edu.ifpb.dac.ssp.service.SchedulingValidatorService;
import br.edu.ifpb.dac.ssp.service.UserConverterService;
import br.edu.ifpb.dac.ssp.service.UserService;
import br.edu.ifpb.dac.ssp.util.Constants;

public class SchedulingControllerTest {
	
	@Mock
	private SchedulingService schedulingService;
	
	@Mock
	private UserService userService;
	
	@Mock
	private SchedulingConverterService converterService;
	
	@Mock
	private UserConverterService userConverterService;
	
	@Mock
	private SchedulingValidatorService validatorService;
	
	@InjectMocks
	@Spy
	private static SchedulingController controller;
	
	private static SchedulingDTO dto;
	private static Scheduling entity;
	private static Optional<Place> place;
	private static Optional<Sport> sport;
	
	@Mock
	private static User user;
	@Mock
	private static UserDTO userDto;
	
	private static ArrayList<Scheduling> listEntity;
	private static ArrayList<SchedulingDTO> listDto;
	private static Set<User> setUser;
	private static ArrayList<UserDTO> listUserDto;
	
	private ResponseEntity response;
	
	@BeforeAll 
	public static void setUpBeforeAll() {
		place = Optional.of(new Place(1, "Ginásio", "Perto do estacionamento", 80, false));
		sport = Optional.of(new Sport(2, "Futebol"));
		
		System.out.println("Setting attributtes for entity...");
		entity = new Scheduling();
		entity.setId(1);
		entity.setScheduledDate(LocalDate.parse("2022-05-01"));
		entity.setScheduledStartTime(LocalTime.parse("08:00"));
		entity.setScheduledFinishTime(LocalTime.parse("09:00"));
		entity.setPlace(place.get());
		entity.setSport(sport.get());
		
		System.out.println("Setting attributtes for dto...");
		dto = new SchedulingDTO();
		dto.setId(1);
		dto.setScheduledDate("2022-05-01");
		dto.setScheduledStartTime("08:00");
		dto.setScheduledFinishTime("09:00");
		dto.setPlaceName("Ginásio");
		dto.setSportName("Futebol");
		
		listEntity = new ArrayList();
		listEntity.add(entity);
		
		listDto = new ArrayList();
		listDto.add(dto);
		
		setUser = new HashSet();
		setUser.add(user);
		entity.setParticipants(setUser);
		
		listUserDto = new ArrayList();
		listUserDto.add(userDto);
	}
	
	@BeforeEach
	public void setUpBeforeEach() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testFindByIdValid() {
		try {
			when(schedulingService.findById(anyInt())).thenReturn(entity);
			when(converterService.schedulingToDto(any(Scheduling.class))).thenReturn(dto);
			
		} catch (Exception e) {
			fail();
		}
		
		response = controller.findById(1);
		
		assertAll("Asserting HttpStatus and body content",
			() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
			() -> assertEquals(SchedulingDTO.class, response.getBody().getClass()),
			() -> assertEquals(dto, response.getBody()));
	}
	
	@Test 
	public void testFindByIdInvalid() {
		try {
			when(schedulingService.findById(anyInt())).thenCallRealMethod();
		} catch (Exception e) {
			fail();
		}
		
		response = controller.findById(100);
		
		assertAll("Asserting HttpStatus and body content",
			() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
			() -> assertEquals("Could not find Scheduling with id 100", response.getBody()));
		
		try {
			verify(converterService, never()).schedulingToDto(any(Scheduling.class));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testSaveValid() {
		try {
			when(validatorService.validateSchedulingDTO(any(SchedulingDTO.class))).thenReturn(true);
			when(validatorService.validateScheduling(any(Scheduling.class))).thenReturn(true);
			
			when(converterService.dtoToScheduling(any(SchedulingDTO.class))).thenReturn(entity);
			when(converterService.schedulingToDto(any(Scheduling.class))).thenReturn(dto);
		
			when(schedulingService.save(any(Scheduling.class))).thenReturn(entity);
		} catch (Exception e) {
			fail();
		}
		
		response = controller.save(dto);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
				() -> assertEquals(SchedulingDTO.class, response.getBody().getClass()),
				() -> assertEquals(dto, response.getBody()));
	}
	
	@Test
	public void testSaveInvalidPlaceName() {
		try {
			when(validatorService.validateSchedulingDTO(any(SchedulingDTO.class))).thenThrow(new ObjectNotFoundException("Place", "name",  entity.getPlace().getName()));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.save(dto);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Could not find Place with name Ginásio", response.getBody()));
		
		try {
			verify(converterService, never()).dtoToScheduling(any(SchedulingDTO.class));
			verify(validatorService, never()).validateScheduling(any(Scheduling.class));
			verify(converterService, never()).schedulingToDto(any(Scheduling.class));
			verify(schedulingService, never()).save(any(Scheduling.class));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testSaveInvalidSportName() {
		try {
			when(validatorService.validateSchedulingDTO(any(SchedulingDTO.class))).thenThrow(new ObjectNotFoundException("Sport", "name",  entity.getSport().getName()));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.save(dto);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Could not find Sport with name Futebol", response.getBody()));
		
		try {
			verify(converterService, never()).dtoToScheduling(any(SchedulingDTO.class));
			verify(validatorService, never()).validateScheduling(any(Scheduling.class));
			verify(converterService, never()).schedulingToDto(any(Scheduling.class));
			verify(schedulingService, never()).save(any(Scheduling.class));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testSaveInvalidSchedulingDateAndTime() {
		try {
			when(validatorService.validateSchedulingDTO(any(SchedulingDTO.class))).thenThrow(new RuleViolationException("Scheduled date shouldn't be in past!"));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.save(dto);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Scheduled date shouldn't be in past!", response.getBody()));
		
		try {
			verify(converterService, never()).dtoToScheduling(any(SchedulingDTO.class));
			verify(validatorService, never()).validateScheduling(any(Scheduling.class));
			verify(converterService, never()).schedulingToDto(any(Scheduling.class));
			verify(schedulingService, never()).save(any(Scheduling.class));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testSaveInvalidScheduledTime() {
		try {
			when(validatorService.validateSchedulingDTO(any(SchedulingDTO.class))).thenThrow(new RuleViolationException("Scheduled time should be between " + Constants.INSTITUTION_OPENING_TIME + " and " + Constants.INSTITUTION_CLOSING_TIME));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.save(dto);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Scheduled time should be between 07:00 and 22:00", response.getBody()));
		
		try {
			verify(converterService, never()).dtoToScheduling(any(SchedulingDTO.class));
			verify(validatorService, never()).validateScheduling(any(Scheduling.class));
			verify(converterService, never()).schedulingToDto(any(Scheduling.class));
			verify(schedulingService, never()).save(any(Scheduling.class));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test 
	public void testSaveInvalidDurationOfPractice() {
		try {
			when(validatorService.validateSchedulingDTO(any(SchedulingDTO.class))).thenThrow(new RuleViolationException("Duration of practice should be a maximum of " + Constants.MAXIMUM_DURATION_PRACTICE_MINUTES + " minutes!"));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.save(dto);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Duration of practice should be a maximum of 180 minutes!", response.getBody()));
		
		try {
			verify(converterService, never()).dtoToScheduling(any(SchedulingDTO.class));
			verify(validatorService, never()).validateScheduling(any(Scheduling.class));
			verify(converterService, never()).schedulingToDto(any(Scheduling.class));
			verify(schedulingService, never()).save(any(Scheduling.class));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testSaveInvalidScheduledDate() {
		try {
			when(validatorService.validateSchedulingDTO(any(SchedulingDTO.class))).thenReturn(true);
			when(converterService.dtoToScheduling(any(SchedulingDTO.class))).thenReturn(entity);

			when(validatorService.validateScheduling(any(Scheduling.class))).thenThrow(new TimeAlreadyScheduledException());
		} catch (Exception e) {
			fail();
		}
		
		response = controller.save(dto);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("There is already a practice scheduled for this time!", response.getBody()));
		
		try {
			verify(converterService).dtoToScheduling(any(SchedulingDTO.class));
			verify(validatorService).validateSchedulingDTO(any(SchedulingDTO.class));
			verify(converterService, never()).schedulingToDto(any(Scheduling.class));
			verify(schedulingService, never()).save(any(Scheduling.class));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testDeleteValid() {
		response = controller.delete(1);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	@Test
	public void testGetAllConfirmedByPlaceValid() {
		try {
			when(validatorService.validatePlaceName(anyString())).thenReturn(true);
			when(schedulingService.findAllByPlaceName(anyString())).thenReturn(listEntity);
			when(converterService.schedulingToDtos(listEntity)).thenReturn(listDto);
		} catch (Exception e) {
			fail();
		}
		
		response = controller.getAllSchedulingConfirmedByPlace("Ginásio");
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals(listDto, response.getBody()));
	}
	
	@Test
	public void testGetAllConfirmedByPlaceInvalid() {
		try {
			when(validatorService.validatePlaceName(anyString())).thenThrow(new ObjectNotFoundException("Place", "name",  entity.getPlace().getName()));
			
			when(schedulingService.findAllByPlaceName(anyString())).thenReturn(listEntity);
			when(converterService.schedulingToDtos(listEntity)).thenReturn(listDto);
		} catch (Exception e) {
			fail();
		}
		
		response = controller.getAllSchedulingConfirmedByPlace("Ginásio");
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Could not find Place with name Ginásio", response.getBody()));
		
		try {
			verify(schedulingService, never()).findAllByPlaceName(anyString());
			verify(converterService, never()).schedulingToDtos(listEntity);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testGetAllConfirmedBySportValid() {
		try {
			when(validatorService.validateSportName(anyString())).thenReturn(true);
			when(schedulingService.findAllBySportName(anyString())).thenReturn(listEntity);
			when(converterService.schedulingToDtos(listEntity)).thenReturn(listDto);
		} catch (Exception e) {
			fail();
		}
		
		response = controller.getAllSchedulingConfirmedBySport("Futebol");
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals(listDto, response.getBody()));
	}
	
	@Test
	public void testGetAllConfirmedBySportInvalid() {
		try {
			when(validatorService.validateSportName(anyString())).thenThrow(new ObjectNotFoundException("Sport", "name",  entity.getSport().getName()));
			
			when(schedulingService.findAllBySportName(anyString())).thenReturn(listEntity);
			when(converterService.schedulingToDtos(listEntity)).thenReturn(listDto);
		} catch (Exception e) {
			fail();
		}
		
		response = controller.getAllSchedulingConfirmedBySport("Futebol");
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Could not find Sport with name Futebol", response.getBody()));
		
		try {
			verify(schedulingService, never()).findAllBySportName(anyString());
			verify(converterService, never()).schedulingToDtos(listEntity);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testGetSchedulingParticipantsValid() {
		try {
			when(schedulingService.findById(anyInt())).thenReturn(entity);
			when(userConverterService.usersToDtos(any())).thenReturn(listUserDto);
		} catch (Exception e) {
			fail();
		}
		
		response = controller.getSchedulingParticipants(1);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.OK, response.getStatusCode()),
				() -> assertEquals(listUserDto, response.getBody()));
	}
	
	@Test
	public void testGetSchedulingParticipantsInalid() {
		try {
			when(schedulingService.findById(anyInt())).thenThrow(new ObjectNotFoundException("Scheduling", "id", entity.getId()));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.getSchedulingParticipants(1);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Could not find Scheduling with id 1", response.getBody()));
		
		verify(userConverterService, never()).usersToDtos(any());
	}
	
	@Test
	public void testAddParticipantValid() {
		try {
			when(schedulingService.findById(anyInt())).thenReturn(entity);
			when(schedulingService.save(any(Scheduling.class))).thenReturn(entity);
			
			when(userService.findByRegistration(anyInt())).thenReturn(Optional.of(user));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.addParticipant(1, 123);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());		
	}
	
	@Test
	public void testAddParticipantInvalidRegistration() {
		try {
			when(schedulingService.findById(anyInt())).thenReturn(entity);
			when(schedulingService.save(any(Scheduling.class))).thenReturn(entity);
			
			when(userService.findByRegistration(anyInt())).thenThrow(new ObjectNotFoundException("User", "registration", 123));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.addParticipant(1, 123);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Could not find User with registration 123", response.getBody()));
		
		try {
			verify(schedulingService, never()).findById(anyInt());	
			verify(schedulingService, never()).save(any(Scheduling.class));	
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testAddParticipantInvalidId() {
		try {
			when(schedulingService.findById(anyInt())).thenThrow(new ObjectNotFoundException("Scheduling", "id", 1));
			when(schedulingService.save(any(Scheduling.class))).thenReturn(entity);
			
			when(userService.findByRegistration(anyInt())).thenReturn(Optional.of(user));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.addParticipant(1, 123);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Could not find Scheduling with id 1", response.getBody()));
		
		verify(schedulingService, never()).save(any(Scheduling.class));	
	}
	
	@Test
	public void testRemoveParticipantValid() {
		try {
			when(schedulingService.findById(anyInt())).thenReturn(entity);
			when(schedulingService.save(any(Scheduling.class))).thenReturn(entity);
			
			when(userService.findByRegistration(anyInt())).thenReturn(Optional.of(user));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.removeParticipant(1, 123);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());		
	}
	
	@Test
	public void testRemoveParticipantInvalidRegistration() {
		try {
			when(schedulingService.findById(anyInt())).thenReturn(entity);
			when(schedulingService.save(any(Scheduling.class))).thenReturn(entity);
			
			when(userService.findByRegistration(anyInt())).thenThrow(new ObjectNotFoundException("User", "registration", 123));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.removeParticipant(1, 123);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Could not find User with registration 123", response.getBody()));
		
		try {
			verify(schedulingService, never()).findById(anyInt());	
			verify(schedulingService, never()).save(any(Scheduling.class));	
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testRemoveParticipantInvalidId() {
		try {
			when(schedulingService.findById(anyInt())).thenThrow(new ObjectNotFoundException("Scheduling", "id", 1));
			when(schedulingService.save(any(Scheduling.class))).thenReturn(entity);
			
			when(userService.findByRegistration(anyInt())).thenReturn(Optional.of(user));
		} catch (Exception e) {
			fail();
		}
		
		response = controller.removeParticipant(1, 123);
		
		assertAll("Asserting HttpStatus and body content",
				() -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
				() -> assertEquals("Could not find Scheduling with id 1", response.getBody()));
		
		verify(schedulingService, never()).save(any(Scheduling.class));	
	}
}
