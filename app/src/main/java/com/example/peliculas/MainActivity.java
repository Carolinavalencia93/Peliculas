package com.example.peliculas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.peliculas.Models.Movies;
import com.example.peliculas.Models.results;
import com.example.peliculas.dataAccess.RetrofitClientInstance;
import com.example.peliculas.dataAccess.endpoints;

import org.json.JSONArray;
import org.json.JSONObject;

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
	boolean click = true;
	private ConstraintLayout constraintLayout;

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
		cargarPeliculasPopulares();
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

	private void cargarPeliculasPopulares() {
		String apiKey = "a65d435b94f33462d56b8b5300c7827d";
		endpoints endpoints = RetrofitClientInstance.getRetrofitInstance().create(endpoints.class);
		Call<Movies> movieResultCallback = endpoints.getPopular(apiKey);
		// asynchronous call
		movieResultCallback.enqueue(new Callback<Movies>() {
			@Override
			public void onResponse(Call<Movies> call, Response<Movies> response) {



				//int code = response.code();
				// can check the status code
				Log.v("String ", String.valueOf(response.body()));
				Movies results = response.body();
				results.getResults();
				Log.v(" Movies ", String.valueOf(results.getResults()));
				//mAdapter.setMovieList(response.body().getResults());
				adapter = new RecyclerViewAdapterClass(results.getResults());
				mRecyclerView.setAdapter(adapter);


			}

			@Override
			public void onFailure(Call<Movies> call, Throwable t) {
				Log.v("String ", String.valueOf(call));
				Log.v("String ", String.valueOf(t));
			}
		});

	}

	public class RecyclerViewAdapterClass extends RecyclerView.Adapter<RecyclerViewAdapterClass.MyViewHolder> implements Filterable {
		private List<results> ArrayListUnits;
		private List<results> filteredNameList;
		String[] values;

		public RecyclerViewAdapterClass(List<results> modelList) {
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
			final results model = filteredNameList.get(position);
			//holder.textView.setText( model.getTitle());
			String urlapi = "https://image.tmdb.org/t/p/w185/";
			String urlMovie = model.getBackdropPath();
			Glide.with(MainActivity.this)
					.load(urlapi + urlMovie)
					.centerCrop()
					.into(holder.imageView);

			//	https://image.tmdb.org/t/p/w185/{{movies.poster_path}}

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

					textPeliculas.setText(filteredNameList.get(position).getTitle());
					String urlapi = "https://image.tmdb.org/t/p/w185/";
					String urlMovie = filteredNameList.get(position).getBackdropPath();
					Glide.with(MainActivity.this)
							.load(urlapi + urlMovie)
							.centerCrop()
							.into(img);

					textPelicula.setText(filteredNameList.get(position).getOverview());

					// Set a click listener for the popup window close button
					closeButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Dismiss the popup window
							popUp.dismiss();
						}
					});

                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
					// Finally, show the popup window at the center location of root relative layout
					popUp.showAtLocation(constraintLayout, Gravity.CENTER,0,0);
				}
			});


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
						List<results> filteredList = new ArrayList<>();
						for (results name : ArrayListUnits) {
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
					filteredNameList = (List<results>) results.values;
					notifyDataSetChanged();
				}
			};
		}

		public class MyViewHolder extends RecyclerView.ViewHolder {
			private View view;
			private ImageView imageView;

			private MyViewHolder(View itemView) {
				super(itemView);
				view = itemView;
				imageView =  itemView.findViewById(R.id.info_text);



			}
		}
	}
}





