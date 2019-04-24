package org.mindaugas.github.controller.dto;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

public class GithubSearchReqest {

	private GithubSearchSort sort;

	private GithubSearchOrde order;

	private Integer page = 1;

	private Integer perPage = 10;

	public GithubSearchSort getSort() {
		return sort;
	}

	public void setSort(GithubSearchSort sort) {
		this.sort = sort;
	}

	public GithubSearchOrde getOrder() {
		return order;
	}

	public void setOrder(GithubSearchOrde order) {
		this.order = order;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPerPage() {
		return perPage;
	}

	public void setPerPage(Integer perPage) {
		this.perPage = perPage;
	}

	public Map<String, String> getUriVariables() {
		final Map<String, String> result = new HashedMap();
		result.put("sort", sort.toString());
		result.put("order", order.toString());
		result.put("page", page.toString());
		result.put("perPage", perPage.toString());
		return result;
	}

}
