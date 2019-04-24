package org.mindaugas.github.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, value = "github.url=http://localhost:${wiremock.server.port}")
@AutoConfigureWireMock(port = 8081)
public class StarControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void addStar_Fail() {
		final String path = "/api/repository/star/test-user1/test-repo1";
		final ResponseEntity<String> response = this.restTemplate.exchange(path, HttpMethod.PUT,
				getAutenticationHeaders(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	public void addStar() {
		final String path = "/api/repository/star/test-user/test-repo";
		final ResponseEntity<String> response = this.restTemplate.exchange(path, HttpMethod.PUT,
				getAutenticationHeaders(), String.class);
		assertThat(response.getBody()).isEqualTo("OK");
	}

	@Test
	public void deleteStar_Fail() {
		final String path = "/api/repository/star/test-user1/test-repo1";
		final ResponseEntity<String> response = this.restTemplate.exchange(path, HttpMethod.DELETE,
				getAutenticationHeaders(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	public void deleteStar() {
		final String path = "/api/repository/star/test-user/test-repo";
		final ResponseEntity<String> response = this.restTemplate.exchange(path, HttpMethod.DELETE,
				getAutenticationHeaders(), String.class);
		assertThat(response.getBody()).isEqualTo("OK");
	}

	private HttpEntity<Object> getAutenticationHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0");
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return new HttpEntity<Object>(headers);
	}
}
