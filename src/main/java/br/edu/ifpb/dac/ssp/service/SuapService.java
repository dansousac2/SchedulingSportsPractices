package br.edu.ifpb.dac.ssp.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuapService implements SuapServiceInterface {

	@Autowired
	private LoginConverterService loginConverter;

	@Override
	public String login(String username, String password) {
		Map body = Map.of(USERNAME_JSON_FIELD, username, PASSWORD_JSON_FIELD, password);

		String json = loginConverter.mapToJson(body);

		try {
			HttpRequest url = generatePostUrl(OBTAIN_TOKEN_URL, null, json);
			return sendRequestUrl(url);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (InterruptedException e3) {
			e3.printStackTrace();
		}

		return null;
	}

	private HttpRequest generatePostUrl(String url, Map<String, String> headers, String body)
			throws URISyntaxException {
		Builder builder = HttpRequest.newBuilder().uri(new URI(url));

		if (DEFAULT_HEADERS != null) {
			for (Map.Entry<String, String> header : DEFAULT_HEADERS.entrySet()) {
				builder.setHeader(header.getKey(), header.getValue());
			}
		}

		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				builder.setHeader(header.getKey(), header.getValue());
			}
		}

		HttpRequest request = builder.POST(BodyPublishers.ofString(body)).build();

		return request;
	}

	private HttpRequest generateGetUrl(String url, Map<String, String> headers) throws URISyntaxException {
		Builder builder = HttpRequest.newBuilder().uri(new URI(url));

		for (Map.Entry<String, String> header : DEFAULT_HEADERS.entrySet()) {
			builder.setHeader(header.getKey(), header.getValue());
		}

		for (Map.Entry<String, String> header : headers.entrySet()) {
			builder.setHeader(header.getKey(), header.getValue());
		}
		
		HttpRequest request = builder.GET().build();

		return request;
	}

	private String sendRequestUrl(HttpRequest httpRequest) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newHttpClient();
		String response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
		return response;
	}
	
	@Override
	public String findUser(String token, String username) {
		String result = findEmployee(token, username);
		System.out.println("\nretorno de findEmployee em findUser: " + result); //teste
		if(result.contains("\"count\":0")) {
			result = findStudent(token, username);
			System.out.println("\nretorno de findStudent em findUser: " + result); //teste
		}
		return result;
	}
	
	@Override
	public String findEmployee(String token, String username) {
		String url = String.format("%s?search=%s", EMPLOYEES_URL, username);
		return find(token, url);
	}

	@Override
	public String findStudent(String token, String username) {
		String url = String.format("%s?search=%s", STUDENTS_URL, username);

		return find(token, url);
	}

	@Override
	public String findEmployee(String token) {
		return null;
	}

	@Override
	public String findStudent(String token) {
		return null;
	}

	private String find(String token, String findUrl) {
		try {
			HttpRequest url = generateGetUrl(findUrl, Map.of(TOKEN_HEADER_NAME, String.format(TOKEN_HEADER_VALUE, token)));
			return sendRequestUrl(url);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch(IOException e2) {
			e2.printStackTrace();
		} catch(InterruptedException e3) {
			e3.printStackTrace();
		}
		
		return null;
	}

}
