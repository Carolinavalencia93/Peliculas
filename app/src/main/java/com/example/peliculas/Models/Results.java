package com.example.peliculas.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Results {

	@PrimaryKey(autoGenerate = true)
	int idPelicula;
	@ColumnInfo(name = "adult")
	private Boolean adult;
	@ColumnInfo(name = "backdrop_path")
	private String backdrop_path;
	@ColumnInfo(name = "movieid")
	private int movieid;
	@ColumnInfo(name = "original_language")
	private String original_language;
	@ColumnInfo(name = "original_title")
	private String original_title;
	@ColumnInfo(name = "overview")
	private String overview;
	@ColumnInfo(name = "popularity")
	private Double popularity;
	@ColumnInfo(name = "poster_path")
	private String poster_path;
	@ColumnInfo(name = "release_date")
	private String release_date;
	@ColumnInfo(name = "title")
	private String title;
	@ColumnInfo(name = "video")
	private Boolean video;
	@ColumnInfo(name = "voteAverage")
	private Integer voteAverage;
	@ColumnInfo(name = "voteCount")
	private Integer voteCount;


	public Results(int movieid, Boolean adult, String backdrop_path, String original_language, String original_title, String overview, Double popularity, String poster_path, String release_date, String title, Boolean video, Integer voteAverage, Integer voteCount) {
		this.movieid = movieid;
		this.adult = adult;
		this.backdrop_path = backdrop_path;
		this.original_language = original_language;
		this.original_title = original_title;
		this.overview = overview;
		this.popularity = popularity;
		this.poster_path = poster_path;
		this.release_date = release_date;
		this.title = title;
		this.video = video;
		this.voteAverage = voteAverage;
		this.voteCount = voteCount;
	}

	public Boolean getAdult() {
		return adult;
	}

	public void setAdult(Boolean adult) {
		this.adult = adult;
	}

	public int getIdPelicula() {
		return idPelicula;
	}

	public void setIdPelicula(int idPelicula) {
		this.idPelicula = idPelicula;
	}

	public String getBackdrop_path() {
		return backdrop_path;
	}

	public void setBackdrop_path(String backdrop_path) {
		this.backdrop_path = backdrop_path;
	}

	public int getMovieid() {
		return movieid;
	}

	public void setMovieid(int movieid) {
		this.movieid = movieid;
	}

	public String getOriginal_language() {
		return original_language;
	}

	public void setOriginal_language(String original_language) {
		this.original_language = original_language;
	}

	public String getOriginal_title() {
		return original_title;
	}

	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}

	public String getPoster_path() {
		return poster_path;
	}

	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
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
		return poster_path;
	}

	public void setPosterPath(String posterPath) {
		this.poster_path = posterPath;
	}

	public String getReleaseDate() {
		return release_date;
	}

	public void setReleaseDate(String releaseDate) {
		this.release_date = releaseDate;
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
		this.voteAverage = voteCount;
	}

}

