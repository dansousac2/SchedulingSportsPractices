package br.edu.ifpb.dac.ssp.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.edu.ifpb.dac.ssp.model.User;

@Service
public class LoginConverterService {

	public String jsonToToken(String json) {
		JsonElement jsonElement = JsonParser.parseString(json);
		String token = jsonElement.getAsJsonObject().get("token").getAsString();
		return token;
	}
	
	//TODO LoginConverter - esse método deve criar o tipo de User (adm, estudent, employee, etc.)
	public User jsonToUser(String json) {
		JsonElement jsonElement = JsonParser.parseString(json);
		JsonObject jsonObject = jsonElement.getAsJsonObject()
											.get("results")
											.getAsJsonArray()
											.get(0)
											.getAsJsonObject();
		
		String name = jsonObject.get("nome").getAsString();
		String registration = jsonObject.get("matricula").getAsString();
		
		User user = new User();
		user.setName(name);
		user.setRegistration(Long.parseLong(registration));

		return user;
	}

	public String mapToJson(Map<String, String> map) {
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}

}
