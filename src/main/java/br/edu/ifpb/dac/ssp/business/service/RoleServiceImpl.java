package br.edu.ifpb.dac.ssp.business.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.entity.Role;
import br.edu.ifpb.dac.ssp.model.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository repository;
	
	@Override
	public void createDefaultValues() {
		for (AVALIABLE_ROLES availableRole : AVALIABLE_ROLES.values()) {
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
			throw new IllegalStateException("Nome não pode ser nulo");
		}
		
		Optional<Role> optional = repository.findByName(name);
		
		return optional.isPresent() ? optional.get() : null;
	}
	
	@Override
	public Role findDefault() {
		return findByName(AVALIABLE_ROLES.USER.name());
	}
	

}
