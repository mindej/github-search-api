package org.mindaugas.github.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.mindaugas.github.controller.dto.GithubSearchReqest;
import org.mindaugas.github.controller.dto.GithubSearchResponce;
import org.mindaugas.github.service.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api")
public class SearchController {

	private final GithubService githubService;

	@Autowired
	SearchController(GithubService githubService) {
		this.githubService = githubService;
	}

	@ApiOperation(value = "Github search operation")
	@RequestMapping(value = "/search/language/{language}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	public List<GithubSearchResponce> searchByLanguageType(@PathVariable(value = "language") String language,
			@ModelAttribute() GithubSearchReqest params) {
		return githubService.search(language, params);
	}

}
