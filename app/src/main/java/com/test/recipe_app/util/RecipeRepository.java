package com.test.recipe_app.util;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.test.recipe_app.data.RecipeDao;
import com.test.recipe_app.data.RecipeRoomDatabase;
import com.test.recipe_app.model.Recipe;

import java.util.List;

public class RecipeRepository {
    private RecipeDao recipeDao;
    private LiveData<List<Recipe>> allRecipes;

    public RecipeRepository(Application application) {
        RecipeRoomDatabase db = RecipeRoomDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
        allRecipes = recipeDao.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    public void insert(Recipe recipe) { new insertAsyncTask(recipeDao).execute(recipe); }

    public void update(Recipe recipe) { new UpdateAsyncTask(recipeDao).execute(recipe); }

    public void delete(Recipe recipe) { new DeleteAsyncTask(recipeDao).execute(recipe); }

    private class insertAsyncTask extends AsyncTask<Recipe, Void, Void> {
        private RecipeDao asyncTaskDao;
        public insertAsyncTask(RecipeDao dao) { asyncTaskDao = dao; }

        @Override
        protected Void doInBackground(Recipe... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private class OperationsAsyncTask extends AsyncTask<Recipe, Void, Void> {

        RecipeDao mAsyncTaskDao;

        OperationsAsyncTask(RecipeDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            return null;
        }
    }

    private class UpdateAsyncTask extends OperationsAsyncTask {

        UpdateAsyncTask(RecipeDao recipeDao) {
            super(recipeDao);
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            mAsyncTaskDao.update(recipes[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends OperationsAsyncTask {

        public DeleteAsyncTask(RecipeDao recipeDao) {
            super(recipeDao);
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            mAsyncTaskDao.delete(recipes[0]);
            return null;
        }
    }


}
