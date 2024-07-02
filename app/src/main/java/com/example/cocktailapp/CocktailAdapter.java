package com.example.cocktailapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cocktailapp.model.Cocktail;
import com.example.cocktailapp.ui.favorites.FavoritesViewModel;
import java.util.List;

public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder> {

    private List<Cocktail> cocktailList;
    private final OnCocktailClickListener onCocktailClickListener;
    private final OnFavoriteClickListener onFavoriteClickListener;
    private List<Cocktail> favoriteCocktails;

    public CocktailAdapter(List<Cocktail> cocktailList, OnCocktailClickListener onCocktailClickListener, OnFavoriteClickListener onFavoriteClickListener, LiveData<List<Cocktail>> favoriteCocktailsLiveData) {
        this.cocktailList = cocktailList;
        this.onCocktailClickListener = onCocktailClickListener;
        this.onFavoriteClickListener = onFavoriteClickListener;
        this.favoriteCocktails = favoriteCocktailsLiveData.getValue();
        favoriteCocktailsLiveData.observeForever(newFavorites -> {
            this.favoriteCocktails = newFavorites;
            notifyDataSetChanged();
        });
    }

    public void setCocktailList(List<Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
        notifyDataSetChanged();
    }

    public void setFavoriteCocktails(List<Cocktail> favoriteCocktails) {
        this.favoriteCocktails = favoriteCocktails;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CocktailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cocktail, parent, false);
        return new CocktailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailViewHolder holder, int position) {
        Cocktail cocktail = cocktailList.get(position);
        holder.bind(cocktail, onCocktailClickListener, onFavoriteClickListener);
    }

    @Override
    public int getItemCount() {
        return cocktailList != null ? cocktailList.size() : 0;
    }

    private boolean isFavorite(Cocktail cocktail) {
        if (favoriteCocktails == null) {
            return false;
        }
        for (Cocktail fav : favoriteCocktails) {
            if (fav.getIdDrink().equals(cocktail.getIdDrink())) {
                return true;
            }
        }
        return false;
    }

    class CocktailViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final ImageView infoIcon;
        private final ImageView favoriteIcon;
        private final TextView textView;
        private long lastClickTime = 0;

        CocktailViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            infoIcon = itemView.findViewById(R.id.ic_info);
            favoriteIcon = itemView.findViewById(R.id.ic_favorite);
            textView = itemView.findViewById(R.id.text_view);
        }

        void bind(Cocktail cocktail, OnCocktailClickListener onCocktailClickListener, OnFavoriteClickListener onFavoriteClickListener) {
            textView.setText(cocktail.getStrDrink());
            String imageUrl = cocktail.getStrDrinkThumb();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemView.getContext()).load(imageUrl).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.placeholder_image);
            }

            updateFavoriteIcon(cocktail);

            infoIcon.setOnClickListener(v -> onCocktailClickListener.onCocktailClick(cocktail));

            View.OnClickListener favoriteClickListener = v -> {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < 400) {
                    onFavoriteClickListener.onFavoriteClick(cocktail);
                    updateFavoriteIcon(cocktail);
                    lastClickTime = 0; // Reset the last click time to prevent multiple toggles
                } else {
                    lastClickTime = clickTime;
                }
            };

            favoriteIcon.setOnClickListener(favoriteClickListener);
            imageView.setOnClickListener(favoriteClickListener);
        }

        private void updateFavoriteIcon(Cocktail cocktail) {
            if (isFavorite(cocktail)) {
                favoriteIcon.setImageResource(R.drawable.ic_favorite_red);
            } else {
                favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
            }
        }
    }

    public interface OnCocktailClickListener {
        void onCocktailClick(Cocktail cocktail);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Cocktail cocktail);
    }
}
