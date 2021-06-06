package com.example.peliculas.Models;

import java.util.List;

public class Movies {

	private int page;
	private List<results> results;
	private int total_pages;

	public Movies(int page, List<results> results) {
		this.page = page;
		this.results = results;
	}

	public int getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<com.example.peliculas.Models.results> getResults() {
		return results;
	}

	public void setResults(List<com.example.peliculas.Models.results> results) {
		this.results = results;
	}
}
