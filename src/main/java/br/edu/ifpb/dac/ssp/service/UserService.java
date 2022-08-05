package br.edu.ifpb.dac.ssp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectAlreadyExistsException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.model.Role;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoderService passwordEncoderService;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public boolean existsById(Integer id) {
		return userRepository.existsById(id);
	}
	
	public boolean existsByRegistration(Long registration) {
		return userRepository.existsByRegistration(registration);
	}
	
	public User findById(Integer id) throws Exception {
		if (!existsById(id)) {
			throw new ObjectNotFoundException("usuário", "id", id);
		}
		return userRepository.getById(id);
	}
	
	public Optional<User> findByName(String name) throws Exception {
		if (name == null || name.isBlank()) {
			throw new MissingFieldException("nome");
		}
		
		if (!userRepository.existsByName(name)) {
			throw new ObjectNotFoundException("usuário", "nome", name);
		}
		return userRepository.findByName(name);
	}
	
	public Optional<User> findByRegistration(Long registration) throws Exception {
		if (registration == null) {
			throw new MissingFieldException("matrícula");
		}
		
		if (!existsByRegistration(registration)) {
			throw new ObjectNotFoundException("usuário", "matrícula", registration);
		}
		
		return userRepository.findByRegistration(registration);
	}
	
	public User save(User user) throws Exception {
		if (user.getName() == null || user.getName().isBlank()) {
			throw new MissingFieldException("nome", "save");
		}
		
		if (existsByRegistration(user.getRegistration())) {
			throw new ObjectAlreadyExistsException("Já existe um usuário com matrícula " + user.getRegistration());
		}
		
		passwordEncoderService.encriptyPassword(user);
		
		return userRepository.save(user);
	}
	
	public User update(User user) throws Exception {
		if (user.getName() == null || user.getName().isBlank()) {
			throw new MissingFieldException("nome", "update");
		}
		
		if (user.getId() == null) {
			throw new MissingFieldException("id", "update");
		} else if (!existsById(user.getId())) {
			throw new ObjectNotFoundException("usuário", "id", user.getId());
		}
		
		if (existsByRegistration(user.getRegistration())) {
			User userSaved = findByRegistration(user.getRegistration()).get();
			if (userSaved.getId() != user.getId()) {
				throw new ObjectAlreadyExistsException("Já existe um usuário com matrícula " + user.getRegistration());
			}
		}
		
		return userRepository.save(user);
	}
	
	public void delete(User user) throws Exception {
		if (user.getId() == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(user.getId())) {
			throw new ObjectNotFoundException("usuário", "id", user.getId());
		}
		
		userRepository.delete(user);
	}
	
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(id)) {
			throw new ObjectNotFoundException("usuário", "id", id);
		}
		
		userRepository.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = findByRegistration(Long.parseLong(username)).get();
			return user;
		} catch (Exception e) {
			throw new UsernameNotFoundException("Não pode ser encontrado nenhum usuário com matrícula :" + username);
		}
	}

}
