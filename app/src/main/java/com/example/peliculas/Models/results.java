package com.example.peliculas.Models;

import java.util.List;

public class results {

	private Boolean adult;
	private String backdrop_path;
	private List<Integer> genreIds = null;
	private Integer id;
	private String original_language;
	private String original_title;
	private String overview;
	private Double popularity;
	private String posterPath;
	private String releaseDate;
	private String title;
	private Boolean video;
	private Integer voteAverage;
	private Integer voteCount;

	public Boolean getAdult() {
		return adult;
	}

	public void setAdult(Boolean adult) {
		this.adult = adult;
	}

	public String getBackdropPath() {
		return backdrop_path;
	}

	public void setBackdropPath(String backdropPath) {
		this.backdrop_path = backdrop_path;
	}

	public List<Integer> getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(List<Integer> genreIds) {
		this.genreIds = genreIds;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOriginalLanguage() {
		return original_language;
	}

	public void setOriginalLanguage(String originalLanguage) {
		this.original_language = originalLanguage;
	}

	public String getOriginalTitle() {
		return original_title;
	}

	public void setOriginalTitle(String originalTitle) {
		this.original_title = original_title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public Double getPopularity() {
		return popularity;
	}

	public void setPopularity(Double popularity) {
		this.popularity = popularity;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getVideo() {
		return video;
	}

	public void setVideo(Boolean video) {
		this.video = video;
	}

	public Integer getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage(Integer voteAverage) {
		this.voteAverage = voteAverage;
	}

	public Integer getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Integer voteCount) {
		this.voteCount = voteCount;
	}

}

