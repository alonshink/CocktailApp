package com.example.cocktailapp.ui.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cocktailapp.model.Cocktail;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritesViewModel extends ViewModel {
    private final MutableLiveData<List<Cocktail>> favoriteCocktails = new MutableLiveData<>(new ArrayList<>());
    private final FirebaseFirestore db;

    public FavoritesViewModel() {
        db = FirebaseFirestore.getInstance();
        loadFavoritesFromFirestore();
    }

    public LiveData<List<Cocktail>> getFavoriteCocktails() {
        return favoriteCocktails;
    }

    public void addFavorite(Cocktail cocktail) {
        List<Cocktail> currentFavorites = favoriteCocktails.getValue();
        if (currentFavorites != null && !isFavorite(cocktail)) {
            currentFavorites.add(cocktail);
            favoriteCocktails.setValue(currentFavorites);
            addToFirestore(cocktail);
        }
    }

    public void removeFavorite(Cocktail cocktail) {
        List<Cocktail> currentFavorites = favoriteCocktails.getValue();
        if (currentFavorites != null && isFavorite(cocktail)) {
            currentFavorites.remove(cocktail);
            favoriteCocktails.setValue(currentFavorites);
            removeFromFirestore(cocktail.getIdDrink());
        }
    }

    public boolean isFavorite(Cocktail cocktail) {
        List<Cocktail> currentFavorites = favoriteCocktails.getValue();
        if (currentFavorites != null) {
            for (Cocktail fav : currentFavorites) {
                if (fav.getIdDrink().equals(cocktail.getIdDrink())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addToFirestore(Cocktail cocktail) {
        db.collection("favorites")
                .document(cocktail.getIdDrink())
                .set(cocktail)
                .addOnSuccessListener(aVoid -> {})
                .addOnFailureListener(e -> {});
    }

    private void removeFromFirestore(String cocktailId) {
        db.collection("favorites")
                .document(cocktailId)
                .delete()
                .addOnSuccessListener(aVoid -> {})
                .addOnFailureListener(e -> {});
    }

    public void clearFavorites() {
        List<Cocktail> currentFavorites = favoriteCocktails.getValue();
        if (currentFavorites != null) {
            for (Cocktail cocktail : currentFavorites) {
                removeFromFirestore(cocktail.getIdDrink());
            }
            currentFavorites.clear();
            favoriteCocktails.setValue(currentFavorites);
        }
    }

    public void loadFavoritesFromFirestore() {
        db.collection("favorites")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Cocktail> favorites = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Cocktail cocktail = document.toObject(Cocktail.class);
                            favorites.add(cocktail);
                        }
                        favoriteCocktails.setValue(favorites);
                    }
                });
    }
}
