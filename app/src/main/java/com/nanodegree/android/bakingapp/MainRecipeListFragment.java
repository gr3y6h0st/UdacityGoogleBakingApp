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

import com.nanodegree.android.bakingapp.BakingData.BakingData;
import com.nanodegree.android.bakingapp.Utils.BakingAppDatabaseJsonUtils;
import com.nanodegree.android.bakingapp.Utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainRecipeListFragment extends Fragment implements
        RecipesAdapter.RecipesAdapterOnClickListener,
        LoaderManager.LoaderCallbacks<List<BakingData>>{

    public MainRecipeListFragment(){

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID_BAKING_LOADER, null, this);
    }

    private final String TAG = MainActivity.class.getSimpleName();


    private RecipesAdapter mAdapter;
    private RecyclerView recyclerView;
    public static List<BakingData> bakingDataList = new ArrayList<>();
    public static final int ID_BAKING_LOADER = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        /*if(savedInstanceState != null){
            //check savedInstanceState bundle to obtain any current baking data
            bakingDataList = (List<BakingData>) savedInstanceState.getSerializable(MAIN_ACTIVITY_BAKING_DATA_EXTRA);
        } else{
            //if null then, check to see if an argument was set in MainActivity and obtain it.
        }*/

        //inflate MainActivity layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_rv, container, false);

        //bakingDataList = (List<BakingData>) getArguments().getSerializable(MAIN_ACTIVITY_BAKING_DATA_EXTRA);


        //get reference to RecylerView in fragment layout.
        recyclerView = rootView.findViewById(R.id.recipe_main_frag_rv);


        //TODO: Obtain Bundle from MainActivity. Should hold an ArrayList of data pertaining to current recipe.


        //TODO: Declare and set a RecyclerViewAdapter
        //mAdapter = new RecipesAdapter(getActivity(), bakingDataList, this);
        //recyclerView.setAdapter(mAdapter);

        LoaderManager.enableDebugLogging(true);

        return rootView;
    }

    @Override
    public void onItemClick(int clickedPosition) {

        Intent intentToStartRecipeInfoActivity = new Intent(getActivity(), RecipeStepsActivity.class);
        intentToStartRecipeInfoActivity.putExtra("RecipeData", bakingDataList.get(clickedPosition));
        startActivity(intentToStartRecipeInfoActivity);

    }

    @NonNull
    @Override
    public Loader<List<BakingData>> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id) {
            case ID_BAKING_LOADER:

                return new AsyncTaskLoader<List<BakingData>>(getContext()) {
                    List<BakingData> mBakingData;

                    @Override
                    protected void onStartLoading() {
                        if (mBakingData != null) {
                            deliverResult(mBakingData);
                            System.out.println("Baking Data isn't null, delivering data.");
                        } else {
                            System.out.println("Forceloading");

                            forceLoad();
                        }
                    }

                    @Nullable
                    @Override
                    public List<BakingData> loadInBackground() {

                        System.out.println("Loading Background started.");

                        try {
                            URL bakingDataRequestUrl = NetworkUtils.buildUrl();

                            String jsonBakingDataResponse = NetworkUtils.getResponseFromHttpUrl(bakingDataRequestUrl);


                            List<BakingData> simpleJsonBakingData = BakingAppDatabaseJsonUtils.getRecipeNames(jsonBakingDataResponse);

                            Log.v("ASYNC TASK READ ME: ", simpleJsonBakingData.get(0).getRecipe_name());

                            return simpleJsonBakingData;

                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    public void deliverResult(@Nullable List<BakingData> data) {
                        System.out.println(" DELIVERY RIGHT HERE.");
                        mBakingData = data;
                        super.deliverResult(data);
                    }
                };
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<BakingData>> loader, List<BakingData> data) {

        if(data != null){
            bakingDataList = data;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            mAdapter = new RecipesAdapter(getContext(), bakingDataList, this);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyBakingDataChange(data);
        } else{
            Log.v("ASYNC TASK README: ", "Baking Data is null or empty.");
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BakingData>> loader) {

    }
}
