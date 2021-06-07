package com.example.peliculas;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Aplicacion extends Application {



		public boolean isConnected(Context context) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
		}

	}