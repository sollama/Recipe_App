package com.test.recipe_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.test.recipe_app.model.EditRecipeViewModel;
import com.test.recipe_app.model.Recipe;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ViewRecipeActivity extends AppCompatActivity {
    private static final String TAG = "ViewRecipeActivity";

    private String recipeName,
            id;
    private TextView viewTag,
            viewPrepTime,
            viewCookTime,
            viewIngredients,
            viewInstructions;
    private ImageButton viewWantToMake;
    private boolean wantToMake;
    private ImageView viewImage;
    EditRecipeViewModel recipeModel;
    private Bundle bundle;
    private LiveData<Recipe> recipe;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewTag = findViewById(R.id.view_tag);
        viewWantToMake = findViewById(R.id.view_wantToMake);
        viewPrepTime = findViewById(R.id.view_prepTime);
        viewCookTime = findViewById(R.id.view_cookTime);
        viewIngredients = findViewById(R.id.view_ingredients);
        viewInstructions = findViewById(R.id.view_instructions);
        viewImage = findViewById(R.id.view_recipe_image);


        bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("recipe_id");
        }

        recipeModel = ViewModelProviders.of(this).get(EditRecipeViewModel.class);
        recipe = recipeModel.getRecipe(id);


        recipe.observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null) {
                    recipeName = recipe.getName();
                    getSupportActionBar().setTitle(recipeName);
                    Log.d(TAG, "recipeName" + recipeName);
                    Log.d(TAG, "recipe.getName()" + recipe.getName());
                    viewTag.setText(recipe.getTag());
                    viewPrepTime.setText(recipe.getPrepTime());
                    viewCookTime.setText(recipe.getCookTime());
                    viewIngredients.setText(recipe.getIngredients());
                    viewInstructions.setText(recipe.getInstructions());


                    //Log.d(TAG, "recipe.getTag()" + recipe.getTag());


                    if (recipe.getWantToMake()) {
                        wantToMake = true;
                        viewWantToMake.setImageResource(R.drawable.star_24px);
                    } else {
                        wantToMake = false;
                        viewWantToMake.setImageResource(R.drawable.star_outline_24px);
                    }

                    try {
                        //convert & display image
                        Uri imageUri = Uri.parse(recipe.getImage());
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        viewImage.setImageBitmap(bitmap);
                        viewImage.setBackground(null);
                        viewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e(TAG, "recipe null");
                }

            }
        });

        TabHost host = findViewById(R.id.tab_host);
        host.setup();

        //Ingredients Tab
        TabHost.TabSpec spec = host.newTabSpec("Ingredients");
        spec.setContent(R.id.ingredients_tab);
        spec.setIndicator("Ingredients");
        host.addTab(spec);

        //Instructions Tab
        spec = host.newTabSpec("Instructions");
        spec.setContent(R.id.instructions_tab);
        spec.setIndicator("Instructions");
        host.addTab(spec);

        final Typeface typeface = ResourcesCompat.getFont(getApplicationContext(),
                R.font.montserrat_reg);

        final TabWidget tw = host.findViewById(android.R.id.tabs);
        for (int i = 0; i < tw.getChildCount(); ++i)
        {
            final View tabView = tw.getChildTabViewAt(i);
            final TextView tv = (TextView)tabView.findViewById(android.R.id.title);
            tv.setTextSize(18);
            tv.setTypeface(typeface);
        }

    }

}
