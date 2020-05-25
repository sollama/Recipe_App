package com.test.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.test.recipe_app.model.EditRecipeViewModel;
import com.test.recipe_app.model.Recipe;

public class EditRecipeActivity extends AppCompatActivity {

    private static final String TAG = "EditRecipeActivity";
    public static final String RECIPE_ID = "recipe_id";
    public static final String UPDATED_RECIPE_NAME = "updated_recipe";
    public static final String UPDATED_RECIPE_TAG = "updated_tag";
    public static final String UPDATED_RECIPE_PREPTIME = "updated_prepTime";
    public static final String UPDATED_RECIPE_COOKTIME = "updated_cookTime";
    public static final String UPDATED_RECIPE_INGREDIENTS = "updated_ingredients";
    public static final String UPDATED_RECIPE_INSTRUCTIONS = "updated_instructions";

    private EditText editRecipeName;
    private EditText editTag;
    private EditText editPrepTime;
    private EditText editCookTime;
    private EditText editIngredients;
    private EditText editInstructions;
    private ImageButton editWantToMake;
    EditRecipeViewModel recipeModel;
    private Bundle bundle;
    private String id;
    private LiveData<Recipe> recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //xml underscore
        setContentView(R.layout.activity_edit_recipe);
        editRecipeName = findViewById(R.id.edit_recipe);
        editTag = findViewById(R.id.edit_tag);
        editPrepTime = findViewById(R.id.edit_prepTime);
        editCookTime = findViewById(R.id.edit_cookTime);
        editIngredients = findViewById(R.id.edit_ingredients);
        editInstructions = findViewById(R.id.edit_instructions);
        editWantToMake = findViewById(R.id.edit_wantToMake);

        bundle = getIntent().getExtras();

        if(bundle != null) {
            id = bundle.getString("recipe_id");
        }

        recipeModel = ViewModelProviders.of(this).get(EditRecipeViewModel.class);
        recipe = recipeModel.getRecipe(id);
        Log.d(TAG , "id" + id);


        recipe.observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null) {
                    String recipeName = recipe.getName();
                    String recipeIng = recipe.getIngredients();
                    String recipeInstruc = recipe.getInstructions();
                    String recipePrep = String.valueOf(recipe.getPrepTime());
                    Log.d(TAG, "Name: "+ recipeName + ", Ingredients: " + recipeIng + ", Instructions: " + recipeInstruc);
                    editRecipeName.setText(recipe.getName());
                    editTag.setText(recipe.getTag());
                    editPrepTime.setText(recipe.getPrepTime());
                    editCookTime.setText(recipe.getCookTime());
                    editIngredients.setText(recipe.getIngredients());
                    editInstructions.setText(recipe.getInstructions());
                } else {
                    Log.e(TAG, "recipe null");
                }

            }
        });
    }

    public void updateRecipe(View view) {
        //send updated recipe to Main Activity & update database
        String eRecipeId = id;
        String eRecipeName = editRecipeName.getText().toString();
        String eTag = editTag.getText().toString();
        String ePrepTime = String.valueOf(editPrepTime.getText());
        String eCookTime = String.valueOf(editCookTime.getText());
        String eIngredients = editIngredients.getText().toString();
        String eInstructions = editInstructions.getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(RECIPE_ID, eRecipeId);
        resultIntent.putExtra(UPDATED_RECIPE_NAME, eRecipeName);
        resultIntent.putExtra(UPDATED_RECIPE_TAG, eTag);
        resultIntent.putExtra(UPDATED_RECIPE_PREPTIME, ePrepTime);
        resultIntent.putExtra(UPDATED_RECIPE_COOKTIME, eCookTime);
        resultIntent.putExtra(UPDATED_RECIPE_INGREDIENTS, eIngredients);
        resultIntent.putExtra(UPDATED_RECIPE_INSTRUCTIONS, eInstructions);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void cancelUpdate(View view) {
        finish();
    }
}
