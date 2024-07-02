package com.example.cocktailapp.api;

import com.example.cocktailapp.model.Cocktail;
import java.util.List;

public class CocktailResponse {
    private List<Cocktail> drinks;

    public List<Cocktail> getCocktails() {
        return drinks;
    }
}
