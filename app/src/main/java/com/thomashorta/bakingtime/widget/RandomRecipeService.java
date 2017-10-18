package com.thomashorta.bakingtime.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.thomashorta.bakingtime.R;
import com.thomashorta.bakingtime.RecipeUtil;
import com.thomashorta.bakingtime.model.Recipe;

import java.util.ArrayList;

public class RandomRecipeService extends IntentService {
    private static final String ACTION_UPDATE_RANDOM_RECIPE = "update_random_recipe";
    private static final String ACTION_GET_RANDOM_RECIPE = "get_random_recipe";
    private static final String EXTRA_RECIPE_LIST = "recipe_list";

    public RandomRecipeService() {
        super(RandomRecipeService.class.getSimpleName());
    }

    public static void startGetRandomRecipe(Context context, ArrayList<Recipe> recipeList) {
        Intent intent = new Intent(context, RandomRecipeService.class);
        intent.setAction(ACTION_GET_RANDOM_RECIPE);
        intent.putParcelableArrayListExtra(EXTRA_RECIPE_LIST, recipeList);
        context.startService(intent);
    }

    public static void startUpdateRandomRecipe(Context context) {
        Intent intent = new Intent(context, RandomRecipeService.class);
        intent.setAction(ACTION_UPDATE_RANDOM_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;

        Recipe recipe = null;
        if (ACTION_GET_RANDOM_RECIPE.equals(intent.getAction())) {
            ArrayList<Recipe> recipeList = intent.getParcelableArrayListExtra(EXTRA_RECIPE_LIST);
            recipe = getRandomRecipe(recipeList);
        } else if (ACTION_UPDATE_RANDOM_RECIPE.equals(intent.getAction())) {
            ArrayList<Recipe> recipeList = RecipeUtil.requestRecipeList();
            if (recipeList != null) recipe = getRandomRecipe(recipeList);
            if (recipe == null) recipe = RecipeUtil.getRandomRecipeFromPref(this);
        } else {
            return;
        }

        RecipeUtil.storeRandomRecipeToPref(this, recipe);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                RandomRecipeWidgetProvider.class));
        RandomRecipeWidgetProvider.updateWidgets(this, recipe, appWidgetManager, appWidgetIds);
    }

    private static Recipe getRandomRecipe(ArrayList<Recipe> recipeList) {
        int idx = (int) Math.floor(Math.random() * recipeList.size());
        return recipeList.get(idx);
    }
}
