package com.thomashorta.bakingtime;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.thomashorta.bakingtime.adapter.RecipeDetailAdapter;
import com.thomashorta.bakingtime.model.Recipe;
import com.thomashorta.bakingtime.model.Step;

import static com.thomashorta.bakingtime.RecipeDetailFragment.EXTRA_RECIPE;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailAdapter.OnStepClickListener,
        StepDetailFragment.OnStepButtonClickListener{
    private static final String EXTRA_CURRENT_STEP = "current_step";
    private static final String EXTRA_BACK_STACK = "backstack";

    private static final int ANIM_NONE = 0;
    private static final int ANIM_NEXT = 1;
    private static final int ANIM_PREVIOUS = 2;
    private static final int ANIM_FIRST = 3;

    private static final String TAG_RECIPE_DETAIL = "recipe_detail_fragment";
    private static final String TAG_STEP_DETAIL = "step_detail_fragment";

    private boolean mTwoPane;
    private Recipe mRecipe;
    private int mCurrentStepPos;

    private int mBackStack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mCurrentStepPos = -1;
        mTwoPane = findViewById(R.id.step_detail_container) != null;

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            Intent intent = getIntent();
            if (intent.hasExtra(EXTRA_RECIPE)) {
                mRecipe = intent.getParcelableExtra(EXTRA_RECIPE);
                RecipeDetailFragment detailFragment = RecipeDetailFragment.newInstance(mRecipe);
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_detail_container, detailFragment, TAG_RECIPE_DETAIL)
                        .commit();
            }
        } else {
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            mCurrentStepPos = savedInstanceState.getInt(EXTRA_CURRENT_STEP);
            mBackStack = savedInstanceState.getInt(EXTRA_BACK_STACK);
        }
        if (mRecipe != null) setTitle(mRecipe.getName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_RECIPE, mRecipe);
        outState.putInt(EXTRA_CURRENT_STEP, mCurrentStepPos);
        outState.putInt(EXTRA_BACK_STACK, mBackStack);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mBackStack > 0) {
                popBackStack();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mBackStack > 0) {
            popBackStack();
            return;
        }
        super.onBackPressed();
    }

    private void popBackStack() {
        getSupportFragmentManager().popBackStackImmediate();
        mBackStack--;
    }

    @Override
    public void onStepClick(int stepPosition) {
        if (mTwoPane) {
            showStep(stepPosition, R.id.step_detail_container, false, ANIM_NONE);
            findViewById(R.id.tv_show_details).setVisibility(View.GONE);
        } else {
            showStep(stepPosition, R.id.recipe_detail_container, true, ANIM_FIRST);
        }
    }

    private void showStep(int stepIdx, int container,
                          boolean addToBackStack, int animDirection) {
        mCurrentStepPos = stepIdx;
        boolean showPrevious = !mTwoPane && stepIdx > 0;
        boolean showNext =  !mTwoPane && (stepIdx < mRecipe.getSteps().size() - 1);

        Step step = mRecipe.getSteps().get(stepIdx);
        StepDetailFragment fragment = StepDetailFragment.newInstance(step, stepIdx + 1,
                showPrevious, showNext);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (animDirection == ANIM_FIRST) {
            transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top,
                    R.anim.slide_in_top, R.anim.slide_out_bottom);
        } else if (animDirection == ANIM_NEXT) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (animDirection == ANIM_PREVIOUS) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        transaction.replace(container, fragment, TAG_STEP_DETAIL);

        if (addToBackStack) {
            transaction.addToBackStack(null);
            mBackStack++;
        }
        transaction.commit();
    }

    @Override
    public void onClickPrevious() {
        if (mCurrentStepPos > 0) {
            showStep(mCurrentStepPos - 1, R.id.recipe_detail_container, false, ANIM_PREVIOUS);
        }
    }

    @Override
    public void onClickNext() {
        if (mCurrentStepPos < mRecipe.getSteps().size() -1) {
            showStep(mCurrentStepPos + 1, R.id.recipe_detail_container, false, ANIM_NEXT);
        }
    }
}
