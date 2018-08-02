package com.nanodegree.android.bakingapp.BakingData;

import java.io.Serializable;
import java.util.List;

public class BakingData implements Serializable{

    private int recipe_id;
    private String recipe_name;
    private List<RecipeIngredientInfo> recipeIngredientInfo = null;
    private List<RecipeSteps> recipeSteps = null;
    private Double ingredient_quantity;
    private String ingredient_measure;
    private String ingredient_description;
    private int recipe_image;


    //No Args constructor, use in serialization
    public BakingData() {
    }

    public BakingData(int recipe_id, String recipe_name){
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
    }

    public BakingData(
                      Double ingredient_quantity,
                      String ingredient_measure,
                      String ingredient_description) {

        this.ingredient_description = ingredient_description;
        this.ingredient_quantity = ingredient_quantity;
        this.ingredient_measure = ingredient_measure;

    }


    public Double getIngredient_quantity() {
        return ingredient_quantity;
    }

    public void setIngredient_quantity(Double ingredient_quantity) {
        this.ingredient_quantity = ingredient_quantity;
    }

    public String getIngredient_measure() {
        return ingredient_measure;
    }

    public void setIngredient_measure(String ingredient_measure) {
        this.ingredient_measure = ingredient_measure;
    }

    public String getIngredient_description() {
        return ingredient_description;
    }

    public void setIngredient_description(String ingredient_description) {
        this.ingredient_description = ingredient_description;
    }

    public int getRecipe_image() {
        return recipe_image;
    }

    public void setRecipe_image(int recipe_image) {
        this.recipe_image = recipe_image;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public List<RecipeIngredientInfo> getRecipeIngredientInfo() {
        return recipeIngredientInfo;
    }

    public void setRecipeIngredientInfo(List<RecipeIngredientInfo> recipeIngredientInfo) {
        this.recipeIngredientInfo = recipeIngredientInfo;
    }

    public List<RecipeSteps> getRecipeSteps() {
        return recipeSteps;
    }

    public void setRecipeSteps(List<RecipeSteps> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }
}
