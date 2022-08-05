package br.edu.ifpb.dac.ssp.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.ifpb.dac.ssp.model.User;

public interface PasswordEncoderService extends PasswordEncoder {

	void encriptyPassword(User user);
}
