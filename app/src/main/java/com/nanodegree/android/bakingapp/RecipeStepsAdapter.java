package com.nanodegree.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.bakingapp.BakingData.RecipeSteps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

        private static final String TAG = RecipesInfoAdapter.class.getSimpleName();

        Context mContext;

        private List<RecipeSteps> mData;

        final private RecipeStepsAdapterOnClickListener mOnClickListener;

    public RecipeStepsAdapter(Context context, List<RecipeSteps> recipeSteps, RecipeStepsAdapter.RecipeStepsAdapterOnClickListener listener ) {
            this.mContext = context;
            this.mData = recipeSteps;
            this.mOnClickListener = listener;
        }

        public interface RecipeStepsAdapterOnClickListener {
            void onItemClick(int clickedPosition);
        }

        @NonNull
        @Override
        public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.recipe_steps_list_item, parent, false);

            return new RecipeStepsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
            if(mData == null) {
                return;
            }

            Log.d(TAG, "#" + position);

            //display the images here
            holder.setRecipeStepsData(mData.get(position), position);

        }

        @Override
        public int getItemCount() {
            if(null == mData) return 0;
            return mData.size();
        }

        public class RecipeStepsViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
            @BindView(R.id.recipe_info_videoURL_tv)
            TextView listRecipeInfoStepsTextView;
            @BindView(R.id.recipe_steps_desc_tv)
            TextView listRecipeInfoIngredientTextview;

            RecipeStepsViewHolder(View view){
                super(view);
                ButterKnife.bind(this,view);
                view.setOnClickListener(this);
            }
            @Override
            public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                mOnClickListener.onItemClick(clickedPosition);

            }

            public void setRecipeStepsData(RecipeSteps recipeSteps, int position){

                //TODO:handle how to display videos in EXOPLAYER + if they don't exist.
                listRecipeInfoIngredientTextview.setText(recipeSteps.getStep_videoURL());

                //There is a repeat "Recipe Introduction" Items @ position 0.
                listRecipeInfoStepsTextView.setText(recipeSteps.getStep_description());
            }
        }

        public void notifyRecipeStepsChange(List<RecipeSteps> recipeSteps){
            mData = new ArrayList<>();
            mData = recipeSteps;

            notifyDataSetChanged();
        }
}
