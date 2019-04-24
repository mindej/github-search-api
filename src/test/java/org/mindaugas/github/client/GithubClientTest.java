package org.mindaugas.github.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindaugas.github.client.dto.Contributor;
import org.mindaugas.github.client.dto.GithubSearchRoot;
import org.mindaugas.github.client.dto.GithubStarred;
import org.mindaugas.github.client.dto.Item;
import org.mindaugas.github.client.dto.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, value = "github.url=http://localhost:${wiremock.server.port}")
@AutoConfigureWireMock(port = 8081)
public class GithubClientTest {

	@Autowired
	GithubClient githubClient;

	@Value("${github.url}")
	private String githubUrl;

	@Test
	public void getUserStarred() {
		final Map<String, GithubStarred> result = githubClient.getUserStarred();
		assertThat(result.size()).isEqualTo(2);

		final GithubStarred[] mockedData = getMockedDataStarred();
		validateStarred(result.get("botpress/Boost"), mockedData[0]);
		validateStarred(result.get("botpress/botpress-examples"), mockedData[1]);
	}

	@Test
	public void getUserStarredAsync() throws InterruptedException, ExecutionException {
		final CompletableFuture<Map<String, GithubStarred>> githubStarredFuture = githubClient.getUserStarredAsync();
		CompletableFuture.allOf(githubStarredFuture).join();
		final Map<String, GithubStarred> result = githubStarredFuture.get();

		assertThat(result.size()).isEqualTo(2);

		final GithubStarred[] mockedData = getMockedDataStarred();
		validateStarred(result.get("botpress/Boost"), mockedData[0]);
		validateStarred(result.get("botpress/botpress-examples"), mockedData[1]);
	}

	private GithubStarred[] getMockedDataStarred() {
		final GithubStarred[] data = new GithubStarred[2];
		data[0] = new GithubStarred();
		data[0].setId(76992484);
		data[0].setFullName("botpress/Boost");

		data[1] = new GithubStarred();
		data[1].setId(74048282);
		data[1].setFullName("botpress/botpress-examples");
		return data;

	}

	private void validateStarred(GithubStarred returned, GithubStarred expected) {
		assertThat(returned.getId()).isEqualTo(expected.getId());
		assertThat(returned.getFullName()).isEqualTo(expected.getFullName());
	}

	@Test
	public void getContributors() {
		final String url = githubUrl + "/contributors/mock";
		final Contributor[] result = githubClient.getContributors(url);

		assertThat(result.length).isEqualTo(2);

		final Contributor[] expected = getMockedDataContributors();
		for (int i = 0; i < result.length; i++) {
			validateContributor(result[i], expected[i]);
		}
	}

	@Test
	public void getContributorsAsync() throws InterruptedException, ExecutionException {
		final String url = githubUrl + "/contributors/mock";
		final CompletableFuture<Contributor[]> githubFuture = githubClient.getContributorsAsync(url);
		CompletableFuture.allOf(githubFuture).join();

		final Contributor[] result = githubFuture.get();

		assertThat(result.length).isEqualTo(2);

		final Contributor[] expected = getMockedDataContributors();
		for (int i = 0; i < result.length; i++) {
			validateContributor(result[i], expected[i]);
		}
	}

	@Test(expected = GithubClientException.class)
	public void getContributors_withInvalidResponce() {
		final String url = githubUrl + "/contributors/mock_403";
		githubClient.getContributors(url);
	}

	private Contributor[] getMockedDataContributors() {
		final Contributor[] data = new Contributor[2];
		data[0] = new Contributor();
		data[0].setId(463230);

		data[1] = new Contributor();
		data[1].setId(207870);
		return data;
	}

	private void validateContributor(Contributor returned, Contributor expected) {
		assertThat(returned.getId()).isEqualTo(expected.getId());

	}

	@Test
	public void getRepositories() {
		final Map<String, String> uriVariables = new HashedMap();
		uriVariables.put("sort", "sort-test");
		uriVariables.put("order", "order-test");
		uriVariables.put("page", "page-test");
		uriVariables.put("perPage", "per-page-test");

		final GithubSearchRoot result = githubClient.getRepositories("test-query", uriVariables);

		assertThat(result).isNotNull();

		validateGithubSearchRoot(result, getMockedGithubSearchRoot());

	}

	private void validateGithubSearchRoot(GithubSearchRoot result, GithubSearchRoot expected) {
		assertThat(result.getTotalCount()).isEqualTo(expected.getTotalCount());

		assertThat(result.getItems()).isNotNull();
		assertThat(result.getItems().length).isEqualTo(2);

		final Item resultItem1 = result.getItems()[0];
		final Item expectedItem1 = expected.getItems()[0];

		assertThat(resultItem1.getContributorsUrl()).isEqualTo(expectedItem1.getContributorsUrl());
		assertThat(resultItem1.getDescription()).isEqualTo(expectedItem1.getDescription());
		assertThat(resultItem1.getFullName()).isEqualTo(expectedItem1.getFullName());
		assertThat(resultItem1.getLicense()).isNotNull();
		assertThat(resultItem1.getLicense().getName()).isEqualTo(expectedItem1.getLicense().getName());
		assertThat(resultItem1.getUrl()).isEqualTo(expectedItem1.getUrl());

		final Item resultItem2 = result.getItems()[1];
		final Item expectedItem2 = expected.getItems()[1];

		assertThat(resultItem2.getContributorsUrl()).isEqualTo(expectedItem2.getContributorsUrl());
		assertThat(resultItem2.getDescription()).isEqualTo(expectedItem2.getDescription());
		assertThat(resultItem2.getFullName()).isEqualTo(expectedItem2.getFullName());
		assertThat(resultItem2.getLicense()).isNotNull();
		assertThat(resultItem2.getLicense().getName()).isEqualTo(expectedItem2.getLicense().getName());
		assertThat(resultItem2.getUrl()).isEqualTo(expectedItem2.getUrl());
	}

	private GithubSearchRoot getMockedGithubSearchRoot() {
		final GithubSearchRoot data = new GithubSearchRoot();
		data.setTotalCount(6202854);

		data.setItems(new Item[2]);

		final Item i1 = new Item();
		data.getItems()[0] = i1;
		i1.setFullName("botpress/Boost");
		i1.setDescription("test description1");
		i1.setStargazersCount(123);
		i1.setLicense(new License("license-test1"));
		i1.setContributorsUrl("http://localhost:8081/contributors/mock");
		i1.setUrl("http://localhost:8081/repos/test1");

		final Item i2 = new Item();
		data.getItems()[1] = i2;
		i2.setFullName("iluwatar/java-design-patterns");
		i2.setDescription("test description2");
		i2.setStargazersCount(345);
		i2.setLicense(new License("license-test2"));
		i2.setContributorsUrl("http://localhost:8081/contributors/mock");
		i2.setUrl("http://localhost:8081/repos/test2");

		return data;
	}

	@Test
	public void addStar() {
		final String result = githubClient.addStar("test-user/test-repo");
		assertThat(result).isEqualTo("OK");
	}

	@Test(expected = GithubClientException.class)
	public void addStar_whenRepoIsInvalid() {
		githubClient.addStar("test-user1/test-repo1");
	}

	@Test
	public void deleteStar() {
		final String result = githubClient.deleteStar("test-user/test-repo");
		assertThat(result).isEqualTo("OK");
	}

	@Test(expected = GithubClientException.class)
	public void deleteStarr_whenRepoIsInvalid() {
		githubClient.deleteStar("test-user1/test-repo1");
	}

}
