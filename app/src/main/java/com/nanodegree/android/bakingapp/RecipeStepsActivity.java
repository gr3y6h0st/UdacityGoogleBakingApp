package com.nanodegree.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nanodegree.android.bakingapp.BakingData.BakingData;
import com.nanodegree.android.bakingapp.BakingData.RecipeSteps;
import com.nanodegree.android.bakingapp.Utils.BakingAppDatabaseJsonUtils;
import com.nanodegree.android.bakingapp.Utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<RecipeSteps>>,RecipeStepsAdapter.RecipeStepsAdapterOnClickListener {

    private final String TAG = RecipeStepsActivity.class.getSimpleName();

    @BindView(R.id.recipe_info_rv)
    RecyclerView recipeStepsRv;
    private RecipeStepsAdapter mRecipeStepsAdapter;
    private Context mContext = RecipeStepsActivity.this;

    BakingData selectedRecipeData;
    int mRecipeID;
    public static List<RecipeSteps> RecipeStepsList = new ArrayList<>();
    public static final int ID_RECIPE_STEPS_LOADER = 219;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__information);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);

        recipeStepsRv.setLayoutManager(layoutManager);
        recipeStepsRv.setHasFixedSize(true);


        Intent intent = getIntent();
        if (intent == null) throw new NullPointerException("YOUR INTENT cannot be null");
        selectedRecipeData = (BakingData) intent.getSerializableExtra("RecipeData");
        //TODO:UNLOAD RECIPE ID/POSITION and USE FOR ASYNCTASK call to Util Class
        mRecipeID = selectedRecipeData.getRecipe_id();

        //TODO: Declare and set a RecyclerViewAdapter
        mRecipeStepsAdapter = new RecipeStepsAdapter(mContext, RecipeStepsList, this);
        recipeStepsRv.setAdapter(mRecipeStepsAdapter);

        LoaderManager.enableDebugLogging(true);

        getSupportLoaderManager().initLoader(ID_RECIPE_STEPS_LOADER, null, this);

    }

    /*@Override
    public void onItemClick(int clickedPosition) {

    }*/

    @NonNull
    @Override
    public Loader<List<RecipeSteps>> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id) {

            case ID_RECIPE_STEPS_LOADER:
                System.out.println(id);

                return new AsyncTaskLoader<List<RecipeSteps>>(this) {
                    List<RecipeSteps> mRecipeSteps;

                    @Override
                    protected void onStartLoading() {
                        if (mRecipeSteps != null) {
                            deliverResult(mRecipeSteps);
                            System.out.println("BAKING DATA! delivering RECIPE INFO!");
                        } else {
                            forceLoad();
                            System.out.println("BAKING DATA forceloading.");
                        }
                    }

                    @Override
                    public void deliverResult(@Nullable List<RecipeSteps> data) {
                        mRecipeSteps = data;
                        super.deliverResult(data);
                    }

                    @Nullable
                    @Override
                    public List<RecipeSteps> loadInBackground() {

                        try {
                            URL recipeInfoRequestUrl = NetworkUtils.buildUrl();

                            String jsonRecipeStepsbaseResponse = NetworkUtils.getResponseFromHttpUrl(recipeInfoRequestUrl);

                            List<RecipeSteps> simpleRecipeStepsJson = BakingAppDatabaseJsonUtils.getRecipeSteps(jsonRecipeStepsbaseResponse, mRecipeID);

                            return simpleRecipeStepsJson;

                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<RecipeSteps>> loader, List<RecipeSteps> data) {

        if(data != null){
            RecipeStepsList = data;
            recipeStepsRv.setAdapter(mRecipeStepsAdapter);
            recipeStepsRv.setHasFixedSize(true);
            mRecipeStepsAdapter.notifyRecipeStepsChange(RecipeStepsList);
        } else{
            Log.v("ASYNC TASK README: ", "Baking Data is null or empty.");
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<RecipeSteps>> loader) {

    }

    @Override
    public void onItemClick(int clickedPosition) {

    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create FragmentManager and new reference to Fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        //MainRecipeListFragment recipeListFragment = new MainRecipeListFragment();

        //Commit Transaction
        fragmentManager.beginTransaction()
                .add(R.id.fragment_main_recipe_list, recipeListFragment)
                .commit();
    }*/



}
