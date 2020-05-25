package com.test.recipe_app.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_table")
public class Recipe {

    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    @ColumnInfo(name = "recipe_col")
    public String recipe;
    public String tag;
    public boolean wantToMake;
    public String prepTime;
    public String cookTime;
    public int servings;
    public String ingredients;
    public String instructions;

    public Recipe(@NonNull String id, @NonNull String recipe, @NonNull String tag, boolean wantToMake,
                  @NonNull String prepTime, @NonNull String cookTime, @NonNull String ingredients, @NonNull String instructions) {
        this.id = id;
        this.recipe = recipe;
        this.tag = tag;
        this.wantToMake = false;
        this.cookTime = cookTime;
        this.prepTime = prepTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Recipe() {

    }

    public String getRecipe() {
        return recipe;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return recipe;
    }

    public void setName(String recipe) {
        this.recipe = recipe;
    }

    public boolean getWantToMake(){ return wantToMake; }

    public void setWantToMake(){ this.wantToMake = !this.wantToMake; }

    public String getCookTime(){ return cookTime; }

    public void setCookTime(String cookTime){ this.cookTime = cookTime; }

    public String getPrepTime(){ return prepTime; }

    public void setPrepTime(String prepTime){ this.prepTime = prepTime; }

    public String getTag() { return tag; }

    public void setTag(String tag) { this.tag = tag; }

    public String getIngredients() { return ingredients; }

    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }

    public void setInstructions(String instructions) { this.instructions = instructions; }

    @Override
    public String toString() {
        return "Name: " + recipe + " Tag: " + tag;
    }
}
