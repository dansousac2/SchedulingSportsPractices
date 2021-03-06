package br.edu.ifpb.dac.ssp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.edu.ifpb.dac.ssp.model.Role;
import br.edu.ifpb.dac.ssp.model.User;
import br.edu.ifpb.dac.ssp.service.RoleService.AVAILABLE_ROLES;

@Service
public class LoginConverterService {
	
	@Autowired
	private RoleServiceImpl roleService;
	
	public String jsonToToken(String json) {
		JsonElement jsonElement = JsonParser.parseString(json);
		String token = jsonElement.getAsJsonObject().get("token").getAsString();
		return token;
	}
	
	public User jsonToUser(String json) {
		JsonElement jsonElement = JsonParser.parseString(json);
		JsonObject jsonObject = jsonElement.getAsJsonObject()
											.get("results")
											.getAsJsonArray()
											.get(0)
											.getAsJsonObject();
		
		String name = jsonObject.get("nome").getAsString();
		String registration = jsonObject.get("matricula").getAsString();
		JsonElement office = jsonObject.get("cargo_emprego");

		List<Role> roles = new ArrayList<>();
		roles.add(roleService.findDefault());
		
		if(office == null) {
			roles.add(roleService.findByName(RoleService.AVAILABLE_ROLES.STUDENT.name()));
		} else {
			roles.add(roleService.findByName(RoleService.AVAILABLE_ROLES.EMPLOYEE.name()));
		}
		
		User user = new User();
		user.setName(name);
		user.setRegistration(Long.parseLong(registration));
		user.setRoles(roles);
		
		return user;
	}

	public String mapToJson(Map<String, String> map) {
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}

}
