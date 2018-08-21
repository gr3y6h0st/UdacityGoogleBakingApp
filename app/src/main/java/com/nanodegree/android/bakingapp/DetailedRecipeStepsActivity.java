package com.nanodegree.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.nanodegree.android.bakingapp.BakingData.RecipeSteps;

import java.io.Serializable;
import java.util.List;

public class DetailedRecipeStepsActivity extends AppCompatActivity {
    private final String TAG = DetailedRecipeStepsActivity.class.getSimpleName();
    List<RecipeSteps> recipeStepsData;
    int mPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_steps);

        if (savedInstanceState == null) {

            //create FragmentManager and new reference to Fragment.
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailedRecipeStepsFragment detailedRecipeStepsFragment = new DetailedRecipeStepsFragment();

            Intent RecipeStepsFragmentIntent = getIntent();
            //TODO: Pass recipeID to Fragment.
            recipeStepsData = (List<RecipeSteps>) RecipeStepsFragmentIntent.getSerializableExtra("RecipeStepsData");

            mPosition = RecipeStepsFragmentIntent.getIntExtra("clickedPosition", mPosition);
            detailedRecipeStepsFragment.setStepPosition(mPosition);

            Bundle bundle = new Bundle();
            bundle.putInt("recipe_id", mPosition);
            bundle.putSerializable("recipeSteps", (Serializable) recipeStepsData);

            //set bundle as argument on fragment obj
            detailedRecipeStepsFragment.setArguments(bundle);

            //Commit Transaction
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_detailed_recipe_steps, detailedRecipeStepsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home) {
            Toast.makeText(this, "Back/Home", Toast.LENGTH_SHORT).show();
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
