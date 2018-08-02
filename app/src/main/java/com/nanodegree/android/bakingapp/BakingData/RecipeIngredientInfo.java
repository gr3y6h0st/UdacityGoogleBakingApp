package com.nanodegree.android.bakingapp.BakingData;


import java.io.Serializable;

public class RecipeIngredientInfo implements Serializable{


    private Double ingredient_quantity;
    private String ingredient_measure;
    private String ingredient_description;


    //No Args constructor, use in serialization
    public RecipeIngredientInfo() {
    }

    public RecipeIngredientInfo(
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
}
