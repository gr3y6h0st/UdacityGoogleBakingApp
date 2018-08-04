package com.nanodegree.android.bakingapp.Utils;

import android.util.Log;

import com.nanodegree.android.bakingapp.BakingData.BakingData;
import com.nanodegree.android.bakingapp.BakingData.RecipeIngredientInfo;
import com.nanodegree.android.bakingapp.BakingData.RecipeSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class BakingAppDatabaseJsonUtils {

    final static String TAG = BakingAppDatabaseJsonUtils.class.getSimpleName();

    final static String RECIPE_ID = "id";
    final static String RECIPE_NAMES = "name";
    final static String RECIPE_INGREDIENTS = "ingredients";
    final static String RECIPE_STEPS = "steps";
    final static String RECIPE_SERVING_SIZE = "servings";
    final static String RECIPE_IMAGE = "image";

    final static String SPECIFIC_INGREDIENTS = "ingredient";
    final static String SPECIFIC_MEASURE = "measure";
    final static String SPECIFIC_QUANTITY = "quantity";

    final static String RECIPE_STEPS_ID = "id";
    final static String RECIPE_STEPS_SHORT_DESC = "shortDescription";
    final static String RECIPE_STEPS_DESC = "description";
    final static String RECIPE_STEPS_VIDEO_URL = "videoURL";
    final static String RECIPE_STEPS_THUMBNAIL = "thumbnailURL";

    public static List<BakingData> getRecipeNames(String json) throws JSONException {
        JSONArray bakingData = new JSONArray(json);
        List<BakingData> recipeName = new ArrayList<BakingData>();

        for(int i = 0; i < bakingData.length(); i++){
            JSONObject recipeNames = bakingData.getJSONObject(i);
            BakingData specificRecipeInfo = new BakingData(
                    recipeNames.getInt(RECIPE_ID),
                    recipeNames.getString(RECIPE_NAMES)
            );
            Log.v(TAG, recipeNames.getString(RECIPE_NAMES));
            recipeName.add(specificRecipeInfo);
            Log.v(TAG, recipeName.toString());

        }
        return recipeName;
    }

    //TODO: Find a way to sort out the ingredients by recipe. (Currently displays all results).

    public static List<RecipeIngredientInfo> getRecipeIngredients(String json, int recipeId) throws JSONException {
        JSONArray recipeList = new JSONArray(json);
        List<RecipeIngredientInfo> recipeIngredients = new ArrayList<RecipeIngredientInfo>();
        JSONObject recipeObj = recipeList.getJSONObject(recipeId - 1);
        JSONArray ingredientsArr = recipeObj.getJSONArray(RECIPE_INGREDIENTS);

        for(int x = 0; x < ingredientsArr.length(); x++){
            JSONObject ingredientsInfo = ingredientsArr.getJSONObject(x);
            RecipeIngredientInfo specificRecipeIngredients = new RecipeIngredientInfo(
                    ingredientsInfo.getDouble(SPECIFIC_QUANTITY),
                    ingredientsInfo.getString(SPECIFIC_MEASURE),
                    ingredientsInfo.getString(SPECIFIC_INGREDIENTS)
            );
            recipeIngredients.add(specificRecipeIngredients);
        }
        Log.v(TAG, recipeIngredients.toString());
        return recipeIngredients;
    }

    public static List<RecipeSteps> getRecipeSteps(String json, int recipeId) throws JSONException {
        JSONArray recipeList = new JSONArray(json);
        List<RecipeSteps> recipeSteps = new ArrayList<RecipeSteps>();
        JSONObject recipeObj = recipeList.getJSONObject(recipeId - 1);
        JSONArray stepsArr = recipeObj.getJSONArray(RECIPE_STEPS);

        for (int x = 0; x < stepsArr.length(); x++) {
            JSONObject stepsInfo = stepsArr.getJSONObject(x);
            RecipeSteps specificRecipeSteps = new RecipeSteps(
                    stepsInfo.getInt(RECIPE_ID),
                    stepsInfo.getString(RECIPE_STEPS_SHORT_DESC),
                    stepsInfo.getString(RECIPE_STEPS_DESC),
                    stepsInfo.getString(RECIPE_STEPS_VIDEO_URL),
                    stepsInfo.getString(RECIPE_STEPS_THUMBNAIL)
            );
            recipeSteps.add(specificRecipeSteps);
        }
        Log.v(TAG, recipeSteps.toString());
        return recipeSteps;
    }
}
