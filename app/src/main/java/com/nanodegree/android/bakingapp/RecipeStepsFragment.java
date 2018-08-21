package com.nanodegree.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.TextView;

import com.nanodegree.android.bakingapp.BakingData.BakingData;
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
    public static final String RVPOSITION = "stepsRvPosition";
    public static final String RECIPE_STEPS_DATA = "recipeStepsData";

    @BindView(R.id.recipe_steps_rv)
    RecyclerView recipeStepsRv;

    @BindView(R.id.recipe_steps_desc_title_tv)
    TextView recipeStepsTitleTv;

    RecipeStepsAdapter mRecipeStepsAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static List<BakingData> bakingData = new ArrayList<>();
    public static List<RecipeSteps> recipeStepsList = new ArrayList<>();
    private Parcelable mPosition;
    int mRecipeID;
    public static final int ID_RECIPE_STEPS_LOADER = 219;
    private String mRecipeName;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_RECIPE_STEPS_LOADER, null, this);
        LoaderManager.enableDebugLogging(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null){
            //check savedInstanceState bundle to obtain any current baking data
            recipeStepsList = (List<RecipeSteps>) savedInstanceState.getSerializable(RECIPE_STEPS_DATA);
            mPosition = savedInstanceState.getParcelable(RVPOSITION);

            mRecipeName = savedInstanceState.getString("recipeName");

        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(getContext());
        recipeStepsRv.setLayoutManager(layoutManager);
        recipeStepsRv.setHasFixedSize(true);
        //set label (recipe name) to Steps Fragment tv
        recipeStepsTitleTv.setText(mRecipeName);

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
                            System.out.println("Delivering RecipeSteps data!");
                        } else {
                            forceLoad();
                            System.out.println("Steps Data forceloading.");
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
            recipeStepsList = data;
            recipeStepsRv.setLayoutManager(layoutManager);
            recipeStepsRv.setHasFixedSize(true);

            //set the adapter to Rv.
            mRecipeStepsAdapter = new RecipeStepsAdapter(getContext(), recipeStepsList, this);
            recipeStepsRv.setAdapter(mRecipeStepsAdapter);
            layoutManager.onRestoreInstanceState(mPosition);

            //notify Adapter of data change.
            mRecipeStepsAdapter.notifyRecipeStepsChange(recipeStepsList);
        } else{
            Log.v("ASYNC TASK README: ", "recipeStepsList is null or empty.");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<RecipeSteps>> loader) {

    }

    @Override
    public void onItemClick(int clickedPosition) {

        //Toast.makeText(getContext(), recipeStepsList.get(clickedPosition).getStep_description(), Toast.LENGTH_SHORT).show();
        Intent intentToStartDetailedStepsActivity = new Intent(getActivity(), DetailedRecipeStepsActivity.class);
        intentToStartDetailedStepsActivity.putExtra("RecipeStepsData", (Serializable) recipeStepsList);
        intentToStartDetailedStepsActivity.putExtra("clickedPosition", clickedPosition);

        startActivity(intentToStartDetailedStepsActivity);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {

        //save data
        currentState.putSerializable(RECIPE_STEPS_DATA, (ArrayList<RecipeSteps>) recipeStepsList);

        //saves position
        mPosition = ((LinearLayoutManager) recipeStepsRv.getLayoutManager()).onSaveInstanceState();
        currentState.putParcelable(RVPOSITION, mPosition);

        //save RecipeName
        currentState.putString("recipeName", mRecipeName);

        //Debug purposes.
        String savedAdapterPosition = layoutManager.onSaveInstanceState().toString();
        Log.v(TAG, savedAdapterPosition);
    }

    public void setRecipeId(int recipeId){
        mRecipeID = recipeId;
    }
    
    public void setRecipeName(String recipeName){
        mRecipeName = recipeName;
    }
}
