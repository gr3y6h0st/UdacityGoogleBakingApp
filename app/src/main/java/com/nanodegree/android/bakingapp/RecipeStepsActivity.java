package com.nanodegree.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.nanodegree.android.bakingapp.BakingData.BakingData;

public class RecipeStepsActivity extends AppCompatActivity {


    private Context mContext = RecipeStepsActivity.this;

    BakingData selectedRecipeData;
    int mRecipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        //create FragmentManager and new reference to Fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        Intent mainActivityIntent = getIntent();
        //TODO: Pass recipeID to Fragment.
        selectedRecipeData = (BakingData) mainActivityIntent.getSerializableExtra("RecipeData");
        mRecipeID = selectedRecipeData.getRecipe_id();

        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", mRecipeID);

        //set bundle as argument on fragment obj
        recipeStepsFragment.setArguments(bundle);

        //Commit Transaction
        fragmentManager.beginTransaction()
                .add(R.id.fragment_recipe_steps, recipeStepsFragment)
                .commit();





    }
}
