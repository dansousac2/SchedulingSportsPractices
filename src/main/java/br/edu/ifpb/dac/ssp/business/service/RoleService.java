package br.edu.ifpb.dac.ssp.business.service;

import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.ssp.model.entity.Role;

@Service
public interface RoleService {

	public enum AVALIABLE_ROLES { ADMIN, USER, STUDENT, EMPLOYEE }

	public void createDefaultValues();

	public Role findByName(String name);

	public Role findDefault();

}