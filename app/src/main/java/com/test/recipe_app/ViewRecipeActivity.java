package com.test.recipe_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.test.recipe_app.model.EditRecipeViewModel;
import com.test.recipe_app.model.Recipe;

public class ViewRecipeActivity extends AppCompatActivity {
    private static final String TAG = "ViewRecipeActivity";

    private String recipeName;
    private TextView viewTag;
    private TextView viewPrepTime;
    private TextView viewCookTime;
    private TextView viewIngredients;
    private TextView viewInstructions;
    private ImageButton viewWantToMake;
    private boolean wantToMake;
    private TabHost tabHost;
    private ImageView viewImage;


    EditRecipeViewModel recipeModel;
    private Bundle bundle;
    private String id;
    private LiveData<Recipe> recipe;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setTitle(recipeName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewTag = findViewById(R.id.view_tag);
        viewWantToMake = findViewById(R.id.view_wantToMake);
        viewPrepTime = findViewById(R.id.view_prepTime);
        viewCookTime = findViewById(R.id.view_cookTime);
        viewIngredients = findViewById(R.id.view_ingredients);
        viewInstructions = findViewById(R.id.view_instructions);
        viewImage = findViewById(R.id.view_recipe_image);
        //toolbar = findViewById(R.id.toolbar_view);
        //toolbar.Title(recipeName);

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

                } else {
                    Log.e(TAG, "recipe null");
                }

            }
        });

        TabHost host = (TabHost)findViewById(R.id.tab_host);
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


    }

}
