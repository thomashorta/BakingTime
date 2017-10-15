package com.thomashorta.bakingtime;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thomashorta.bakingtime.adapter.RecipeDetailAdapter;
import com.thomashorta.bakingtime.model.Recipe;

public class RecipeDetailFragment extends Fragment {
    public static final String EXTRA_RECIPE = "recipe";

    private Recipe mRecipe;
    private RecyclerView mRecipeDetailList;
    private RecipeDetailAdapter mRecipeDetailAdapter;

    private RecipeDetailAdapter.OnStepClickListener mListener;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(EXTRA_RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecipeDetailList = (RecyclerView) rootView.findViewById(R.id.rv_recipe_detail);
        mRecipeDetailList.setLayoutManager(lm);
        mRecipeDetailList.setHasFixedSize(true);

        mRecipeDetailAdapter = new RecipeDetailAdapter(getContext(), mListener);
        mRecipeDetailList.setAdapter(mRecipeDetailAdapter);
        mRecipeDetailAdapter.setRecipe(mRecipe);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeDetailAdapter.OnStepClickListener) {
            mListener = (RecipeDetailAdapter.OnStepClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RecipeDetailAdapter.OnStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
