package com.example.peliculas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.peliculas.Models.Movies;
import com.example.peliculas.Models.Results;
import com.example.peliculas.dataAccess.RetrofitClientInstance;
import com.example.peliculas.dataAccess.endpoints;
import com.example.peliculas.database.AppDataBase;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener  {

	private RecyclerView mRecyclerView;
	private RecyclerView.LayoutManager recyclerViewLayoutManager;
	private Context context;
	private RecyclerViewAdapterClass adapter;
	private SearchView searchViewTitle;
	PopupWindow popUp;
	private ConstraintLayout constraintLayout;
	private int pages = 1,totalPublication, totalPages;
	private String urlapiImage = "https://image.tmdb.org/t/p/w185/";
	private boolean isLoading = false;
	List<Results> listResults = new ArrayList<>();
	List<Results> listResultsDatabse = new ArrayList<>();
	private ProgressBar progressBar;
	private Aplicacion aplicacion;
	private AppDataBase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		constraintLayout = findViewById(R.id.constrain);
		mRecyclerView = findViewById(R.id.recicleView);
		recyclerViewLayoutManager = new GridLayoutManager(context, 3);
		mRecyclerView.setLayoutManager(recyclerViewLayoutManager);
		mRecyclerView.setHasFixedSize(true);
		searchViewTitle = findViewById(R.id.searchViewTitle);
		searchViewTitle.setOnQueryTextListener(this);
		progressBar = findViewById(R.id.progressBar);
		db = Room.databaseBuilder(getApplicationContext(),
				AppDataBase.class, "database-moviesResults").build();
		aplicacion = (Aplicacion) getApplicationContext();
		AsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				listResults = db.movieDao().getAll();

			}
		});
		if(aplicacion.isConnected(this)){
			cargarPeliculasPopulares(pages);
		}else {
			cargarPelicualasDataBase();
		}


		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				int stateScroll = recyclerView.getScrollState();
				boolean canScroll = recyclerView.canScrollVertically(1);
				int firstItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
				int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

				if (stateScroll == RecyclerView.SCROLL_STATE_IDLE ) {
					if(lastItem == (totalPublication - 1) && firstItem != 0 ){
						if ((totalPublication - 1) == lastItem && pages < totalPages  ){
							progressBar.setVisibility(View.VISIBLE);
							pages ++;
							cargarPeliculasPopulares(pages);

						}else {
							progressBar.setVisibility(View.GONE);
						}
					}else if(firstItem == 0){
						if (pages > 1){
							progressBar.setVisibility(View.VISIBLE);
							pages --;
							cargarPeliculasPopulares(pages);
						}else {
							progressBar.setVisibility(View.GONE);
						}
					}




				}
			}
		});


	}

	private void cargarPelicualasDataBase() {




		AsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				listResults = db.movieDao().getAll();

			}
		});
		adapter = new RecyclerViewAdapterClass(listResults);
		mRecyclerView.setAdapter(adapter);
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		String text = newText;
		if(text != " "){
			adapter.getFilter().filter(text);
		}
		return false;
	}

	private void cargarPeliculasPopulares(int page) {
		String apiKey = "a65d435b94f33462d56b8b5300c7827d";
		isLoading = true;
		endpoints endpoints = RetrofitClientInstance.getRetrofitInstance().create(endpoints.class);
		Call<Movies> movieResultCallback = endpoints.getPopular(apiKey,page);
		// asynchronous call
		movieResultCallback.enqueue(new Callback<Movies>() {
			@Override
			public void onResponse(Call<Movies> call, Response<Movies> response) {
				Movies results = response.body();
				results.getResults();

					totalPages = results.getTotal_pages();
					totalPublication = results.getResults().size();
					adapter = new RecyclerViewAdapterClass(results.getResults());
					mRecyclerView.setAdapter(adapter);
				    progressBar.setVisibility(View.GONE);
				    listResults = results.getResults();

				    if(listResultsDatabse == null){
						saveMovies(listResults);
					}else {

						AsyncTask.execute(new Runnable() {
							@Override
							public void run() {
								db.movieDao().delete();
								saveMovies(listResults);
							}
						});


					}


			}

			@Override
			public void onFailure(Call<Movies> call, Throwable t) {
				Log.v("Error ", String.valueOf(t));
			}
		});

	}

	private void saveMovies(List<Results> listMovieResults) {

		Results resutsMovie;

		for (int i = 0; i < listMovieResults.size(); i++) {
			 resutsMovie = new Results(
					listMovieResults.get(i).getMovieid(),
					listMovieResults.get(i).getAdult(),
					listMovieResults.get(i).getBackdrop_path(),
					listMovieResults.get(i).getOriginalLanguage(),
					listMovieResults.get(i).getOriginalTitle(),
					listMovieResults.get(i).getOverview(),
					listMovieResults.get(i).getPopularity(),
					listMovieResults.get(i).getPosterPath(),
					listMovieResults.get(i).getReleaseDate(),
					listMovieResults.get(i).getTitle(),
					listMovieResults.get(i).getVideo(),
					listMovieResults.get(i).getVoteAverage(),
					listMovieResults.get(i).getVoteCount()
					);


			final Results finalResutsMovie = resutsMovie;
			AsyncTask.execute(new Runnable() {
				@Override
				public void run() {
					db.movieDao().insertAll(finalResutsMovie);
				}
			});

		}

		}






	public class RecyclerViewAdapterClass extends RecyclerView.Adapter<RecyclerViewAdapterClass.MyViewHolder> implements Filterable {
		private List<Results> ArrayListUnits;
		private List<Results> filteredNameList;

		public RecyclerViewAdapterClass(List<Results> modelList) {
			ArrayListUnits = modelList;
			this.filteredNameList = ArrayListUnits;
		}

		@Override
		public RecyclerViewAdapterClass.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view, parent, false);
			return new RecyclerViewAdapterClass.MyViewHolder(view);
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public void onBindViewHolder(final RecyclerViewAdapterClass.MyViewHolder holder, int position) {
			final Results model = filteredNameList.get(position);
			//holder.textView.setText( model.getTitle());
			String urlMovie = model.getBackdrop_path();
			Glide.with(MainActivity.this)
					.load(urlapiImage + urlMovie)
					.centerCrop()
					.into(holder.imageView);


			holder.imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					int position = holder.getAdapterPosition();

					// Inflate the custom layout/view
					View customView = inflater.inflate(R.layout.popup_window,null);


					popUp = new PopupWindow(
							customView,
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT
					);


					if(Build.VERSION.SDK_INT>=21){
						popUp.setElevation(5.0f);
					}


					// Get a reference for the custom view close button
					ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
					TextView textPeliculas = customView.findViewById(R.id.textPeliculas);
					TextView textPelicula = customView.findViewById(R.id.textPelicula);
					ImageView img = customView.findViewById(R.id.img);
					TextView namePelicula = customView.findViewById(R.id.namePelicula);

					textPeliculas.setText(filteredNameList.get(position).getTitle());

					String urlMovie = filteredNameList.get(position).getBackdrop_path();
					Glide.with(MainActivity.this)
							.load(urlapiImage + urlMovie)
							.centerCrop()
							.into(img);

					textPelicula.setText(filteredNameList.get(position).getOverview());
					namePelicula.setText(filteredNameList.get(position).getOriginalTitle());

					// Set a click listener for the popup window close button
					closeButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Dismiss the popup window
							popUp.dismiss();
						}
					});

					popUp.showAtLocation(constraintLayout, Gravity.CENTER,0,0);
				}
			});
 				holder.nameMovie.setText(model.getOriginalTitle());

		}

		@Override
		public int getItemCount() {
			return filteredNameList.size();
		}

		@Override
		public Filter getFilter() {
			return new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					String charSequenceString = constraint.toString();
					if (charSequenceString.isEmpty()) {
						filteredNameList = ArrayListUnits;
					} else {
						List<Results> filteredList = new ArrayList<>();
						for (Results name : ArrayListUnits) {
							if (name.getTitle().toLowerCase().contains(charSequenceString.toLowerCase())) {
								filteredList.add(name);
							}
							filteredNameList = filteredList;
						}
					}
					FilterResults results = new FilterResults();
					results.values = filteredNameList;
					return results;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					filteredNameList = (List<Results>) results.values;
					notifyDataSetChanged();
				}
			};
		}

		public class MyViewHolder extends RecyclerView.ViewHolder {
			private View view;
			private ImageView imageView;
			private TextView nameMovie;

			private MyViewHolder(View itemView) {
				super(itemView);
				view = itemView;
				imageView =  itemView.findViewById(R.id.info_text);
				nameMovie =  itemView.findViewById(R.id.nameMovie);



			}
		}
	}
}





