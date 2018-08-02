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
import com.nanodegree.android.bakingapp.BakingData.RecipeIngredientInfo;
import com.nanodegree.android.bakingapp.Utils.BakingAppDatabaseJsonUtils;
import com.nanodegree.android.bakingapp.Utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Recipe_Information extends AppCompatActivity implements
        RecipesInfoAdapter.RecipesInfoAdapterOnClickListener, LoaderManager.LoaderCallbacks<List<RecipeIngredientInfo>>{

    @BindView(R.id.recipe_info_rv)
    RecyclerView recipeInfoRv;
    private RecipesInfoAdapter mRecipeInfoAdapter;
    private Context mContext = Recipe_Information.this;

    BakingData selectedRecipeData;
    int mRecipeID;
    public static List<RecipeIngredientInfo> RecipeIngredientInfoList = new ArrayList<>();
    public static final int ID_RECIPE_INFO_LOADER = 119;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe__information);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);

        recipeInfoRv.setLayoutManager(layoutManager);
        recipeInfoRv.setHasFixedSize(true);
        Intent intent = getIntent();
        if (intent == null) throw new NullPointerException("YOUR INTENT cannot be null");

        selectedRecipeData = (BakingData) intent.getSerializableExtra("RecipeData");
        //TODO:UNLOAD RECIPE ID/POSITION and USE FOR ASYNCTASK call to Util Class
        mRecipeID = selectedRecipeData.getRecipe_id();

        //TODO: Declare and set a RecyclerViewAdapter
        mRecipeInfoAdapter = new RecipesInfoAdapter(mContext, RecipeIngredientInfoList, this);
        recipeInfoRv.setAdapter(mRecipeInfoAdapter);

        LoaderManager.enableDebugLogging(true);

        getSupportLoaderManager().initLoader(ID_RECIPE_INFO_LOADER, null, this);

    }

    @Override
    public void onItemClick(int clickedPosition) {

    }

    @NonNull
    @Override
    public Loader<List<RecipeIngredientInfo>> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id) {

            case ID_RECIPE_INFO_LOADER:
                System.out.println(id);

                return new AsyncTaskLoader<List<RecipeIngredientInfo>>(this) {
                    List<RecipeIngredientInfo> mRecipeIngredientInfo;

                    @Override
                    protected void onStartLoading() {
                        if (mRecipeIngredientInfo != null) {
                            deliverResult(mRecipeIngredientInfo);
                            System.out.println("BAKING DATA! delivering data!");
                        } else {
                            forceLoad();
                            System.out.println("BAKING DATA forceloading.");
                        }
                    }

                    @Override
                    public void deliverResult(@Nullable List<RecipeIngredientInfo> data) {
                        mRecipeIngredientInfo = data;
                        super.deliverResult(data);
                    }

                    @Nullable
                    @Override
                    public List<RecipeIngredientInfo> loadInBackground() {

                        try {
                            URL recipeInfoRequestUrl = NetworkUtils.buildUrl();

                            String jsonRecipeIngredientInfobaseResponse = NetworkUtils.getResponseFromHttpUrl(recipeInfoRequestUrl);

                            List<RecipeIngredientInfo> simpleRecipeInfoJson = BakingAppDatabaseJsonUtils.getRecipeIngredients(jsonRecipeIngredientInfobaseResponse, mRecipeID);

                            return simpleRecipeInfoJson;

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
    public void onLoadFinished(@NonNull Loader<List<RecipeIngredientInfo>> loader, List<RecipeIngredientInfo> data) {

        if(data != null){
            RecipeIngredientInfoList = data;

            recipeInfoRv.setAdapter(mRecipeInfoAdapter);
            recipeInfoRv.setHasFixedSize(true);
            mRecipeInfoAdapter.notifyRecipeIngredientInfoChange(RecipeIngredientInfoList);
        } else{
            Log.v("ASYNC TASK README: ", "Baking Data is null or empty.");
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<RecipeIngredientInfo>> loader) {

    }
}
