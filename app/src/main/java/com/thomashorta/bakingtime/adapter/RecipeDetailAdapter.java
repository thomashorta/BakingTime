package com.thomashorta.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thomashorta.bakingtime.R;
import com.thomashorta.bakingtime.model.Ingredient;
import com.thomashorta.bakingtime.model.Recipe;
import com.thomashorta.bakingtime.model.RecipeDetailItem;
import com.thomashorta.bakingtime.model.Step;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecipeDetailAdapter
        extends RecyclerView.Adapter<RecipeDetailAdapter.DetailItemViewHolder>{
    private float INGREDIENT_LINE_SPACING = 1.5f;

    private WeakReference<Context> mContext;
    private OnStepClickListener mStepClickListener;
    private Recipe mRecipe;

    public RecipeDetailAdapter(Context context, OnStepClickListener listener) {
        mContext = new WeakReference<>(context);
        mStepClickListener = listener;
    }

    @Override
    public DetailItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recipe_detail_card, parent, false);
        return new DetailItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailItemViewHolder holder, int position) {
        String title;
        String desc;

        if (position == 0) {
            // ingredient
            title = mContext.get().getString(R.string.ingredients_title);
            StringBuilder builder = new StringBuilder();
            List<Ingredient> ingredientList = mRecipe.getIngredients();
            for (int i = 0; i < ingredientList.size(); i++) {
                if (i > 0) builder.append("\n");
                Ingredient ingredient = ingredientList.get(i);
                builder.append(String.format(mContext.get().getString(
                        R.string.ingredient_list_item_format), ingredient.getIngredient(),
                        ingredient.getQuantity(), ingredient.getMeasure()));
            }
            desc = builder.toString();
        } else {
            int idx = position - 1;
            Step step = mRecipe.getSteps().get(idx);
            title = String.format(mContext.get().getString(R.string.steps_title_format), position);
            desc = step.getShortDescription();
        }

        RecipeDetailItem recipeDetail = new RecipeDetailItem(title, desc);
        holder.setDetailItem(recipeDetail, position == 0 ? INGREDIENT_LINE_SPACING : null);
    }

    @Override
    public int getItemCount() {
        return mRecipe != null ? mRecipe.getSteps().size() + 1 : 0;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
        notifyDataSetChanged();
    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    public interface OnStepClickListener {
        void onStepClick(Step step, int totalSteps);
    }

    public class DetailItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mDetailTitle;
        private TextView mDetailDescription;

        public DetailItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mDetailTitle = (TextView) itemView.findViewById(R.id.tv_recipe_detail_card_title);
            mDetailDescription =
                    (TextView) itemView.findViewById(R.id.tv_recipe_detail_card_description);
        }

        public void setDetailItem(RecipeDetailItem item, Float lineSpacingMult) {
            mDetailTitle.setText(item.getTitle());
            mDetailDescription.setText(item.getShortDescription());
            if (lineSpacingMult != null) mDetailDescription.setLineSpacing(0, lineSpacingMult);
        }

        @Override
        public void onClick(View v) {
            // ingredients list
            if (getAdapterPosition() == 0) return;

            if (mStepClickListener != null) {
                Step step = mRecipe.getSteps().get(getAdapterPosition() - 1);
                mStepClickListener.onStepClick(step, mRecipe.getSteps().size());
            }
        }
    }
}
