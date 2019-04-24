package org.mindaugas.github.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.mindaugas.github.client.GithubClient;
import org.mindaugas.github.client.dto.Contributor;
import org.mindaugas.github.client.dto.GithubSearchRoot;
import org.mindaugas.github.client.dto.GithubStarred;
import org.mindaugas.github.client.dto.Item;
import org.mindaugas.github.controller.dto.GithubSearchReqest;
import org.mindaugas.github.controller.dto.GithubSearchResponce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubService {

	Logger logger = LoggerFactory.getLogger(GithubService.class);

	private final GithubClient githubClient;
	private final DozerMapperService mapper;

	@Autowired
	GithubService(GithubClient githubClient, DozerMapperService mapper) {

		this.githubClient = githubClient;
		this.mapper = mapper;
	}

	@SuppressWarnings("unchecked")
	public List<GithubSearchResponce> search(String query, GithubSearchReqest params) {

		final CompletableFuture<Map<String, GithubStarred>> githubStarredFuture = githubClient.getUserStarredAsync();

		final GithubSearchRoot resultSearchRoot = githubClient.getRepositories(query, params.getUriVariables());
		final List<GithubSearchResponce> responce = new ArrayList<GithubSearchResponce>();

		final CompletableFuture<Contributor[]>[] completableFutures = (CompletableFuture<Contributor[]>[]) new CompletableFuture<?>[resultSearchRoot
				.getItems().length];

		for (int i = 0; i < resultSearchRoot.getItems().length; i++) {
			final Item item = resultSearchRoot.getItems()[i];
			try {
				completableFutures[i] = githubClient.getContributorsAsync(item.getContributorsUrl());
			} catch (final InterruptedException e) {
				logger.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
			final GithubSearchResponce mappedData = mapper.map(item, GithubSearchResponce.class);
			responce.add(mappedData);
		}

		CompletableFuture.allOf(completableFutures).join();
		CompletableFuture.allOf(githubStarredFuture).join();

		updateCuntributorsCountAndStras(responce, completableFutures, githubStarredFuture);

		return responce;
	}

	private void updateCuntributorsCountAndStras(final List<GithubSearchResponce> responce,
			final CompletableFuture<Contributor[]>[] completableFutures,
			CompletableFuture<Map<String, GithubStarred>> githubStarredFuture) {
		try {
			final Map<String, GithubStarred> userGithubStarredMap = githubStarredFuture.get();
			for (int i = 0; i < completableFutures.length; i++) {
				final GithubSearchResponce item = responce.get(i);
				item.setContributorCount(completableFutures[i].get().length);
				item.setHasUserStar(userGithubStarredMap.get(item.getFullName()) != null);
			}
		} catch (final InterruptedException e) {
			logger.error("InterruptedException: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (final ExecutionException e) {
			logger.error("ExecutionException: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

	public String addStar(String user, String repo) {
		return githubClient.addStar(user + "/" + repo);
	}

	public String deleteStar(String user, String repo) {
		return githubClient.deleteStar(user + "/" + repo);
	}

}
