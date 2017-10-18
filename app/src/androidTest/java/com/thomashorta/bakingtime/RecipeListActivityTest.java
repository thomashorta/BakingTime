package com.thomashorta.bakingtime;

import android.content.Intent;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;


@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    private static final String RECIPE_NAME = "Yellow Cake";

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule =
            new ActivityTestRule<RecipeListActivity>(RecipeListActivity.class, false, false) {
                @Override
                protected void afterActivityLaunched() {
                    super.afterActivityLaunched();
                    registerIdlingResource(getActivity());
                }

                @Override
                protected void afterActivityFinished() {
                    super.afterActivityFinished();
                    unregisterIdlingResource();
                }
            };

    private IdlingResource mIdlingResource;

    public void registerIdlingResource(RecipeListActivity activity) {
        mIdlingResource = activity.getIdlingResource();
        registerIdlingResources(mIdlingResource);
    }

    @Test
    public void clickRecyclerViewItem_OpensRecipeDetailActivity() {
        Intent intent = new Intent();
        intent.putExtra(RecipeListActivity.EXTRA_IS_TESTING, true);
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        // check if activity title (in action bar) matches the recipe name
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.action_bar))))
                .check(matches(withText(RECIPE_NAME)));
    }

    public void unregisterIdlingResource() {
        if (mIdlingResource != null) unregisterIdlingResources(mIdlingResource);
    }
}
