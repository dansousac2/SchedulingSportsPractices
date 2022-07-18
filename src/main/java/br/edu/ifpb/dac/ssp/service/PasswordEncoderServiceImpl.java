package br.edu.ifpb.dac.ssp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.User;

@Service
public class PasswordEncoderServiceImpl extends BCryptPasswordEncoder implements PasswordEncoderService {

	@Override
	public void encryptPassword(User user) {
		if(user.getPassword() != null) {
			String encryptedPassword = encode(user.getPassword());
			user.setPassword(encryptedPassword);
		}
		
	}
	

}

