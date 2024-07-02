package com.example.cocktailapp.ui.favorites;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cocktailapp.R;
import com.example.cocktailapp.model.Cocktail;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private FavoritesViewModel favoritesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoritesViewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);
        favoriteAdapter = new FavoriteAdapter(favoritesViewModel.getFavoriteCocktails().getValue(), favoritesViewModel, getContext());
        recyclerView.setAdapter(favoriteAdapter);

        favoritesViewModel.getFavoriteCocktails().observe(getViewLifecycleOwner(), favoriteAdapter::setFavoriteCocktails);

        Button removeAllButton = view.findViewById(R.id.remove_all_button);
        removeAllButton.setOnClickListener(v -> {
            favoritesViewModel.clearFavorites();
            Toast.makeText(getContext(), "All favorites removed", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

        private List<Cocktail> favoriteCocktails;
        private FavoritesViewModel favoritesViewModel;
        private Context context;

        FavoriteAdapter(List<Cocktail> favoriteCocktails, FavoritesViewModel favoritesViewModel, Context context) {
            this.favoriteCocktails = favoriteCocktails;
            this.favoritesViewModel = favoritesViewModel;
            this.context = context;
        }

        void setFavoriteCocktails(List<Cocktail> favoriteCocktails) {
            this.favoriteCocktails = favoriteCocktails;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
            return new FavoriteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
            Cocktail cocktail = favoriteCocktails.get(position);

            holder.cocktailName.setText(cocktail.getStrDrink());
            Glide.with(holder.itemView.getContext()).load(cocktail.getStrDrinkThumb()).into(holder.cocktailImage);

            holder.removeIcon.setOnClickListener(v -> {
                favoritesViewModel.removeFavorite(cocktail);
                favoriteCocktails.remove(cocktail);
                notifyDataSetChanged();
                if (context != null) {
                    Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                }
            });

            holder.updateFavoriteIcon(cocktail);

            holder.cocktailImage.setOnClickListener(v -> {
                long clickTime = System.currentTimeMillis();
                if (clickTime - holder.lastClickTime < 400) {
                    if (favoritesViewModel.isFavorite(cocktail)) {
                        favoritesViewModel.removeFavorite(cocktail);
                    } else {
                        favoritesViewModel.addFavorite(cocktail);
                    }
                    notifyDataSetChanged();
                }
                holder.lastClickTime = clickTime;
            });
        }

        @Override
        public int getItemCount() {
            return favoriteCocktails != null ? favoriteCocktails.size() : 0;
        }

        class FavoriteViewHolder extends RecyclerView.ViewHolder {

            TextView cocktailName;
            ImageView cocktailImage;
            ImageView favoriteIcon;
            ImageView removeIcon;
            long lastClickTime = 0;

            FavoriteViewHolder(View itemView) {
                super(itemView);

                cocktailName = itemView.findViewById(R.id.cocktail_name);
                cocktailImage = itemView.findViewById(R.id.cocktail_image);
                favoriteIcon = itemView.findViewById(R.id.ic_favorite);
                removeIcon = itemView.findViewById(R.id.ic_remove);
            }

            void updateFavoriteIcon(Cocktail cocktail) {
                if (favoritesViewModel.isFavorite(cocktail)) {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_red);
                } else {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        }
    }
}
