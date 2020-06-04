package com.test.recipe_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.makeramen.roundedimageview.RoundedImageView;
import com.test.recipe_app.model.EditRecipeViewModel;
import com.test.recipe_app.model.Recipe;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditRecipeActivity extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    private static final String TAG = "EditRecipeActivity";
    public static final String RECIPE_ID = "recipe_id",
            UPDATED_RECIPE_NAME = "updated_recipe",
            UPDATED_RECIPE_TAG = "updated_tag",
            UPDATED_RECIPE_PREPTIME = "updated_prepTime",
            UPDATED_RECIPE_COOKTIME = "updated_cookTime",
            UPDATED_RECIPE_INGREDIENTS = "updated_ingredients",
            UPDATED_RECIPE_INSTRUCTIONS = "updated_instructions",
            UPDATED_RECIPE_WANTTOMAKE = "updated_wantToMake",
            UPDATED_RECIPE_IMAGE = "updated_image";

    private EditText editRecipeName,
            editTag,
            editPrepTime,
            editCookTime,
            editIngredients,
            editInstructions;
    private ImageButton editWantToMake,
            editImageButton;
    private boolean wantToMake;
    EditRecipeViewModel recipeModel;
    private Bundle bundle;
    private String id;
    private LiveData<Recipe> recipe;
    private ImageView editImage;
    float cornerRadius = 80.0f;
    private Uri uri;


    @Override
    public void onUserInteraction() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //xml underscore
        setContentView(R.layout.activity_edit_recipe);
        getSupportActionBar().setTitle("Edit Recipe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editRecipeName = findViewById(R.id.edit_recipe);
        editTag = findViewById(R.id.edit_tag);
        editPrepTime = findViewById(R.id.edit_prepTime);
        editCookTime = findViewById(R.id.edit_cookTime);
        editIngredients = findViewById(R.id.edit_ingredients);
        editInstructions = findViewById(R.id.edit_instructions);
        editWantToMake = findViewById(R.id.edit_want_to_make);
        editImage = findViewById(R.id.edit_recipe_image);
        editImageButton = findViewById(R.id.edit_image);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("recipe_id");
        }

        recipeModel = ViewModelProviders.of(this).get(EditRecipeViewModel.class);
        recipe = recipeModel.getRecipe(id);
        Log.d(TAG, "id" + id);


        recipe.observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null) {
                    String recipeName = recipe.getName();
                    String recipeIng = recipe.getIngredients();
                    String recipeInstruc = recipe.getInstructions();
                    Log.d(TAG, "Name: " + recipeName + ", Ingredients: " + recipeIng + ", Instructions: " + recipeInstruc);
                    editRecipeName.setText(recipe.getName());
                    editTag.setText(recipe.getTag());
                    editPrepTime.setText(recipe.getPrepTime());
                    editCookTime.setText(recipe.getCookTime());
                    editIngredients.setText(recipe.getIngredients());
                    editInstructions.setText(recipe.getInstructions());

                    if (recipe.getWantToMake()) {
                        wantToMake = true;
                        editWantToMake.setImageResource(R.drawable.star_24px);
                    } else {
                        wantToMake = false;
                        editWantToMake.setImageResource(R.drawable.star_outline_24px);
                    }

                    try {
                        //convert & display image
                        uri = Uri.parse(recipe.getImage());
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        editImage.setImageBitmap(bitmap);

//                        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
//                        dr.setCornerRadius(cornerRadius);
//                        editImage.setImageDrawable(dr);
                        editImage.setBackground(null);

                        editImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        editImage.setClipToOutline(true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e(TAG, "recipe null");
                }

            }
        });

        try {
            editImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(
                            EditRecipeActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_GALLERY
                    );
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        editWantToMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "wantToMake: " + wantToMake);
                wantToMake = !wantToMake;
                if (wantToMake) {
                    editWantToMake.setImageResource(R.drawable.star_24px);
                } else {
                    editWantToMake.setImageResource(R.drawable.star_outline_24px);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //set image
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                editImage.setImageBitmap(bitmap);
                editImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    //sends updated recipe to Main Activity & update database
    public void updateRecipe(View view) {
        String eRecipeId = id;
        String eRecipeName = editRecipeName.getText().toString();
        String eTag = editTag.getText().toString();
        String ePrepTime = String.valueOf(editPrepTime.getText());
        String eCookTime = String.valueOf(editCookTime.getText());
        String eIngredients = editIngredients.getText().toString();
        String eInstructions = editInstructions.getText().toString();
        boolean recipeWantToMake = Boolean.valueOf(wantToMake);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(RECIPE_ID, eRecipeId);
        resultIntent.putExtra(UPDATED_RECIPE_NAME, eRecipeName);
        resultIntent.putExtra(UPDATED_RECIPE_TAG, eTag);
        resultIntent.putExtra(UPDATED_RECIPE_PREPTIME, ePrepTime);
        resultIntent.putExtra(UPDATED_RECIPE_COOKTIME, eCookTime);
        resultIntent.putExtra(UPDATED_RECIPE_INGREDIENTS, eIngredients);
        resultIntent.putExtra(UPDATED_RECIPE_INSTRUCTIONS, eInstructions);
        resultIntent.putExtra(UPDATED_RECIPE_WANTTOMAKE, recipeWantToMake);
        resultIntent.putExtra(UPDATED_RECIPE_IMAGE, uri.toString());

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void cancelUpdate(View view) {
        finish();
    }
}
