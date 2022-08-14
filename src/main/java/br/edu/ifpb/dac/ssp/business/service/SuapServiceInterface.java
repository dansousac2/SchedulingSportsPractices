package br.edu.ifpb.dac.ssp.business.service;

import java.util.Map;

public interface SuapServiceInterface {
	
	// URLs usadas
	public static final String OBTAIN_TOKEN_URL = "https://suap.ifpb.edu.br/api/jwt/obtain_token/";
	public static final String EMPLOYEES_URL = "https://suap.ifpb.edu.br/api/recursos-humanos/servidores/v1/";
	public static final String STUDENTS_URL = "https://suap.ifpb.edu.br/api/ensino/alunos/v1/";
	
	// Campos de login
	public static final String USERNAME_JSON_FIELD = "username";
	public static final String PASSWORD_JSON_FIELD = "password";
	
	// Dados do header de autenticação
	public static final String TOKEN_HEADER_NAME = "Authorization";
	public static final String TOKEN_HEADER_VALUE = "JWT %s";
	
	// Header default
	public static final Map<String, String> DEFAULT_HEADERS = Map.of("Content-type", "application/json");
	
	// * * Métodos * *
	
	// Obter token
	public String login(String username, String password);
	
	// Encontra todos os servidores
	public String findEmployee(String token);
	// Encontra servidor específico
	public String findEmployee(String token, String username);
	
	// Encontra todos os alunos
	public String findStudent(String token);
	// Encontra aluno específico
	public String findStudent(String token, String username);
	
	// Encontrar usuário de qualquer tipo
	public String findUser(String token, String username);
}
