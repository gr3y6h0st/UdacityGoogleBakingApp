package com.nanodegree.android.bakingapp.BakingData;

import android.net.Uri;
import android.provider.BaseColumns;

public class BakingDataContract implements BaseColumns {

    public static final String TABLE_NAME = "RecipeIngredients";

    public static final String CONTENT_AUTHORITY = "com.nanodegree.android.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPES = "recipes";

    public static final String COLUMN_INGREDIENT_QUANTITY= "quantity";
    public static final String COLUMN_INGREDIENT_DESCRIPTION = "description";
    public static final String COLUMN_INGREDIENT_MEASURE = "measure";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_RECIPES)
            .build();
    public static final String COLUMN_RECIPE_NAME = "recipeName";
    public static final String COLUMN_RECIPE_ID = "recipeId";
}
