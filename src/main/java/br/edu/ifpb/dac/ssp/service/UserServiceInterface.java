package br.edu.ifpb.dac.ssp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.edu.ifpb.dac.ssp.model.User;

public interface UserServiceInterface extends UserDetailsService {

	List<User> findAll();

	boolean existsById(Integer id);

	boolean existsByRegistration(Long registration);

	User findById(Integer id) throws Exception;

	Optional<User> findByName(String name) throws Exception;

	Optional<User> findByRegistration(Long registration) throws Exception;

	User save(User user) throws Exception;

	User update(User user) throws Exception;

	void delete(User user) throws Exception;

	void deleteById(Integer id) throws Exception;

}
