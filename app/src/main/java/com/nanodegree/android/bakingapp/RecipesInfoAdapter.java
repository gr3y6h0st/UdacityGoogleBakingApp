package com.nanodegree.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.android.bakingapp.BakingData.RecipeIngredientInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesInfoAdapter extends RecyclerView.Adapter<RecipesInfoAdapter.RecipesInfoViewHolder> {

    private static final String TAG = RecipesInfoAdapter.class.getSimpleName();

    Context mContext;

    private List<RecipeIngredientInfo> mData;

    final private RecipesInfoAdapterOnClickListener mOnClickListener;

    public RecipesInfoAdapter(Context context, List<RecipeIngredientInfo> recipeIngredientInfo, RecipesInfoAdapterOnClickListener listener ) {
        this.mContext = context;
        this.mData = recipeIngredientInfo;
        this.mOnClickListener = listener;
    }

    public interface RecipesInfoAdapterOnClickListener {
        void onItemClick(int clickedPosition);
    }

    @NonNull
    @Override
    public RecipesInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.recipe_info_list_item, parent, false);

        return new RecipesInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesInfoViewHolder holder, int position) {
        if(mData == null) {
            return;
        }

        Log.d(TAG, "#" + position);

        //display the images here
        holder.setRecipeInfoData(mData.get(position), position);

    }

    @Override
    public int getItemCount() {
        if(null == mData) return 0;
        return mData.size();
    }

    public class RecipesInfoViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_info_ingredients_tv)
        TextView listRecipeInfoStepsTextView;
        @BindView(R.id.recipe_info_steps_tv)
        TextView listRecipeInfoIngredientTextview;

        RecipesInfoViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onItemClick(clickedPosition);

        }

        public void setRecipeInfoData(RecipeIngredientInfo recipeIngredientInfo, int position){
            listRecipeInfoIngredientTextview.setText(recipeIngredientInfo.getIngredient_quantity().toString());
            listRecipeInfoStepsTextView.setText(recipeIngredientInfo.getIngredient_measure());
        }
    }

    public void notifyRecipeIngredientInfoChange(List<RecipeIngredientInfo> recipeIngredientInfo){
        mData = new ArrayList<RecipeIngredientInfo>();
        mData = recipeIngredientInfo;

        notifyDataSetChanged();
    }
}
