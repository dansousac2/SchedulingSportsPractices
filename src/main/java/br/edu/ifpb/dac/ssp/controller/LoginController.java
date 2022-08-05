package br.edu.ifpb.dac.ssp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.model.dto.LoginDTO;
import br.edu.ifpb.dac.ssp.model.dto.TokenDTO;
import br.edu.ifpb.dac.ssp.model.dto.UserDTO;
import br.edu.ifpb.dac.ssp.service.LoginService;
import br.edu.ifpb.dac.ssp.service.TokenService;
import br.edu.ifpb.dac.ssp.service.UserConverterService;
import br.edu.ifpb.dac.ssp.service.UserService;

@RestController
@RequestMapping("/api")
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserConverterService userConverter;
	@Autowired
	private UserService userService;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginDTO dto) {
		try {
			String token = loginService.login(dto.getUsername(), dto.getPassword());
			User entity = userService.findByRegistration(Long.parseLong(dto.getUsername())).get();
			UserDTO userDto = userConverter.userToDto(entity);
			
			TokenDTO tokenDto = new TokenDTO(token, userDto);
			
			return new ResponseEntity(tokenDto, HttpStatus.OK);
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
