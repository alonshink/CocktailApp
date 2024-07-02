package com.example.cocktailapp.ui.search;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import com.example.cocktailapp.CocktailAdapter;
import com.example.cocktailapp.DetailedActivity;
import com.example.cocktailapp.R;
import com.example.cocktailapp.model.Cocktail;
import com.example.cocktailapp.ui.favorites.FavoritesViewModel;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private CocktailAdapter cocktailAdapter;
    private FavoritesViewModel favoritesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        favoritesViewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);

        RecyclerView recyclerView = root.findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cocktailAdapter = new CocktailAdapter(new ArrayList<>(), this::onCocktailClick, this::onFavoriteClick, favoritesViewModel.getFavoriteCocktails());
        recyclerView.setAdapter(cocktailAdapter);

        EditText editTextSearch = root.findViewById(R.id.search_edit_text);
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            String query = editTextSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                searchViewModel.searchCocktails(query);
            }
            return true;
        });

        searchViewModel.getCocktails().observe(getViewLifecycleOwner(), cocktails -> {
            if (cocktails != null) {
                cocktailAdapter.setCocktailList(cocktails);
            }
        });

        favoritesViewModel.getFavoriteCocktails().observe(getViewLifecycleOwner(), favoriteCocktails -> {
            cocktailAdapter.setFavoriteCocktails(favoriteCocktails);
            cocktailAdapter.notifyDataSetChanged();
        });

        return root;
    }

    private void onCocktailClick(Cocktail cocktail) {
        Intent intent = new Intent(getContext(), DetailedActivity.class);
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
        if (cocktailAdapter != null) {
            favoritesViewModel.loadFavoritesFromFirestore();
            cocktailAdapter.notifyDataSetChanged(); // Notify adapter to refresh the UI
        }
    }
}
