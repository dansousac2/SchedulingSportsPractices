package br.edu.ifpb.dac.ssp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.exception.MissingFieldException;
import br.edu.ifpb.dac.ssp.exception.ObjectAlreadyExistsException;
import br.edu.ifpb.dac.ssp.exception.ObjectNotFoundException;
import br.edu.ifpb.dac.ssp.model.Role;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PasswordEncoderService passwordEncoderService;
	
	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	@Override
	public boolean existsById(Integer id) {
		return userRepository.existsById(id);
	}
	
	@Override
	public boolean existsByRegistration(Long registration) {
		return userRepository.existsByRegistration(registration);
	}
	
	@Override
	public User findById(Integer id) throws Exception {
		if (!existsById(id)) {
			throw new ObjectNotFoundException("usuário", "id", id);
		}
		return userRepository.getById(id);
	}
	
	@Override
	public Optional<User> findByName(String name) throws Exception {
		if (name == null || name.isBlank()) {
			throw new MissingFieldException("nome");
		}
		
		if (!userRepository.existsByName(name)) {
			throw new ObjectNotFoundException("usuário", "nome", name);
		}
		return userRepository.findByName(name);
	}
	
	@Override
	public Optional<User> findByRegistration(Long registration) throws Exception {
		if (registration == null) {
			throw new MissingFieldException("matrícula");
		}
		
		if (!existsByRegistration(registration)) {
			throw new ObjectNotFoundException("usuário", "matrícula", registration);
		}
		
		return userRepository.findByRegistration(registration);
	}
	
	@Override
	public User save(User user) throws Exception {
		if (user.getName() == null || user.getName().isBlank()) {
			throw new MissingFieldException("nome", "save");
		}
		
		if (existsByRegistration(user.getRegistration())) {
			throw new ObjectAlreadyExistsException("Já existe um usuário com matrícula " + user.getRegistration());
		}
		
		passwordEncoderService.encryptPassword(user);
		List<Role> roles = new ArrayList<>();
		roles.add(roleService.findDefault());
		
		String registration = String.valueOf(user.getRegistration());
		// Separando usuários entre estudantes e servidores de acordo com a matrícula
		if (registration.length() == 5) {
			roles.add(roleService.findByName("EMPLOYEE"));
		} else {
			roles.add(roleService.findByName("STUDENT"));
		}
		
		user.setRoles(roles);
		
		return userRepository.save(user);
	}
	
	@Override
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
	
	@Override
	public void delete(User user) throws Exception {
		if (user.getId() == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(user.getId())) {
			throw new ObjectNotFoundException("usuário", "id", user.getId());
		}
		
		userRepository.delete(user);
	}
	
	@Override
	public void deleteById(Integer id) throws Exception {
		if (id == null) {
			throw new MissingFieldException("id", "delete");
		} else if (!existsById(id)) {
			throw new ObjectNotFoundException("usuário", "id", id);
		}
		
		userRepository.deleteById(id);
	}

	// Adicionei aqui só porque tava dando erro
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
