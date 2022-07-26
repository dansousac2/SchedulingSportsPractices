package br.edu.ifpb.dac.ssp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.Role;
import br.edu.ifpb.dac.ssp.repository.RoleRepository;

@Service
public class RoleService implements RoleServiceInterface {
	
	@Autowired
	private RoleRepository repository;
	
	@Override
	public void createDefaultValues() {
		for (AVAILABLE_ROLES availableRole : AVAILABLE_ROLES.values()) {
			Role role = findByName(availableRole.name());
			
			if(role == null) {
				role = new Role();
				role.setName(availableRole.name());
				repository.save(role);
			}
		}
	}
	
	@Override
	public Role findByName(String name) {
		if(name == null) {
			throw new IllegalStateException("Name cannot be null");
		}
		
		Optional<Role> optional = repository.findByName(name);
		
		return optional.isPresent() ? optional.get() : null;
	}
	
	@Override
	public Role findDefault() {
		return findByName(AVAILABLE_ROLES.USER.name());
	}
	

}
