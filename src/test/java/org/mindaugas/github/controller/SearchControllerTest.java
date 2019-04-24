package org.mindaugas.github.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindaugas.github.controller.dto.GithubSearchResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, value = "github.url=http://localhost:${wiremock.server.port}")
@AutoConfigureWireMock(port = 8081)
public class SearchControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test(expected = RestClientException.class)
	public void searchByType_Fail() {
		final String path = "/api/search/language/test-query_fail?order=desc&page=1&perPage=10&sort=stars";
		this.restTemplate.getForObject(path, GithubSearchResponce[].class);
	}

	@Test
	public void searchByType() {
		final String path = "/api/search/language/test-query?order=desc&page=1&perPage=10&sort=stars";
		final GithubSearchResponce[] result = this.restTemplate.getForObject(path, GithubSearchResponce[].class);

		assertThat(result).isNotNull();

		validate(result, getExpectedGithubSearchResponce());
	}

	private void validate(GithubSearchResponce[] result, GithubSearchResponce[] expected) {
		assertThat(result.length).isEqualTo(2);

		assertThat(result[0].getFullName()).isEqualTo(expected[0].getFullName());
		assertThat(result[0].getDescription()).isEqualTo(expected[0].getDescription());
		assertThat(result[0].getUrl()).isEqualTo(expected[0].getUrl());
		assertThat(result[0].getContributorCount()).isEqualTo(expected[0].getContributorCount());
		assertThat(result[0].getStarsCount()).isEqualTo(expected[0].getStarsCount());
		assertThat(result[0].getHasUserStar()).isEqualTo(expected[0].getHasUserStar());
		assertThat(result[0].getLicenseName()).isEqualTo(expected[0].getLicenseName());

		assertThat(result[1].getFullName()).isEqualTo(expected[1].getFullName());
		assertThat(result[1].getDescription()).isEqualTo(expected[1].getDescription());
		assertThat(result[1].getUrl()).isEqualTo(expected[1].getUrl());
		assertThat(result[1].getContributorCount()).isEqualTo(expected[1].getContributorCount());
		assertThat(result[1].getStarsCount()).isEqualTo(expected[1].getStarsCount());
		assertThat(result[1].getHasUserStar()).isEqualTo(expected[1].getHasUserStar());
		assertThat(result[1].getLicenseName()).isEqualTo(expected[1].getLicenseName());
	}

	private GithubSearchResponce[] getExpectedGithubSearchResponce() {
		final GithubSearchResponce[] data = new GithubSearchResponce[2];

		data[0] = new GithubSearchResponce();
		data[0].setFullName("botpress/Boost");
		data[0].setDescription("test description1");
		data[0].setUrl("http://localhost:8081/repos/test1");
		data[0].setContributorCount(2);
		data[0].setStarsCount(123);
		data[0].setHasUserStar(true);
		data[0].setLicenseName("license-test1");

		data[1] = new GithubSearchResponce();
		data[1].setFullName("iluwatar/java-design-patterns");
		data[1].setDescription("test description2");
		data[1].setUrl("http://localhost:8081/repos/test2");
		data[1].setContributorCount(3);
		data[1].setStarsCount(345);
		data[1].setHasUserStar(false);
		data[1].setLicenseName("license-test2");

		return data;
	}
}
