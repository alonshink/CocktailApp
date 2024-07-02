package com.example.cocktailapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cocktailapp.CocktailAdapter;
import com.example.cocktailapp.R;
import com.example.cocktailapp.api.CocktailApi;
import com.example.cocktailapp.api.CocktailResponse;
import com.example.cocktailapp.model.Cocktail;
import com.example.cocktailapp.ui.favorites.FavoritesViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CocktailAdapter adapter;
    private List<Cocktail> cocktailList;
    private FavoritesViewModel favoritesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        favoritesViewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);
        adapter = new CocktailAdapter(new ArrayList<>(), this::onCocktailClick, this::onFavoriteClick, favoritesViewModel.getFavoriteCocktails());
        recyclerView.setAdapter(adapter);

        fetchCocktails();

        favoritesViewModel.getFavoriteCocktails().observe(getViewLifecycleOwner(), favoriteCocktails -> {
            adapter.setFavoriteCocktails(favoriteCocktails);
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    private void fetchCocktails() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CocktailApi api = retrofit.create(CocktailApi.class);
        Call<CocktailResponse> call = api.getCocktailsByLetter("s");

        call.enqueue(new Callback<CocktailResponse>() {
            @Override
            public void onResponse(Call<CocktailResponse> call, Response<CocktailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cocktailList = response.body().getCocktails();
                    adapter.setCocktailList(cocktailList);
                }
            }

            @Override
            public void onFailure(Call<CocktailResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void onCocktailClick(Cocktail cocktail) {
        Intent intent = new Intent(getContext(), com.example.cocktailapp.DetailedActivity.class);
        intent.putExtra("cocktail", cocktail);
        startActivity(intent);
    }

    private void onFavoriteClick(Cocktail cocktail) {
        if (favoritesViewModel.isFavorite(cocktail)) {
            favoritesViewModel.removeFavorite(cocktail);
        } else {
            favoritesViewModel.addFavorite(cocktail);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            favoritesViewModel.loadFavoritesFromFirestore();
            adapter.notifyDataSetChanged(); // Notify adapter to refresh the UI
        }
    }
}
