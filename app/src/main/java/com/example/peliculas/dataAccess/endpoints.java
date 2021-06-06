package com.example.peliculas.dataAccess;



import com.example.peliculas.Models.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface endpoints {

	@GET("/3/movie/popular/")
	Call<Movies> getPopular(@Query("api_key") String key, @Query("page") int pages);
}
