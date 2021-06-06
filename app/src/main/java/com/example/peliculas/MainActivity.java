package com.example.peliculas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.peliculas.Models.Movies;
import com.example.peliculas.Models.results;
import com.example.peliculas.dataAccess.RetrofitClientInstance;
import com.example.peliculas.dataAccess.endpoints;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
	List<results> peliculas = new ArrayList<>();
	private ConstraintLayout constraintLayout;
	private int pages = 1,totalPublication, totalPages;
	private String urlapiImage = "https://image.tmdb.org/t/p/w185/";
	private boolean isLoading = false;
	JSONArray pelicuasnews = new JSONArray();
	private ProgressBar progressBar;

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
		cargarPeliculasPopulares(pages);

		mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				int stateScroll = recyclerView.getScrollState();
				boolean canScroll = recyclerView.canScrollVertically(1);
				int firstItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
				int lastItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

				if (stateScroll == RecyclerView.SCROLL_STATE_IDLE ) {

					Log.v("Llego", "llego1");


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




				}else{


					/*
					if (pages > 0){
						pages --;
						cargarPeliculasPopulares(pages);
					}

					 */
					Log.v("Llego", "llego2");
				}
			}
		});


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


			}

			@Override
			public void onFailure(Call<Movies> call, Throwable t) {
				Log.v("Error ", String.valueOf(t));
			}
		});

	}

	public class RecyclerViewAdapterClass extends RecyclerView.Adapter<RecyclerViewAdapterClass.MyViewHolder> implements Filterable {
		private List<results> ArrayListUnits;
		private List<results> filteredNameList;

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
			String urlMovie = model.getBackdropPath();
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

					String urlMovie = filteredNameList.get(position).getBackdropPath();
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





