package br.edu.ifpb.dac.ssp.service;

import br.edu.ifpb.dac.ssp.model.Role;

public interface RoleService {
	
	public enum AVAILABLE_ROLES { ADMIN, USER }

	public void createDefaultValues();

	public Role findByName(String name);

	public Role findDefault();

}

