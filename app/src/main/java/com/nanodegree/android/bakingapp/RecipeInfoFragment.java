package com.nanodegree.android.bakingapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanodegree.android.bakingapp.BakingData.RecipeIngredientInfo;
import com.nanodegree.android.bakingapp.Utils.BakingAppDatabaseJsonUtils;
import com.nanodegree.android.bakingapp.Utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeInfoFragment extends Fragment implements
        RecipesInfoAdapter.RecipesInfoAdapterOnClickListener,
        LoaderManager.LoaderCallbacks<List<RecipeIngredientInfo>>{

    public RecipeInfoFragment(){

    }

    private final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recipe_info_rv)
    RecyclerView recipeInfoRv;
    private RecipesInfoAdapter mRecipeInfoAdapter;
    int mRecipeID;
    public static List<RecipeIngredientInfo> RecipeIngredientInfoList = new ArrayList<>();
    public static final int ID_RECIPE_INFO_LOADER = 119;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_RECIPE_INFO_LOADER, null, this);
        LoaderManager.enableDebugLogging(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflate MainActivity layout and bind ButterKnife using rootView
        View rootView = inflater.inflate(R.layout.fragment_recipe_info, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeInfoRv.setLayoutManager(layoutManager);
        recipeInfoRv.setHasFixedSize(true);

        //TODO:UNLOAD RECIPE ID/POSITION and USE In ASYNCTASK call to Util Class
        if (getArguments() != null){
            mRecipeID = getArguments().getInt("recipe_id");
        } else{
            Log.v(TAG, "Error Receiving bundle from RecipeInfoActivity. Bundle may be null.");
        }

        return rootView;
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

                return new AsyncTaskLoader<List<RecipeIngredientInfo>>(getContext()) {
                    List<RecipeIngredientInfo> mRecipeIngredientInfo;

                    @Override
                    protected void onStartLoading() {
                        if (mRecipeIngredientInfo != null) {
                            deliverResult(mRecipeIngredientInfo);
                            System.out.println("BAKING DATA! delivering RECIPE INFO!");
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
            //TODO: Declare and set a RecyclerViewAdapter
            mRecipeInfoAdapter = new RecipesInfoAdapter(getContext(), RecipeIngredientInfoList, this);
            recipeInfoRv.setAdapter(mRecipeInfoAdapter);
            recipeInfoRv.setHasFixedSize(true);
            mRecipeInfoAdapter.notifyRecipeIngredientInfoChange(RecipeIngredientInfoList);
        } else{
            Log.v("ASYNC TASK README: ", "Baking Data is null or empty.");

            //TODO: load this fragment as the top of RecipeInfoActivity.
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<RecipeIngredientInfo>> loader) {

    }
}
