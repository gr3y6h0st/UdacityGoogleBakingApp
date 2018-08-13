package com.nanodegree.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nanodegree.android.bakingapp.BakingData.BakingData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeInfoActivity extends AppCompatActivity {
    String mRecipeName;
    int mRecipeID;
    List<BakingData> selectedRecipeData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__information);

        if(savedInstanceState == null) {

            //create FragmentManager and new reference to Fragment.
            FragmentManager fragmentManager = getSupportFragmentManager();

            //Current Recipe Info Fragment.
            RecipeInfoFragment recipeInfoListFragment = new RecipeInfoFragment();

            //get intent info
            Intent mainActivityIntent = getIntent();
            //gets BakingData from MA.
            selectedRecipeData = (List<BakingData>)mainActivityIntent.getSerializableExtra("RecipeData");
            //get recipeID.
            mRecipeID = mainActivityIntent.getIntExtra("recipeID", mRecipeID);
            Log.v("TESTING RECIPE ID #: ", String.valueOf(mRecipeID));
            //get and set title to current Activity
            mRecipeName = mainActivityIntent.getStringExtra("recipeName");
            setTitle(mRecipeName);

            //create new bundle to send over to Fragment
            Bundle bundle = new Bundle();
            //load recipeId
            bundle.putInt("recipe_id", mRecipeID);
            //load BakingData as Serializable
            bundle.putSerializable("RecipeData", (Serializable) selectedRecipeData);
            //set bundle as args to Frag.
            recipeInfoListFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_recipe_info, recipeInfoListFragment)
                    .commit();

            //Steps Fragment
            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            //set same bundle to stepsFrag.
            recipeStepsFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_recipe_steps2, recipeStepsFragment)
                    .commit();
        }
    }
}
