package br.edu.ifpb.dac.ssp.service;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.Role;

@Service
public interface RoleService {

	public enum AVALIABLE_ROLES { ADMIN, USER, STUDENT, EMPLOYEE }

	public void createDefaultValues();

	public Role findByName(String name);

	public Role findDefault();

}