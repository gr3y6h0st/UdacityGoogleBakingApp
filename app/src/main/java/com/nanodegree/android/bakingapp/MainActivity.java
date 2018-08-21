package com.nanodegree.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nanodegree.android.bakingapp.BakingData.BakingData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainRecipeListFragment.OnRecipeClickListener {
    private boolean mTwoPane;

    private int mRecipeID;
    private List<BakingData> selectedRecipeData = new ArrayList<>();
    private Bundle bundle = new Bundle();

    private final String TAG = MainActivity.class.getSimpleName();
    private String mRecipeName;

    @BindView(R.id.horizontal_main_divider)
    View horizontalDiv;

    @BindView(R.id.vertical_main_divider)
    View verticalDiv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.recipe_info_linear_layout) != null) {
            ButterKnife.bind(this);

            //LinearLayout exists in Tablet activity xml
            mTwoPane = true;

            if (savedInstanceState == null) {

                //create FragmentManager and new reference to Fragment.
                FragmentManager fragmentManager = getSupportFragmentManager();
                MainRecipeListFragment recipeListFragment = new MainRecipeListFragment();

                //Commit Transaction
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_main_recipe_list, recipeListFragment)
                        .commit();

                horizontalDiv.setVisibility(View.GONE);

            }
        } else {
            /**
             * false = single plane mode, will follow displaying fragments as separate activities.
             * See onRecipeSelected Method for behavior.
             */
            mTwoPane = false;
            if (savedInstanceState == null) {
                //create FragmentManager and new reference to Fragment.
                FragmentManager fragmentManager = getSupportFragmentManager();
                MainRecipeListFragment recipeListFragment = new MainRecipeListFragment();

                //Commit Transaction
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_main_recipe_list, recipeListFragment)
                        .commit();
            }

        }
    }

    @Override
    public void onRecipeSelected(int position, List<BakingData> bakingDataList) {
        /*Toast.makeText(this, "Position # = " + position, Toast.LENGTH_SHORT).show();*/
        mRecipeID = position;
        mRecipeName = bakingDataList.get(position).getRecipe_name();

        if(mTwoPane) {

            //Current Recipe Info Fragment.
            RecipeInfoFragment newRecipeInfoListFragment = new RecipeInfoFragment();

            //set recipe Id for recipeInfo fragment use
            newRecipeInfoListFragment.setRecipeId(mRecipeID);
            newRecipeInfoListFragment.setRecipeName(mRecipeName);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_recipe_info, newRecipeInfoListFragment)
                    .commit();

            //Steps Fragment
            RecipeStepsFragment newRecipeStepsFragment = new RecipeStepsFragment();

            //set recipe id for recipeSteps Fragment use
            newRecipeStepsFragment.setRecipeId(mRecipeID);

            //set recipeName
            newRecipeStepsFragment.setRecipeName(mRecipeName);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_recipe_steps2, newRecipeStepsFragment)
                    .commit();

            verticalDiv.setVisibility(View.VISIBLE);
            horizontalDiv.setVisibility(View.VISIBLE);

        } else{
            /*
             * For behavior on single-pane devices (loads into new activity)
             */
            bundle.putInt("recipeID", mRecipeID);
            bundle.putString("recipeName", mRecipeName);
            System.out.println("Recipe ID #: " + mRecipeID);

            //prep intent destination
            final Intent recipeInfoActivityIntent = new Intent(this, RecipeInfoActivity.class);
            //load intent extras in bundle
            recipeInfoActivityIntent.putExtras(bundle);

            //start destination intent
            startActivity(recipeInfoActivityIntent);

        }
    }
}


