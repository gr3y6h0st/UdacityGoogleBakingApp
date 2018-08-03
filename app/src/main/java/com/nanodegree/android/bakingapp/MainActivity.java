package com.nanodegree.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create FragmentManager and new reference to Fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        MainRecipeListFragment recipeListFragment = new MainRecipeListFragment();

        //Commit Transaction
        fragmentManager.beginTransaction()
                .add(R.id.fragment_main_recipe_list, recipeListFragment)
                .commit();
    }
}


