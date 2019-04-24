package org.mindaugas.github.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.dozermapper.core.Mapping;

@JsonNaming(SnakeCaseStrategy.class)
public class GithubSearchResponce {

	private String fullName;
	private String description;

	@Mapping("license.name")
	private String LicenseName;

	private String url;

	private Integer contributorCount;

	@Mapping("stargazersCount")
	private Integer starsCount;

	private Boolean hasUserStar;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLicenseName() {
		return LicenseName;
	}

	public void setLicenseName(String licenseName) {
		LicenseName = licenseName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getContributorCount() {
		return contributorCount;
	}

	public void setContributorCount(Integer contributorCount) {
		this.contributorCount = contributorCount;
	}

	public Integer getStarsCount() {
		return starsCount;
	}

	public void setStarsCount(Integer starsCount) {
		this.starsCount = starsCount;
	}

	public Boolean getHasUserStar() {
		return hasUserStar;
	}

	public void setHasUserStar(Boolean hasUserStar) {
		this.hasUserStar = hasUserStar;
	}

}
