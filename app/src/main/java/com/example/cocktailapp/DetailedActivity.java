package com.example.cocktailapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.cocktailapp.R;
import com.example.cocktailapp.model.Cocktail;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        ImageView imageView = findViewById(R.id.detailed_image);
        TextView nameTextView = findViewById(R.id.detailed_name);
        TextView instructionsTextView = findViewById(R.id.detailed_instructions);
        TextView ingredientsTextView = findViewById(R.id.detailed_ingredients);

        Cocktail cocktail = (Cocktail) getIntent().getSerializableExtra("cocktail");

        if (cocktail != null) {
            nameTextView.setText(cocktail.getStrDrink());
            instructionsTextView.setText(cocktail.getStrInstructions());
            Glide.with(this).load(cocktail.getStrDrinkThumb()).into(imageView);

            StringBuilder ingredients = new StringBuilder();
            if (cocktail.getStrIngredient1() != null) ingredients.append(cocktail.getStrIngredient1()).append("\n");
            if (cocktail.getStrIngredient2() != null) ingredients.append(cocktail.getStrIngredient2()).append("\n");
            if (cocktail.getStrIngredient3() != null) ingredients.append(cocktail.getStrIngredient3()).append("\n");
            if (cocktail.getStrIngredient4() != null) ingredients.append(cocktail.getStrIngredient4()).append("\n");
            if (cocktail.getStrIngredient5() != null) ingredients.append(cocktail.getStrIngredient5()).append("\n");
            if (cocktail.getStrIngredient6() != null) ingredients.append(cocktail.getStrIngredient6()).append("\n");
            if (cocktail.getStrIngredient7() != null) ingredients.append(cocktail.getStrIngredient7()).append("\n");
            if (cocktail.getStrIngredient8() != null) ingredients.append(cocktail.getStrIngredient8()).append("\n");
            if (cocktail.getStrIngredient9() != null) ingredients.append(cocktail.getStrIngredient9()).append("\n");
            if (cocktail.getStrIngredient10() != null) ingredients.append(cocktail.getStrIngredient10()).append("\n");
            if (cocktail.getStrIngredient11() != null) ingredients.append(cocktail.getStrIngredient11()).append("\n");
            if (cocktail.getStrIngredient12() != null) ingredients.append(cocktail.getStrIngredient12()).append("\n");
            if (cocktail.getStrIngredient13() != null) ingredients.append(cocktail.getStrIngredient13()).append("\n");
            if (cocktail.getStrIngredient14() != null) ingredients.append(cocktail.getStrIngredient14()).append("\n");
            if (cocktail.getStrIngredient15() != null) ingredients.append(cocktail.getStrIngredient15()).append("\n");

            ingredientsTextView.setText(ingredients.toString().trim());
        }
    }
}
