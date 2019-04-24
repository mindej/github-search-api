package org.mindaugas.github.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.mindaugas.github.service.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api")
public class StarController {

	private final GithubService githubService;

	@Autowired
	StarController(GithubService githubService) {
		this.githubService = githubService;
	}

	@ApiOperation(value = "Github add star operation")
	@RequestMapping(value = "/repository/star/{user}/{repo}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
	public String addStar(@PathVariable(value = "user") String user, @PathVariable(value = "repo") String repo) {
		return githubService.addStar(user, repo);
	}

	@ApiOperation(value = "Github delete star operation")
	@RequestMapping(value = "/repository/star/{user}/{repo}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
	public String deleteStar(@PathVariable(value = "user") String user, @PathVariable(value = "repo") String repo) {
		return githubService.deleteStar(user, repo);
	}

}
