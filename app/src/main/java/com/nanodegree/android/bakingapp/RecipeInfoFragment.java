package com.nanodegree.android.bakingapp;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nanodegree.android.bakingapp.BakingAppWidget.BakingAppWidgetProvider;
import com.nanodegree.android.bakingapp.BakingData.BakingData;
import com.nanodegree.android.bakingapp.BakingData.RecipeIngredientInfo;
import com.nanodegree.android.bakingapp.Utils.BakingAppDatabaseJsonUtils;
import com.nanodegree.android.bakingapp.Utils.NetworkUtils;
import com.nanodegree.android.bakingapp.Utils.RecipesInfoAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeInfoFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<RecipeIngredientInfo>>{


    private String mRecipeName;

    public RecipeInfoFragment(){

    }

    private final String TAG = RecipeInfoFragment.class.getSimpleName();
    @BindView(R.id.recipe_info_rv)
    RecyclerView recipeInfoRv;
    RecyclerView.LayoutManager mLayoutManager;
    private RecipesInfoAdapter mRecipeInfoAdapter;
    private Parcelable mPosition;
    private int mRecipeID;
    public static List<RecipeIngredientInfo> RecipeIngredientInfoList = new ArrayList<>();
    public static List<BakingData> BakingDataList = new ArrayList<>();
    public static final String RECIPE_INFO_DATA = "RecipeIngredientInfoList";
    private static final String RVPOSITION = "recipeInfoRv";
    public static final int ID_RECIPE_INFO_LOADER = 119;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_RECIPE_INFO_LOADER, null, this);
        LoaderManager.enableDebugLogging(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home) {
            Toast.makeText(getContext(), "Back/Home", Toast.LENGTH_SHORT).show();
            NavUtils.navigateUpFromSameTask(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null){
            //check savedInstanceState bundle to obtain any current baking data
            RecipeIngredientInfoList = (List<RecipeIngredientInfo>) savedInstanceState.getSerializable(RECIPE_INFO_DATA);
            mPosition = savedInstanceState.getParcelable(RVPOSITION);
        }

        //inflate MainActivity layout and bind ButterKnife using rootView
        View rootView = inflater.inflate(R.layout.fragment_recipe_info, container, false);
        ButterKnife.bind(this, rootView);

        mLayoutManager = new LinearLayoutManager(getContext());
        recipeInfoRv.setLayoutManager(mLayoutManager);
        recipeInfoRv.setHasFixedSize(true);

        if (getArguments() != null){
            mRecipeID = getArguments().getInt("recipe_id");
            mRecipeName = getArguments().getString("recipe_name");
            Log.v(TAG, "RECEIVED! " + mRecipeID + mRecipeName);
        } else{
            Log.v(TAG, "Error Receiving bundle from RecipeInfoActivity. Bundle may be null.");
        }

        return rootView;
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
                            System.out.println("RECIPE INFO DATA forceloading.");
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
            mRecipeInfoAdapter = new RecipesInfoAdapter(getContext(), RecipeIngredientInfoList);
            recipeInfoRv.setAdapter(mRecipeInfoAdapter);
            recipeInfoRv.setHasFixedSize(true);
            mLayoutManager.onRestoreInstanceState(mPosition);

            mRecipeInfoAdapter.notifyRecipeIngredientInfoChange(RecipeIngredientInfoList);

            BakingAppDatabaseJsonUtils.getRecipeIngredientsCvData(getContext(), RecipeIngredientInfoList, mRecipeName, mRecipeID);


            BakingAppWidgetProvider.sendRefreshBroadcast(getContext());
        } else{
            Log.v("ASYNC TASK README: ", "RecipeIngredientInfo is null or empty.");

            //TODO: load this fragment as the top of RecipeInfoActivity.
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<RecipeIngredientInfo>> loader) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putSerializable(RECIPE_INFO_DATA, (ArrayList<RecipeIngredientInfo>) RecipeIngredientInfoList);
        mPosition = ((LinearLayoutManager) recipeInfoRv.getLayoutManager()).onSaveInstanceState();
        currentState.putParcelable(RVPOSITION, mPosition);

        //Debug purposes.
        String savedAdapterPosition = mLayoutManager.onSaveInstanceState().toString();
        Log.v(TAG, savedAdapterPosition);
    }

    public void setRecipeId(int recipeId){
        mRecipeID = recipeId;
    }

    public void setRecipeName(String recipeName){
        mRecipeName = recipeName;
    }
}
