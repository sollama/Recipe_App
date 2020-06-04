package com.test.recipe_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.recipe_app.model.Recipe;
import com.test.recipe_app.model.RecipeViewModel;
import com.test.recipe_app.ui.RecipeAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnDeleteClickListener {

    private static final String TAG = "MainActivity";

    private static final int NEW_RECIPE_REQUEST_CODE = 1;
    public static final int UPDATE_RECIPE_ACTIVITY_REQUEST_CODE = 2;
    public static final int VIEW_RECIPE_ACTIVITY_REQUEST_CODE = 3;
    private RecipeAdapter recipeAdapter;
    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recipeAdapter = new RecipeAdapter(this, this);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //create new recipe
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewRecipeActivity.class);
                startActivityForResult(intent, NEW_RECIPE_REQUEST_CODE);
            }
        });

        recipeViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                //update the cached copy of recipes in the adapter
                recipeAdapter.setRecipe(recipes);
                Log.d(TAG, "Recipes: " + recipes);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_RECIPE_REQUEST_CODE && resultCode == RESULT_OK) {
            final String recipe_id = UUID.randomUUID().toString();

            Recipe recipe = new Recipe(recipe_id, data.getStringExtra(NewRecipeActivity.EXTRA_RECIPE_NAME),
                    data.getStringExtra(NewRecipeActivity.EXTRA_RECIPE_TAG),
                    data.getBooleanExtra(NewRecipeActivity.EXTRA_RECIPE_WANTTOMAKE, true),
                    data.getStringExtra(NewRecipeActivity.EXTRA_RECIPE_PREPTIME),
                    data.getStringExtra(NewRecipeActivity.EXTRA_RECIPE_COOKTIME),
                    data.getStringExtra(NewRecipeActivity.EXTRA_RECIPE_INGREDIENTS),
                    data.getStringExtra(NewRecipeActivity.EXTRA_RECIPE_INSTRUCTIONS),
                    data.getStringExtra(NewRecipeActivity.IMAGE_URI));
            Log.d(TAG, "rWantToMake: " + " data.getBooleanExtra(NewRecipeActivity.EXTRA_RECIPE_WANTTOMAKE" + data.getBooleanExtra(NewRecipeActivity.EXTRA_RECIPE_WANTTOMAKE, true));
            Log.d(TAG, "Recipe: " + recipe);
            recipeViewModel.insert(recipe);
        } else if (requestCode == UPDATE_RECIPE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Recipe recipe = new Recipe(
                    data.getStringExtra(EditRecipeActivity.RECIPE_ID),
                    data.getStringExtra(EditRecipeActivity.UPDATED_RECIPE_NAME),
                    data.getStringExtra(EditRecipeActivity.UPDATED_RECIPE_TAG),
                    data.getBooleanExtra(EditRecipeActivity.UPDATED_RECIPE_WANTTOMAKE, true),
                    data.getStringExtra(EditRecipeActivity.UPDATED_RECIPE_PREPTIME),
                    data.getStringExtra(EditRecipeActivity.UPDATED_RECIPE_COOKTIME),
                    data.getStringExtra(EditRecipeActivity.UPDATED_RECIPE_INGREDIENTS),
                    data.getStringExtra(EditRecipeActivity.UPDATED_RECIPE_INSTRUCTIONS),
                    data.getStringExtra(EditRecipeActivity.UPDATED_RECIPE_IMAGE));
            recipeViewModel.update(recipe);

            Toast.makeText(
                    getApplicationContext(), R.string.updated, Toast.LENGTH_LONG).show();

        } else if (requestCode == VIEW_RECIPE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(
                    getApplicationContext(), "View Activity", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnDeleteClickListener(Recipe recipe) {
        recipeViewModel.delete(recipe);
    }
}
