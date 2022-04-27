package br.edu.ifpb.dac.ssp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.ssp.model.Scheduling;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.model.dto.SchedulingDTO;
import br.edu.ifpb.dac.ssp.model.dto.UserDTO;
import br.edu.ifpb.dac.ssp.service.SchedulingConverterService;
import br.edu.ifpb.dac.ssp.service.SchedulingService;
import br.edu.ifpb.dac.ssp.service.SchedulingValidatorService;
import br.edu.ifpb.dac.ssp.service.UserConverterService;
import br.edu.ifpb.dac.ssp.service.UserService;

@RestController
@RequestMapping("api/scheduling")

public class SchedulingController {

	@Autowired
	private SchedulingService schedulingService;

	@Autowired
	private SchedulingConverterService converterService;

	@Autowired
	private SchedulingValidatorService validatorService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserConverterService userConverterService;

	@GetMapping
	public ResponseEntity getAll() {
		try {
			List<Scheduling> entityList = schedulingService.findAll();

			List<SchedulingDTO> dtoList = converterService.schedulingToDtos(entityList);

			return ResponseEntity.ok().body(dtoList);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable Integer id) {
		try {
			Scheduling entity = schedulingService.findById(id);
			SchedulingDTO dto = converterService.schedulingToDto(entity);

			return ResponseEntity.ok().body(dto);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity save(@RequestBody SchedulingDTO dto) {

		try {
			validatorService.validateSchedulingDTO(dto);
			Scheduling entity = converterService.dtoToScheduling(dto);

			validatorService.validateScheduling(entity);
			entity = schedulingService.save(entity);

			dto = converterService.schedulingToDto(entity);

			return new ResponseEntity(dto, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/confirmedByPlace/{place}")
	public ResponseEntity getAllSchedulingConfirmedByPlace(@PathVariable String place) {
		try {
			validatorService.validatePlaceName(place);
			List<Scheduling> entityList = schedulingService.findAllByPlaceName(place);
			List<SchedulingDTO> dtoList = converterService.schedulingToDtos(entityList);

			return ResponseEntity.ok().body(dtoList);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/confirmedBySport/{sport}")
	public ResponseEntity getAllSchedulingConfirmedBySport(@PathVariable String sport) {
		try {
			validatorService.validateSportName(sport);
			List<Scheduling> entityList = schedulingService.findAllBySportName(sport);
			List<SchedulingDTO> dtoList = converterService.schedulingToDtos(entityList);

			return ResponseEntity.ok().body(dtoList);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/participation/{id}")
	public ResponseEntity getSchedulingParticipants(@PathVariable Integer id) {
		try {
			Scheduling entity = schedulingService.findById(id);

			List<User> participantList = new ArrayList<>();
			participantList.addAll(entity.getParticipants());

			List<UserDTO> participantListDTO = userConverterService.usersToDtos(participantList);

			return ResponseEntity.ok().body(participantListDTO);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PatchMapping("/participation/add/{id}")
	public ResponseEntity addParticipant(@PathVariable Integer id, @RequestBody Integer userRegistration) {
		try {
			User user = userService.findByRegistration(userRegistration).orElse(null);
			Scheduling entity = schedulingService.findById(id);

			if (user != null) {
				entity.addParticipant(user);
				entity = schedulingService.save(entity);
			}

			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PatchMapping("/participation/remove/{id}")
	public ResponseEntity removeParticipant(@PathVariable Integer id, @RequestBody Integer userRegistration) {
		try {
			User user = userService.findByRegistration(userRegistration).orElse(null);
			Scheduling entity = schedulingService.findById(id);

			if (user != null) {
				entity.removeParticipant(user);
				entity = schedulingService.save(entity);
			}

			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		try {
			schedulingService.deleteById(id);

			return ResponseEntity.noContent().build();

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
