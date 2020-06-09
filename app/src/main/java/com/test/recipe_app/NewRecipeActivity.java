package com.test.recipe_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewRecipeActivity extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    private static final String TAG = "NewRecipeActivity";
    public static final String EXTRA_RECIPE_NAME = "EXTRA_RECIPE_NAME",
            EXTRA_RECIPE_TAG = "EXTRA_RECIPE_TAG",
            EXTRA_RECIPE_INSTRUCTIONS = "EXTRA_RECIPE_INSTRUCTIONS",
            EXTRA_RECIPE_INGREDIENTS = "EXTRA_RECIPE_INGREDIENTS",
            EXTRA_RECIPE_PREPTIME = "EXTRA_RECIPE_PREPTIME",
            EXTRA_RECIPE_COOKTIME = "EXTRA_RECIPE_COOKTIME",
            EXTRA_RECIPE_WANTTOMAKE = "EXTRA_RECIPE_WANTTOMAKE",
            EXTRA_RECIPE_IMAGE_URI = "IMAGE_URI";

    private EditText rName,
            rTag,
            rInstructions,
            rIngredients,
            rPrepTime,
            rCookTime;
    private Button sButton, cButton;
    private ImageButton wantToMakeButton, uploadImage;
    private boolean wantToMake = false;
    ImageView recipeImageView;
    private Uri uri = null;

    @Override
    public void onUserInteraction() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        getSupportActionBar().setTitle("New Recipe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rName = findViewById(R.id.new_recipe);
        rTag = findViewById(R.id.new_tag);
        rInstructions = findViewById(R.id.new_instructions);
        rIngredients = findViewById(R.id.new_ingredients);
        rPrepTime = findViewById(R.id.new_prepTime);
        rCookTime = findViewById(R.id.new_cookTime);
        sButton = findViewById(R.id.button_save);
        wantToMakeButton = findViewById(R.id.new_want_to_make);
        cButton = findViewById(R.id.button_cancel);
        uploadImage = findViewById(R.id.upload_image);
        recipeImageView = findViewById(R.id.new_recipe_image);

        rName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                updateButton();
            }
        });

        rName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                // Get key action, up or down.
                int action = keyEvent.getAction();

                // Only process key up action, otherwise this listener will be triggered twice because of key down action.
                if (action == KeyEvent.ACTION_UP) {
                    updateButton();
                }
                return false;
            }
        });


        wantToMakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "wantToMake: " + wantToMake);
                wantToMake = !wantToMake;
                if (wantToMake) {
                    wantToMakeButton.setImageResource(R.drawable.star_24px);
                } else {
                    wantToMakeButton.setImageResource(R.drawable.star_outline_24px);
                }
            }
        });

        //choose image from Library
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        NewRecipeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        //cancel creating new recipe
        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //save new recipe
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent replyIntent = new Intent();
                    String recipeNameSt = rName.getText().toString();
                    String recipeTagSt = rTag.getText().toString();
                    String recipeIngredientsSt = rIngredients.getText().toString();
                    String recipeInstructionsSt = rInstructions.getText().toString();
                    String recipePrepTimeSt = String.valueOf(rPrepTime.getText());
                    String recipeCookTimeSt = String.valueOf(rCookTime.getText());
                    boolean recipeWantToMake = Boolean.valueOf(wantToMake);
                    Log.d(TAG, "rIngredients" + rIngredients);

                    replyIntent.putExtra(EXTRA_RECIPE_NAME, recipeNameSt);
                    replyIntent.putExtra(EXTRA_RECIPE_TAG, recipeTagSt);
                    replyIntent.putExtra(EXTRA_RECIPE_INGREDIENTS, recipeIngredientsSt);
                    replyIntent.putExtra(EXTRA_RECIPE_INSTRUCTIONS, recipeInstructionsSt);
                    replyIntent.putExtra(EXTRA_RECIPE_PREPTIME, recipePrepTimeSt);
                    replyIntent.putExtra(EXTRA_RECIPE_COOKTIME, recipeCookTimeSt);
                    replyIntent.putExtra(EXTRA_RECIPE_WANTTOMAKE, recipeWantToMake);
                    replyIntent.putExtra(EXTRA_RECIPE_IMAGE_URI, uri.toString());

                    setResult(RESULT_OK, replyIntent);
                    Log.d(TAG, "replyIntent" + replyIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    //disables save button until recipe title is entered
    public void updateButton() {
        String inputText = rName.getText().toString();
        if (inputText.length() > 0) {
            sButton.setEnabled(true);
        } else {
            sButton.setEnabled(false);
            sButton.setBackgroundColor(Color.parseColor("#A9312E2E"));

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(),
                        "You don't have permission to access file location!",
                        Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //set image
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                recipeImageView.setImageBitmap(bitmap);
                recipeImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
