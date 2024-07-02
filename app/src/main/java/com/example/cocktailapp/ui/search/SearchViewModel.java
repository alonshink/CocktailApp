package com.example.cocktailapp.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cocktailapp.api.CocktailApi;
import com.example.cocktailapp.api.RetrofitClient;
import com.example.cocktailapp.model.Cocktail;
import com.example.cocktailapp.api.CocktailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<List<Cocktail>> cocktails;

    public SearchViewModel() {
        cocktails = new MutableLiveData<>();
    }

    public LiveData<List<Cocktail>> getCocktails() {
        return cocktails;
    }

    public void searchCocktails(String query) {
        CocktailApi api = RetrofitClient.getInstance().getApi();
        api.searchCocktails(query).enqueue(new Callback<CocktailResponse>() {
            @Override
            public void onResponse(Call<CocktailResponse> call, Response<CocktailResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    cocktails.setValue(response.body().getCocktails());
            }

            @Override
            public void onFailure(Call<CocktailResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
