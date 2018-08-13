package com.nanodegree.android.bakingapp;

import android.content.Context;
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

import com.nanodegree.android.bakingapp.BakingData.BakingData;
import com.nanodegree.android.bakingapp.Utils.BakingAppDatabaseJsonUtils;
import com.nanodegree.android.bakingapp.Utils.NetworkUtils;
import com.nanodegree.android.bakingapp.Utils.RecipesAdapter;

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
    public static final String BAKING_DATA = "baking_data";


    private RecipesAdapter mAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private Parcelable mPosition;
    public static final String RVPOSITION = "RecyclerView Position";
    public static List<BakingData> bakingDataList = new ArrayList<>();
    public static final int ID_BAKING_LOADER = 20;

    OnRecipeClickListener mCallback;

    public interface OnRecipeClickListener {
        void onRecipeSelected(int position, List<BakingData> bakingDataList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //makes sure HOST activity has implemented call back interface, throws exception if not.
        try{
            mCallback = (OnRecipeClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(getContext().toString()
                    + " must implement OnRecipeClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //check if savedInstanceState is not null. Restore extract restoreSavedState if it exists and is not null.
        //logs error if attempt to restoredSavedState is null.
        if (savedInstanceState != null) {
            //sets data/position to their previous state after rotation event
            bakingDataList = (List<BakingData>) savedInstanceState.getSerializable(BAKING_DATA);
            mPosition = savedInstanceState.getParcelable(RVPOSITION);
        }

        //inflate MainActivity layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_rv, container, false);

        //get reference to RecylerView in fragment layout.
        recyclerView = rootView.findViewById(R.id.recipe_main_frag_rv);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);

        //TODO: Obtain Bundle from MainActivity. Should hold an ArrayList of data pertaining to current recipe.
        LoaderManager.enableDebugLogging(true);

        return rootView;
    }

    @Override
    public void onItemClick(int clickedPosition) {

        if(bakingDataList != null){
            mCallback.onRecipeSelected(clickedPosition, bakingDataList);
        } else{
            Log.v(TAG, "Check to make sure Baking Data isn't null/empty.");
        }

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

                            //Log.v("ASYNC TASK READ ME: ", simpleJsonBakingData.get(0).getRecipe_name());

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
            //populate global List variable for use.
            bakingDataList = data;

            recyclerView.setHasFixedSize(true);
            mAdapter = new RecipesAdapter(getContext(), bakingDataList, this);
            recyclerView.setAdapter(mAdapter);

            //restore position on List
            mLinearLayoutManager.onRestoreInstanceState(mPosition);
            mAdapter.notifyBakingDataChange(data);
        } else{
            Log.v("ASYNC TASK README: ", "Baking Data is null or empty.");
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BakingData>> loader) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {

        currentState.putSerializable(BAKING_DATA, (ArrayList<BakingData>) bakingDataList);
        mPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).onSaveInstanceState();
        currentState.putParcelable(RVPOSITION, mPosition);


        String savedAdapterPosition = mLinearLayoutManager.onSaveInstanceState().toString();
        Log.v(TAG, savedAdapterPosition);
    }
}
