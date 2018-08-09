package com.nanodegree.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nanodegree.android.bakingapp.BakingData.RecipeSteps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailedRecipeStepsFragment extends Fragment {
    private final String TAG = DetailedRecipeStepsFragment.class.getSimpleName();

    @BindView(R.id.detail_steps_tv)
    TextView detailedStepsTv;

    @BindView(R.id.bttn_next_step)
    Button nextBttn;


    public DetailedRecipeStepsFragment() {
        // Required empty public constructor
    }

    List<RecipeSteps> recipeSteps;
    int currentStepPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detailed_recipe_activity, container, false);
        ButterKnife.bind(this, rootView);


        if (getArguments() != null){
            recipeSteps = (List<RecipeSteps>) getArguments().getSerializable("recipeSteps");
            assert recipeSteps != null;
            detailedStepsTv.setText(recipeSteps.get(currentStepPosition).getStep_description());

            nextBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //size correct?
                    if(currentStepPosition < recipeSteps.size()-1){
                        currentStepPosition++;
                    } else {
                        currentStepPosition = 0;
                    }
                    detailedStepsTv.setText(recipeSteps.get(currentStepPosition).getStep_description());
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


}
