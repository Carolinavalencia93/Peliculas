package com.example.peliculas.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.peliculas.Models.Movies;
import com.example.peliculas.Models.Results;

import java.util.List;

@Dao
public interface MovieDao {
	@Query("SELECT * FROM results")
	List<Results> getAll();

	@Insert
	void insertAll(Results... results);

	@Query("DELETE FROM results")
	void delete();
}