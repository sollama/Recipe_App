package com.test.recipe_app.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.test.recipe_app.model.Recipe;

@Database(entities = {Recipe.class}, version = 7)
public abstract class RecipeRoomDatabase extends RoomDatabase {

    public static volatile RecipeRoomDatabase INSTANCE;
    public abstract RecipeDao recipeDao();

    public static RecipeRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (RecipeRoomDatabase.class) {
                if (INSTANCE == null) {
                    //create db
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeRoomDatabase.class, "recipe_database")
                            .addCallback(roomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync (INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final RecipeDao recipeDao;


        public PopulateDbAsync(RecipeRoomDatabase db) {
            recipeDao = db.recipeDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //recipeDao.deleteAll();

            //for testing
//            Recipe apple_pie = new Recipe("apple pie");
//            recipeDao.insert(apple_pie);
//
//            Recipe cookie = new Recipe("cookie");
//            recipeDao.insert(cookie);
              return null;
        }
    }
}
