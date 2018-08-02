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
import com.nanodegree.android.bakingapp.Utils.BakingAppDatabaseJsonUtils;
import com.nanodegree.android.bakingapp.Utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        RecipesAdapter.RecipesAdapterOnClickListener,
        LoaderManager.LoaderCallbacks<List<BakingData>>{

    private final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recipe_main_rv) RecyclerView rv;
    private RecipesAdapter mAdapter;
    private Context mContext = this;
    public static List<BakingData> bakingDataList = new ArrayList<>();
    public static final int ID_BAKING_LOADER = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        rv.setHasFixedSize(true);

        //TODO: Declare and set a RecyclerViewAdapter
        mAdapter = new RecipesAdapter(mContext, bakingDataList, this);
        rv.setAdapter(mAdapter);

        LoaderManager.enableDebugLogging(true);


        getSupportLoaderManager().initLoader(ID_BAKING_LOADER, null, this);
    }

    @Override
    public void onItemClick(int clickedPosition) {

        Intent intentToStartRecipeInfoActivity = new Intent(MainActivity.this, Recipe_Information.class);
        intentToStartRecipeInfoActivity.putExtra("RecipeData", bakingDataList.get(clickedPosition));
        startActivity(intentToStartRecipeInfoActivity);
    }

    @NonNull
    @Override
    public Loader<List<BakingData>> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id) {
            case ID_BAKING_LOADER:

                return new AsyncTaskLoader<List<BakingData>>(this) {
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

            rv.setAdapter(mAdapter);
            rv.setHasFixedSize(true);
            mAdapter.notifyBakingDataChange(bakingDataList);
        } else{
            Log.v("ASYNC TASK README: ", "Baking Data is null or empty.");
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BakingData>> loader) {

    }
}
