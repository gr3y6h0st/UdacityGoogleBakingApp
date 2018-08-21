package com.nanodegree.android.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
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

    int windowIndex;
    long lastPlayerPosition;
    boolean autoPlay = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

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
                prevBttn.setVisibility(View.GONE);
            }

            if(recipeSteps.get(currentStepPosition).getStep_videoURL().isEmpty()){
                //if videoURL isn't available/empty, check thumbnailUrl
                if(recipeSteps.get(currentStepPosition).getStep_thumbnailURL().isEmpty()){

                    //TODO:Could just provide Placeholder Resource instead of making invisible.
                    mRecipeStepPv.setVisibility(View.GONE);

                } else {
                    rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_thumbnailURL();
                    // Initialize Exoplayer w/ step Url.
                    initializePlayer();
                }
            } else{
                //otherwise set VideoUrl if available.
                rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_videoURL();
                // Initialize Exoplayer w/ step Url.
                initializePlayer();
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
                                initializePlayer();
                            }
                        } else{
                            //otherwise set VideoUrl if available.
                            rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_videoURL();
                            initializePlayer();
                        }
                        //set nextBttn text to "Back to 1st Step"
                        if(currentStepPosition == recipeSteps.size()-1){
                            nextBttn.setText(R.string.next_bttn_end_back_to_intro);
                        }

                    } else {
                        currentStepPosition = 0;
                        prevBttn.setVisibility(View.GONE);
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
                        initializePlayer();
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
                                initializePlayer();
                            }
                        } else {
                            //otherwise set VideoUrl if available.
                            rStepVideoUrl = recipeSteps.get(currentStepPosition).getStep_videoURL();
                            initializePlayer();
                        }
                    }
                    else if(currentStepPosition == 0){
                        prevBttn.setVisibility(View.GONE);
                    }

                    detailedStepsTv.setText(recipeSteps.get(currentStepPosition).getStep_description());

                    if (mRecipeStepPv.getVisibility() == View.VISIBLE) {
                        // Initialize Exoplayer w/ step Url only if the View is Visible!.
                        initializePlayer();
                    } else {
                        Log.v(TAG, "PlayerView is not visible, will not initialize player.");
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
     */
    private void initializePlayer() {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity()), new DefaultTrackSelector(), loadControl);
            String userAgent = Util.getUserAgent(getContext(), getResources().getString(R.string.app_name));
            // Prepare the MediaSource.

            mRecipeStepPv.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(true);

            MediaSource mediaSource = new ExtractorMediaSource
                    .Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(Uri.parse(rStepVideoUrl));
            mExoPlayer.prepare(mediaSource,false, false);

        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        //Check if Exoplayer variable is null first. Release player if not null.
        if (mExoPlayer != null){
            lastPlayerPosition = mExoPlayer.getCurrentPosition();
            windowIndex = mExoPlayer.getCurrentWindowIndex();
            autoPlay = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //hide all buttons and textviews in landscape.
            prevBttn.setVisibility(View.GONE);
            nextBttn.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRecipeStepPv.getLayoutParams();
            //modify dimensions of the PlayerView to extend as big as it can.
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            mRecipeStepPv.setLayoutParams(params);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //show buttons and textviews
            prevBttn.setVisibility(View.VISIBLE);
            nextBttn.setVisibility(View.VISIBLE);
            detailedStepsTv.setVisibility(View.VISIBLE);
            //modify dimensions of the PlayerView to original state.
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRecipeStepPv.getLayoutParams();
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height=ViewGroup.LayoutParams.WRAP_CONTENT;
            mRecipeStepPv.setLayoutParams(params);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {

        currentState.putSerializable(RECIPE_DETAILED_STEPS, (ArrayList<RecipeSteps>) recipeSteps);
        currentState.putInt(CURRENT_STEP, currentStepPosition);
    }

}
