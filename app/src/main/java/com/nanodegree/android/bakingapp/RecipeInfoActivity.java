package com.nanodegree.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.nanodegree.android.bakingapp.BakingData.BakingData;

public class RecipeInfoActivity extends AppCompatActivity {
    int mRecipeID;
    BakingData selectedRecipeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__information);

        //create FragmentManager and new reference to Fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeInfoFragment recipeInfoListFragment = new RecipeInfoFragment();

        Intent mainActivityIntent = getIntent();
        //TODO: Pass recipeID to Fragment.
        selectedRecipeData = (BakingData) mainActivityIntent.getSerializableExtra("RecipeData");

        mRecipeID = selectedRecipeData.getRecipe_id();
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", mRecipeID);

        //set bundle as argument on fragment obj
        recipeInfoListFragment.setArguments(bundle);

        //Commit Transaction
        fragmentManager.beginTransaction()
                .add(R.id.fragment_recipe_info, recipeInfoListFragment)
                .commit();



        //TODO: could use custom methods to set data within Fragment.

        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        recipeStepsFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_recipe_steps2, recipeStepsFragment)
                .commit();

        //TODO: insert all Fragments in this activity
    }
}
