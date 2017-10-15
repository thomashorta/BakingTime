package com.thomashorta.bakingtime;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.thomashorta.bakingtime.adapter.RecipeDetailAdapter;
import com.thomashorta.bakingtime.model.Recipe;
import com.thomashorta.bakingtime.model.Step;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailAdapter.OnStepClickListener {
    private static final String EXTRA_NAME = "name";

    private boolean mTwoPane;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mTwoPane = findViewById(R.id.step_detail_container) != null;

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            Intent intent = getIntent();
            if (intent.hasExtra(RecipeDetailFragment.EXTRA_RECIPE)) {
                Recipe recipe = intent.getParcelableExtra(RecipeDetailFragment.EXTRA_RECIPE);
                RecipeDetailFragment detailFragment = RecipeDetailFragment.newInstance(recipe);
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_detail_container, detailFragment)
                        .commit();
                mName = recipe.getName();
            }
        } else {
            mName = savedInstanceState.getString(EXTRA_NAME);
        }
        setTitle(mName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_NAME, mName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStepClick(Step step, int totalSteps) {
        Toast.makeText(this, "Step " + (step.getId() + 1) + ": " + step.getDescription(), Toast.LENGTH_SHORT).show();

        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            // TODO: create a new step fragment
            // TODO: initialize it and show in place of old one
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(StepDetailFragment.EXTRA_STEP, step);
            intent.putExtra(StepDetailFragment.EXTRA_SHOW_PREVIOUS, step.getId() > 0);
            intent.putExtra(StepDetailFragment.EXTRA_SHOW_NEXT, step.getId() < totalSteps - 1);
            startActivity(intent);
        }
    }
}
