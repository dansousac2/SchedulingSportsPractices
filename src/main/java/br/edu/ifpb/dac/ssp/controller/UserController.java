package br.edu.ifpb.dac.ssp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.model.dto.UserDTO;
import br.edu.ifpb.dac.ssp.service.UserConverterService;
import br.edu.ifpb.dac.ssp.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserConverterService converterService;
	
	@GetMapping
	public ResponseEntity getAll() {
		List<User> entityList = userService.findAll();
		
		List<UserDTO> dtoList = converterService.usersToDtos(entityList);
		
		return ResponseEntity.ok().body(dtoList);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity findById(@Valid @PathVariable Integer id) {
		
		try {
			User entity = userService.findById(id);
			UserDTO dto = converterService.userToDto(entity);
			
			return ResponseEntity.ok().body(dto);
		
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity save(@Valid @RequestBody UserDTO dto) {
		
		try {
			User entity = converterService.dtoToUser(dto);
			entity = userService.save(entity);
			dto = converterService.userToDto(entity);
			
			return new ResponseEntity(dto, HttpStatus.CREATED);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity update(@PathVariable Integer id, @Valid @RequestBody UserDTO dto) {
		
		try {
			dto.setId(id);
			User entity = converterService.dtoToUser(dto);
			entity = userService.update(entity);
			dto = converterService.userToDto(entity);
			
			return ResponseEntity.ok().body(dto);
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable Integer id) {
		
		try {
			userService.deleteById(id);
			
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
