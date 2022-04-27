package br.edu.ifpb.dac.ssp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectAlreadyExistsException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public boolean existsById(Integer id) {
		return userRepository.existsById(id);
	}
	
	public boolean existsByRegistration(Integer registration) {
		return userRepository.existsByRegistration(registration);
	}
	
	public User findById(Integer id) throws Exception {
		if (!existsById(id)) {
			throw new ObjectNotFoundException("User", "id", id);
		}
		return userRepository.getById(id);
	}
	
	public Optional<User> findByName(String name) throws Exception {
		if (name == null || name.isBlank()) {
			throw new MissingFieldException("name");
		}
		
		if (!userRepository.existsByName(name)) {
			throw new ObjectNotFoundException("User", "name", name);
		}
		return userRepository.findByName(name);
	}
	
	public Optional<User> findByRegistration(Integer registration) throws Exception {
		if (registration == null) {
			throw new MissingFieldException("registration");
		}
		
		if (!existsByRegistration(registration)) {
			throw new ObjectNotFoundException("User", "registration", registration);
		}
		
		return userRepository.findByRegistration(registration);
	}
	
	public User save(User user) throws Exception {
		if (user.getName() == null || user.getName().isBlank()) {
			throw new MissingFieldException("name", "save");
		}
		
		if (existsByRegistration(user.getRegistration())) {
			throw new ObjectAlreadyExistsException("A user with registration " + user.getRegistration() + " already exists!");
		}
		return userRepository.save(user);
	}
	
	public User update(User user) throws Exception {
		if (user.getName() == null || user.getName().isBlank()) {
			throw new MissingFieldException("name", "update");
		}
		
		if (user.getId() == null) {
			throw new MissingFieldException("id", "update");
		} else if (!existsById(user.getId())) {
			throw new ObjectNotFoundException("User", "id", user.getId());
		}
		
		if (existsByRegistration(user.getRegistration())) {
			User userSaved = findByRegistration(user.getRegistration()).get();
			if (userSaved.getId() != user.getId()) {
				throw new ObjectAlreadyExistsException("A user with registration " + user.getRegistration() + " already exists!");
			}
		}
		
		return userRepository.save(user);
	}
	
	public void delete(User user) throws Exception {
		if (user.getId() == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(user.getId())) {
			throw new ObjectNotFoundException("User", "id", user.getId());
		}
		
		userRepository.delete(user);
	}
	
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(id)) {
			throw new ObjectNotFoundException("User", "id", id);
		}
		
		userRepository.deleteById(id);
	}

}
