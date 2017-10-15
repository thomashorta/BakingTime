package com.thomashorta.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thomashorta.bakingtime.R;
import com.thomashorta.bakingtime.model.Recipe;

import java.util.ArrayList;

public class RecipeListAdapter
        extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>{

    private OnRecipeClickListener mRecipeClickListener;
    private ArrayList<Recipe> mRecipeList;

    public RecipeListAdapter(OnRecipeClickListener listener) {
        mRecipeClickListener = listener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        holder.setRecipeDetails(recipe.getName(), recipe.getServings(),
                recipe.getIngredients().size());
    }

    @Override
    public int getItemCount() {
        return mRecipeList != null ? mRecipeList.size() : 0;
    }

    public void setRecipeList(ArrayList<Recipe> recipeList) {
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getRecipeList() {
        return mRecipeList;
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mRecipeTitle;
        private TextView mRecipeServingIngredients;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRecipeTitle = (TextView) itemView.findViewById(R.id.tv_recipe_title);
            mRecipeServingIngredients =
                    (TextView) itemView.findViewById(R.id.tv_recipe_serving_ingredients);
        }

        public void setRecipeDetails(String title, int servingQty, int ingredientQty) {
            Context context = mRecipeTitle.getContext();
            String servings = context.getResources()
                    .getQuantityString(R.plurals.servings_text, servingQty, servingQty);
            String ingredients = context.getResources()
                    .getQuantityString(R.plurals.ingredients_text, ingredientQty, ingredientQty);
            String servingsIngredients = String.format(
                    context.getString(R.string.servings_ingredients_format), servings, ingredients);

            mRecipeTitle.setText(title);
            mRecipeServingIngredients.setText(servingsIngredients);
        }

        @Override
        public void onClick(View v) {
            if (mRecipeClickListener != null) {
                mRecipeClickListener.onRecipeClick(mRecipeList.get(getAdapterPosition()));
            }
        }
    }
}
