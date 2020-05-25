package com.test.recipe_app.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.test.recipe_app.data.RecipeDao;
import com.test.recipe_app.data.RecipeRoomDatabase;

import java.util.List;

public class EditRecipeViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private RecipeDao recipeDao;
    private RecipeRoomDatabase db;

    public EditRecipeViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "Edit ViewModal");
        db = RecipeRoomDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
    }

    public LiveData<Recipe> getRecipe(String id) {
        return recipeDao.getRecipe(id);
    }

}
