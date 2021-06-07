package com.example.peliculas.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.peliculas.Models.Movies;
import com.example.peliculas.Models.Results;

@Database(entities = {Results.class}, version = 1)
public abstract class AppDataBase  extends RoomDatabase {
	public abstract MovieDao movieDao();
}
