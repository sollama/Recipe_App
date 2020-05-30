package com.test.recipe_app.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.test.recipe_app.EditRecipeActivity;
import com.test.recipe_app.MainActivity;
import com.test.recipe_app.R;
import com.test.recipe_app.ViewRecipeActivity;
import com.test.recipe_app.model.EditRecipeViewModel;
import com.test.recipe_app.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(Recipe recipe);
    }

    private static final String TAG = "RecipeAdapter";
    private final LayoutInflater recipeInflater;
    private Context mContext;//context of MainActivity
    public List<Recipe> recipeList; //cached copy of recipe items
    private OnDeleteClickListener onDeleteClickListener;
    EditRecipeViewModel editRecipeViewModel;

    public void setRecipe(List<Recipe> recipes) {
        recipeList = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (recipeList != null) return recipeList.size();
        else return 0;
    }

    public RecipeAdapter(Context context, OnDeleteClickListener listener) {
        recipeInflater = LayoutInflater.from(context);
        mContext = context;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = recipeInflater.inflate(R.layout.recyclerview_item, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {

        if(recipeList != null) {
            Recipe recipe = recipeList.get(position);
            recipeViewHolder.setData(recipe, position);

            //set wantToMake button
            if (recipe.getWantToMake()) {
                recipeViewHolder.recipeWantToMake.setImageResource(R.drawable.star_24px);
            } else {
                recipeViewHolder.recipeWantToMake.setImageResource(R.drawable.star_outline_24px);
            }
            Log.d(TAG, "rWantToMake: " + " recipe.getWantToMake" + recipe.getWantToMake());

            recipeViewHolder.recipeTextView.setText(recipe.getRecipe());
            recipeViewHolder.recipeTagTextView.setText("" + recipe.getTag());

            //convert & display image
            byte[] image = recipe.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            recipeViewHolder.recipeImage.setImageBitmap(bitmap);

            recipeViewHolder.setListener();
        } else {
            recipeViewHolder.recipeTextView.setText(R.string.no_recipes);
        }

    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTextView;
        public TextView recipeTagTextView;
        private ImageView recipeDelete;
        private ImageView recipeEdit;
        public int mPosition;
        public ImageButton recipeWantToMake;
        public CardView recipeView;
        public ImageView recipeImage;


        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTextView = itemView.findViewById(R.id.recipe_name);
            recipeTagTextView = itemView.findViewById(R.id.recipe_tag);
            recipeWantToMake = itemView.findViewById(R.id.want_to_make);
            recipeDelete = itemView.findViewById(R.id.delete_recipe_item);
            recipeEdit = itemView.findViewById(R.id.edit_recipe_item);
            recipeView = itemView.findViewById(R.id.recipe_card);
            recipeImage = itemView.findViewById(R.id.recipe_image);
        }

        public void setData(Recipe recipe, int position) {
            recipeTextView.setText(recipe.getName());
            //recipeTagTextView.setText();
            mPosition = position;
        }


        public void setListener() {
            //view listener here. start activity (not for result)

            recipeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ViewRecipeActivity.class);
                    intent.putExtra("recipe_id",recipeList.get(mPosition).getId());
                    ((Activity)mContext).startActivityForResult(intent, MainActivity.VIEW_RECIPE_ACTIVITY_REQUEST_CODE);

                }
            });

            recipeEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EditRecipeActivity.class);
                    intent.putExtra("recipe_id",recipeList.get(mPosition).getId());
                    Log.d(TAG , "mPosition: " + mPosition);
                    Log.d(TAG , "recipe_id: " + recipeList.get(mPosition).getId());

                    ((Activity)mContext).startActivityForResult(intent, MainActivity.UPDATE_RECIPE_ACTIVITY_REQUEST_CODE);
                }
            });

            recipeDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnDeleteClickListener(recipeList.get(mPosition));
                    }
                }
            });
        }
    }
}
