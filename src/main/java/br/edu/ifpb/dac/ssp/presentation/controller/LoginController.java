package br.edu.ifpb.dac.ssp.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import br.edu.ifpb.dac.ssp.business.service.LoginService;
import br.edu.ifpb.dac.ssp.business.service.TokenService;
import br.edu.ifpb.dac.ssp.business.service.UserConverterService;
import br.edu.ifpb.dac.ssp.business.service.UserService;
import br.edu.ifpb.dac.ssp.model.entity.User;
import br.edu.ifpb.dac.ssp.presentation.dto.LoginDTO;
import br.edu.ifpb.dac.ssp.presentation.dto.TokenDTO;
import br.edu.ifpb.dac.ssp.presentation.dto.UserDTO;

@RestController
@RequestMapping("/api")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class LoginController {
	
	@Autowired
	private LoginService service;
	@Autowired
	private UserConverterService userConverter;
	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginDTO dto) {
		try {
			String token = service.login(dto.getUsername(), dto.getPassword());
			User entity = userService.findByRegistration(Long.valueOf(dto.getUsername())).get();
			UserDTO userDTO = userConverter.userToDto(entity);
			
			TokenDTO tokenDTO = new TokenDTO(token, userDTO);
			
			return new ResponseEntity(tokenDTO, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/isValidToken")
	public ResponseEntity isValidToken(@RequestBody TokenDTO tokenDto) {
		try {
			boolean isValidToken = tokenService.isValid(tokenDto.getToken());
			
			return new ResponseEntity(isValidToken, HttpStatus.OK);
		} catch(Exception e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
