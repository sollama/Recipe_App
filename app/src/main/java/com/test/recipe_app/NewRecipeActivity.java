package com.test.recipe_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewRecipeActivity extends AppCompatActivity {
    private static final String TAG = "NewRecipeActivity";
    public static final String EXTRA_RECIPE_NAME = "EXTRA_RECIPE_NAME";
    public static final String EXTRA_RECIPE_TAG = "EXTRA_RECIPE_TAG";
    public static final String EXTRA_RECIPE_INSTRUCTIONS = "EXTRA_RECIPE_INSTRUCTIONS";
    public static final String EXTRA_RECIPE_INGREDIENTS = "EXTRA_RECIPE_INGREDIENTS";
    public static final String EXTRA_RECIPE_PREPTIME = "EXTRA_RECIPE_PREPTIME";
    public static final String EXTRA_RECIPE_COOKTIME = "EXTRA_RECIPE_COOKTIME";
    public static final String EXTRA_RECIPE_WANTTOMAKE = "EXTRA_RECIPE_WANTTOMAKE";

    private EditText rName;
    private EditText rTag;
    private EditText rInstructions;
    private EditText rIngredients;
    private EditText rPrepTime;
    private EditText rCookTime;
    private Button sButton;
    private ImageButton wantToMakeButton;
    private boolean wantToMake = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        rName = findViewById(R.id.new_recipe);
        rTag = findViewById(R.id.new_tag);
        rInstructions = findViewById(R.id.new_instructions);
        rIngredients = findViewById(R.id.new_ingredients);
        rPrepTime = findViewById(R.id.new_prepTime);
        rCookTime = findViewById(R.id.new_cookTime);
        sButton = findViewById(R.id.button_save);
        wantToMakeButton = findViewById(R.id.new_wantToMake);
        Log.d(TAG, "wantToMakeButton" + wantToMakeButton);


        rName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

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

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                String recipeNameSt = rName.getText().toString();
                String recipeTagSt = rTag.getText().toString();
                String recipeIngredientsSt = rIngredients.getText().toString();
                String recipeInstructionsSt = rInstructions.getText().toString();
                String recipePrepTimeSt = String.valueOf(rPrepTime.getText());
                String recipeCookTimeSt = String.valueOf(rCookTime.getText());
                Log.d(TAG, "prepTime: " + recipePrepTimeSt + ", recipeIngredientsSt: " + recipeIngredientsSt);
                replyIntent.putExtra(EXTRA_RECIPE_NAME, recipeNameSt);
                replyIntent.putExtra(EXTRA_RECIPE_TAG, recipeTagSt);
                replyIntent.putExtra(EXTRA_RECIPE_INGREDIENTS, recipeIngredientsSt);
                replyIntent.putExtra(EXTRA_RECIPE_INSTRUCTIONS, recipeInstructionsSt);
                replyIntent.putExtra(EXTRA_RECIPE_PREPTIME, recipePrepTimeSt);
                replyIntent.putExtra(EXTRA_RECIPE_COOKTIME, recipeCookTimeSt);
                replyIntent.putExtra(EXTRA_RECIPE_WANTTOMAKE, wantToMake);
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });
    }

    public void updateButton() {
        String inputText = rName.getText().toString();
        if (inputText.length() > 0) {
            sButton.setEnabled(true);
        } else {
            sButton.setEnabled(false);
            sButton.setBackgroundColor(Color.parseColor("#A9312E2E"));

        }
    }


//        sButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent replyIntent = new Intent();
//                if(TextUtils.isEmpty(recipeName.getText())) {
//                    setResult(RESULT_CANCELED, replyIntent);
//                } else {
//                    String recipeString = recipeName.getText().toString();
//                    replyIntent.putExtra(EXTRA_RECIPE_NAME, recipeString);
//                    setResult(RESULT_OK, replyIntent);
//                }
//                finish();
//            }
//        });

}
