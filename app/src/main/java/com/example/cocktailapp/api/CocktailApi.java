package com.example.cocktailapp.api;

import com.example.cocktailapp.api.CocktailResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CocktailApi {
    @GET("search.php")
    Call<CocktailResponse> getCocktailsByLetter(@Query("f") String letter);
    @GET("search.php")
    Call<CocktailResponse> searchCocktails(@Query("s") String query);
}

