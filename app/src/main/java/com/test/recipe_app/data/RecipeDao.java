package com.test.recipe_app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.test.recipe_app.model.Recipe;

import java.util.List;


@Dao
public interface RecipeDao {

    //CRUD
    @Insert
    void insert(Recipe recipe);

    @Query("DELETE FROM recipe_table")
    void deleteAll();

    @Query("DELETE FROM recipe_table WHERE id = :id")
    int deleteARecipe(String id);

    @Query("UPDATE recipe_table SET recipe_col = :recipeText WHERE id = :id")
    int updateRecipeItem(String id, String recipeText);

    @Query("SELECT * FROM recipe_table ORDER BY recipe_col DESC")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipe_table WHERE id=:id")
    LiveData<Recipe> getRecipe(String id);

    @Update
    void update(Recipe recipe);

    @Delete
    int delete(Recipe recipe);
}

