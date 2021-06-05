package com.example.peliculas.dataAccess;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientInstance {

	private static Retrofit retrofit;
	public static Retrofit getRetrofitInstance() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(15, TimeUnit.SECONDS)
				.readTimeout(120, TimeUnit.SECONDS)
				.writeTimeout(120, TimeUnit.SECONDS)
				.build();

		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					.baseUrl("http://api.themoviedb.org")
					//.baseUrl("http://127.0.0.1:49196/")
					.client(okHttpClient)
					.addConverterFactory(ScalarsConverterFactory.create())
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}
		return retrofit;
	}
}
