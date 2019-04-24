package org.mindaugas.github.client;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections.map.HashedMap;
import org.mindaugas.github.client.dto.Contributor;
import org.mindaugas.github.client.dto.GithubSearchRoot;
import org.mindaugas.github.client.dto.GithubStarred;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubClient {
	Logger logger = LoggerFactory.getLogger(GithubClient.class);

	@Value("${github.url}")
	private String githubUrl;

	@Value("${github.token}")
	private String githubToken;

	private final RestTemplate restTemplate;

	@Autowired
	public GithubClient(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public GithubSearchRoot getRepositories(String query, Map<String, String> uriVariables) {
		logger.info("Get Repositories: " + query);
		uriVariables.put("query", query);
		final String path = "/search/repositories?q=language:{query}&sort={sort}&order={order}&page={page}&per_page={perPage}";
		try {
			final HttpEntity<GithubSearchRoot> response = restTemplate.exchange(githubUrl + path, HttpMethod.GET,
					getAutenticationHeaders(), GithubSearchRoot.class, uriVariables);
			return response.getBody();
		} catch (final RestClientException e) {
			logger.error("Get Repositories failed: " + e.getMessage());
			throw new GithubClientException("Get Repositories failed: " + e.getMessage());
		}
	}

	public Map<String, GithubStarred> getUserStarred() {
		logger.info("Get UserStarred: ");
		final String path = "/user/starred";
		try {
			final HttpEntity<GithubStarred[]> response = restTemplate.exchange(githubUrl + path, HttpMethod.GET,
					getAutenticationHeaders(), GithubStarred[].class);
			final GithubStarred[] result = response.getBody();
			final Map<String, GithubStarred> resultMap = new HashedMap();
			for (final GithubStarred item : result) {
				resultMap.put(item.getFullName(), item);
			}
			return resultMap;
		} catch (final RestClientException e) {
			logger.error("Get UserStarred failed: " + e.getMessage());
			throw new GithubClientException("UserStarred failed: " + e.getMessage());
		}
	}

	public Contributor[] getContributors(String url) {
		logger.info("Get Contributors: " + url);
		try {
			final HttpEntity<Contributor[]> response = restTemplate.exchange(url, HttpMethod.GET,
					getAutenticationHeaders(), Contributor[].class);
			return response.getBody();
		} catch (final RestClientException e) {
			logger.error("Get Contributors failed: " + e.getMessage());
			throw new GithubClientException("Contributors failed: " + e.getMessage());
		}
	}

	@Async
	public CompletableFuture<Map<String, GithubStarred>> getUserStarredAsync() {
		logger.info("(Async) Get UserStarred: ");
		try {
			return CompletableFuture.completedFuture(getUserStarred());
		} catch (final GithubClientException e) {
			final CompletableFuture<Map<String, GithubStarred>> completableFuture = new CompletableFuture<Map<String, GithubStarred>>();
			completableFuture.completeExceptionally(e);
			return completableFuture;
		}
	}

	@Async
	public CompletableFuture<Contributor[]> getContributorsAsync(String url) throws InterruptedException {
		logger.info("(Async) Get Contributors: " + url);
		try {
			return CompletableFuture.completedFuture(getContributors(url));
		} catch (final GithubClientException e) {
			final CompletableFuture<Contributor[]> completableFuture = new CompletableFuture<Contributor[]>();
			completableFuture.completeExceptionally(e);
			return completableFuture;
		}
	}

	private HttpEntity<Object> getAutenticationHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0");
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "token " + githubToken);
		return new HttpEntity<Object>(headers);
	}

	public String addStar(String repository) {
		logger.info("Add Star: " + repository);
		final String path = "/user/starred/";
		try {
			final HttpEntity<String> response = restTemplate.exchange(githubUrl + path + repository, HttpMethod.PUT,
					getAutenticationHeaders(), String.class);
			return response.getBody() == null ? "OK" : response.getBody();
		} catch (final RestClientException e) {
			logger.error("Add Star failed: " + e.getMessage());
			throw new GithubClientException("Add Star failed: " + e.getMessage());
		}
	}

	public String deleteStar(String repository) {
		logger.info("Delete Star: " + repository);
		final String path = "/user/starred/";
		try {
			final HttpEntity<String> response = restTemplate.exchange(githubUrl + path + repository, HttpMethod.DELETE,
					getAutenticationHeaders(), String.class);
			return response.getBody() == null ? "OK" : response.getBody();
		} catch (final RestClientException e) {
			logger.error("Delete Star failed: " + e.getMessage());
			throw new GithubClientException("Delete Star failed: " + e.getMessage());
		}
	}

}
