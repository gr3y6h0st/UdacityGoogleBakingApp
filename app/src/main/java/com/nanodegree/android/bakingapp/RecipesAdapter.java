package com.nanodegree.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.android.bakingapp.BakingData.BakingData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private static final String TAG = RecipesAdapter.class.getSimpleName();

    Context mContext;

    //TODO: create a data source that can be declared here ex.public List<BakingData> mData;
    private List<BakingData> mData;

    final private RecipesAdapterOnClickListener mOnClickListener;

    //TODO: upload and create drawableIDs for the 4 recipes (Nutella Pie, Brownies, Yellow Cake, Cheese Cake)
    private int[] recipeImages = {R.drawable.cheesecake_stock, R.drawable.cheesecake_stock,
            R.drawable.cheesecake_stock, R.drawable.cheesecake_stock};


    public RecipesAdapter(Context context, List<BakingData> bakingData, RecipesAdapterOnClickListener listener ) {
        this.mContext = context;
        this.mData = bakingData;
        this.mOnClickListener = listener;
    }

    public interface RecipesAdapterOnClickListener {
        void onItemClick(int clickedPosition);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.recipes_list_item_main, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        if(mData == null) {
            return;
        }

        Log.d(TAG, "#" + position);

        //display the images here
        holder.setBakingAppData(mData.get(position), position);

    }

    @Override
    public int getItemCount() {
        if(null == mData) return 0;
        return mData.size();
    }

    public class RecipeViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_main_iv)
        ImageView listImageView;
        @BindView(R.id.recipe_main_tv)
        TextView listRecipeNameTextview;

        RecipeViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onItemClick(clickedPosition);

        }

        public void setBakingAppData(BakingData bakingAppData, int position){
            listRecipeNameTextview.setText(bakingAppData.getRecipe_name());
            if(bakingAppData.getRecipe_image() == 0 ){
                mData.get(position).setRecipe_image(recipeImages[position]);
                listImageView.setImageResource(bakingAppData.getRecipe_image());

            } else {
                //Will proceed with the image provided from data source
                listImageView.setImageResource(bakingAppData.getRecipe_image());
            }

        }
    }

    public void notifyBakingDataChange(List<BakingData> bakingData){
        mData = new ArrayList<BakingData>();
        mData = bakingData;

        notifyDataSetChanged();
    }
}
