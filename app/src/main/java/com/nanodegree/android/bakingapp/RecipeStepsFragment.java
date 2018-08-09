package com.nanodegree.android.bakingapp;

import android.content.Intent;
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
import android.widget.Toast;

import com.nanodegree.android.bakingapp.BakingData.RecipeSteps;
import com.nanodegree.android.bakingapp.Utils.BakingAppDatabaseJsonUtils;
import com.nanodegree.android.bakingapp.Utils.NetworkUtils;
import com.nanodegree.android.bakingapp.Utils.RecipeStepsAdapter;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsFragment extends Fragment implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<List<RecipeSteps>>,
        RecipeStepsAdapter.RecipeStepsAdapterOnClickListener {

    private final String TAG = RecipeStepsFragment.class.getSimpleName();
    public static final String LIST_INDEX = "list_index";


    @BindView(R.id.recipe_steps_rv)
    RecyclerView recipeStepsRv;

    RecipeStepsAdapter mRecipeStepsAdapter;
    public static List<RecipeSteps> RecipeStepsList = new ArrayList<>();
    int mRecipeID;
    public static final int ID_RECIPE_STEPS_LOADER = 219;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_RECIPE_STEPS_LOADER, null, this);
        LoaderManager.enableDebugLogging(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recipeStepsRv.setLayoutManager(layoutManager);
        recipeStepsRv.setHasFixedSize(true);

        //TODO:UNLOAD RECIPE ID/POSITION and USE In ASYNCTASK call to Util Class
        if (getArguments() != null){
            mRecipeID = getArguments().getInt("recipe_id");
        } else{
            Log.v(TAG, "Error Receiving bundle from RecipeInfoActivity. Bundle may be null.");
        }

        return rootView;

    }

    @NonNull
    @Override
    public Loader<List<RecipeSteps>> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id) {

            case ID_RECIPE_STEPS_LOADER:
                System.out.println(id);

                return new AsyncTaskLoader<List<RecipeSteps>>(getContext()) {
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
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recipeStepsRv.setLayoutManager(layoutManager);
            recipeStepsRv.setHasFixedSize(true);

            //set the adapter to Rv.
            mRecipeStepsAdapter = new RecipeStepsAdapter(getContext(), RecipeStepsList, this);
            recipeStepsRv.setAdapter(mRecipeStepsAdapter);

            //notify Adapter of data change.
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
        Toast.makeText(getContext(), RecipeStepsList.get(clickedPosition).getStep_description(), Toast.LENGTH_SHORT).show();
        Intent intentToStartDetailedStepsActivity = new Intent(getActivity(), DetailedRecipeStepsActivity.class);
        intentToStartDetailedStepsActivity.putExtra("RecipeStepsData", (Serializable) RecipeStepsList);
        intentToStartDetailedStepsActivity.putExtra("clickedPosition", clickedPosition);

        startActivity(intentToStartDetailedStepsActivity);
    }
}
