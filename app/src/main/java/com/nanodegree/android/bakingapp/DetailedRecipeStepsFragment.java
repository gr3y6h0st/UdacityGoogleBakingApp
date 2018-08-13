package com.nanodegree.android.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nanodegree.android.bakingapp.BakingData.RecipeSteps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailedRecipeStepsFragment extends Fragment {
    private final String TAG = DetailedRecipeStepsFragment.class.getSimpleName();
    public static final String RECIPE_DETAILED_STEPS = "recipeDetailStepsData";
    public static final String CURRENT_STEP = "currentRecipe";

    @BindView(R.id.detail_steps_tv)
    TextView detailedStepsTv;

    @BindView(R.id.bttn_next_step)
    Button nextBttn;

    @BindView(R.id.bttn_prev_step)
    Button prevBttn;

    @BindView(R.id.recipeStepPlayerView)
    PlayerView mRecipeStepPv; //per latest ExoPlayer ver. SimpleExoPlayerView is deprecated.

    private SimpleExoPlayer mExoPlayer;


    public DetailedRecipeStepsFragment() {
        // Required empty public constructor
    }

    List<RecipeSteps> recipeSteps;
    int currentStepPosition;
    String rStepVideoUrl = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            //obtains and sets recipeSteps data from savedInstance to handle rotation
            recipeSteps = (List<RecipeSteps>) savedInstanceState.getSerializable(RECIPE_DETAILED_STEPS);

            //obtains and sets saved position from savedInstance
            currentStepPosition = savedInstanceState.getInt(CURRENT_STEP);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detailed_recipe_activity, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null){
            recipeSteps = (List<RecipeSteps>) getArguments().getSerializable("recipeSteps");
            assert recipeSteps != null;

            detailedStepsTv.setText(recipeSteps.get(currentStepPosition).getStep_description());
            if(currentStepPosition == 0){
                prevBttn.setVisibility(View.INVISIBLE);
            }

            if(recipeSteps.get(currentStepPosition).getStep_videoURL().isEmpty()){
                //if videoURL isn't available/empty, check thumbnailUrl
                if(recipeSteps.get(currentStepPosition).getStep_thumbnailURL().isEmpty()){

                    //TODO:Could just provide Placeholder Resource instead of making invisible.
                    mRecipeStepPv.setVisibility(View.GONE);

                } else {
                    rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_thumbnailURL();
                    // Initialize Exoplayer w/ step Url.
                    initializePlayer(Uri.parse(rStepVideoUrl));
                }
            } else{
                //otherwise set VideoUrl if available.
                rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_videoURL();
                // Initialize Exoplayer w/ step Url.
                initializePlayer(Uri.parse(rStepVideoUrl));
            }

            nextBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mRecipeStepPv.getVisibility() == View.GONE){
                        mRecipeStepPv.setVisibility(View.VISIBLE);
                    }

                    //release current video on button press
                    releasePlayer();

                    //track step position and compare w/ steps list size - 1.
                    if(currentStepPosition < recipeSteps.size()-1){
                        currentStepPosition++;
                        prevBttn.setVisibility(View.VISIBLE);

                        //sets title of Activity to short desc of current step
                        getActivity().setTitle(recipeSteps.get(currentStepPosition).getStep_shortdescription());

                        if(recipeSteps.get(currentStepPosition).getStep_videoURL().isEmpty()){
                            //if videoURL isn't available/empty, check thumbnailUrl
                            if(recipeSteps.get(currentStepPosition).getStep_thumbnailURL().isEmpty()){
                                mRecipeStepPv.setVisibility(View.GONE);
                            } else {
                                rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_thumbnailURL();
                                initializePlayer(Uri.parse(rStepVideoUrl));
                            }
                        } else{
                            //otherwise set VideoUrl if available.
                            rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_videoURL();
                            initializePlayer(Uri.parse(rStepVideoUrl));
                        }
                        //set nextBttn text to "Back to 1st Step"
                        if(currentStepPosition == recipeSteps.size()-1){
                            nextBttn.setText(R.string.next_bttn_end_back_to_intro);
                        }

                    } else {
                        currentStepPosition = 0;
                        prevBttn.setVisibility(View.INVISIBLE);
                        nextBttn.setText(getString(R.string.next_step_button_txt));

                        if(recipeSteps.get(currentStepPosition).getStep_videoURL().isEmpty()){
                            //if videoURL isn't available/empty, check thumbnailUrl
                            if(recipeSteps.get(currentStepPosition).getStep_thumbnailURL().isEmpty()){
                                mRecipeStepPv.setVisibility(View.INVISIBLE);
                            } else {
                                rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_thumbnailURL();
                            }
                        } else{
                            //otherwise set VideoUrl if available.
                            rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_videoURL();
                        }
                    }


                    detailedStepsTv.setText(recipeSteps.get(currentStepPosition).getStep_description());

                    if(mRecipeStepPv.getVisibility() == View.VISIBLE){
                        // Initialize Exoplayer w/ step Url only if the View is Visible!.
                        initializePlayer(Uri.parse(rStepVideoUrl));
                    } else{
                        Log.v(TAG, "SimpleExoPlayerView is not visible, will not initialize player.");
                    }

                }

            });

            prevBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRecipeStepPv.getVisibility() == View.GONE) {
                        mRecipeStepPv.setVisibility(View.VISIBLE);
                    }

                    //release current video on button press
                    releasePlayer();

                    //track step position and compare w/ steps list size - 1.
                    if (currentStepPosition != 0) {
                        prevBttn.setVisibility(View.VISIBLE);
                        --currentStepPosition;

                        //sets title of Activity to short desc of current step
                        getActivity().setTitle(recipeSteps.get(currentStepPosition).getStep_shortdescription());

                        if (recipeSteps.get(currentStepPosition).getStep_videoURL().isEmpty()) {
                            //if videoURL isn't available/empty, check thumbnailUrl
                            if (recipeSteps.get(currentStepPosition).getStep_thumbnailURL().isEmpty()) {
                                mRecipeStepPv.setVisibility(View.GONE);
                            } else {
                                rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_thumbnailURL();
                                initializePlayer(Uri.parse(rStepVideoUrl));
                            }
                        } else {
                            //otherwise set VideoUrl if available.
                            rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_videoURL();
                            initializePlayer(Uri.parse(rStepVideoUrl));
                        }
                    }
                    else if(currentStepPosition == 0){
                        prevBttn.setVisibility(View.INVISIBLE);
                    }

                    detailedStepsTv.setText(recipeSteps.get(currentStepPosition).getStep_description());

                    if (mRecipeStepPv.getVisibility() == View.VISIBLE) {
                        // Initialize Exoplayer w/ step Url only if the View is Visible!.
                        initializePlayer(Uri.parse(rStepVideoUrl));
                    } else {
                        Log.v(TAG, "SimpleExoPlayerView is not visible, will not initialize player.");
                    }
                }

            });


        } else{
            Log.v(TAG, "Error Receiving bundle from DetailedRecipeStepsActivity. Bundle may be null.");
        }

        return rootView;
    }

    public void setStepPosition(int position){
        currentStepPosition = position;
    }


    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mRecipeStepPv.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getResources().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        //Check if Exoplayer variable is null first. Release player if not null.
        if (mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {

        currentState.putSerializable(RECIPE_DETAILED_STEPS, (ArrayList<RecipeSteps>) recipeSteps);
        currentState.putInt(CURRENT_STEP, currentStepPosition);

    }
}
