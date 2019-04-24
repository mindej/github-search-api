package org.mindaugas.github.controller.dto;

public enum GithubSearchSort {
	stars("stars"), forks("forks"), helpWantedIssues("help-wanted-issues");

	private String value;

	GithubSearchSort(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
