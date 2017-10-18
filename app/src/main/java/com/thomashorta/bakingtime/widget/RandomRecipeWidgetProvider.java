package com.thomashorta.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.thomashorta.bakingtime.R;
import com.thomashorta.bakingtime.RecipeDetailActivity;
import com.thomashorta.bakingtime.RecipeDetailFragment;
import com.thomashorta.bakingtime.RecipeListActivity;
import com.thomashorta.bakingtime.model.Recipe;

public class RandomRecipeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RandomRecipeService.startUpdateRandomRecipe(context);
    }

    public static void updateWidgets(Context context, Recipe recipe,
                                     AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = getWidgetRemoteView(context, recipe);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }

    private static RemoteViews getWidgetRemoteView(Context context, Recipe recipe) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_random_recipe);
        // Update image and text
        Intent intent;
        if (recipe == null) {
            intent = new Intent(context, RecipeListActivity.class);

            views.setViewVisibility(R.id.widget_content, View.INVISIBLE);
            views.setViewVisibility(R.id.widget_open_app_message, View.VISIBLE);
        } else {
            intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.EXTRA_RECIPE, recipe);

            views.setViewVisibility(R.id.widget_content, View.VISIBLE);
            views.setViewVisibility(R.id.widget_open_app_message, View.INVISIBLE);

            views.setTextViewText(R.id.widget_title, recipe.getName());
            views.setTextViewText(R.id.widget_ingredients,
                    recipe.getSimpleIngredientsString(context));
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_content, pendingIntent);

        return views;
    }
}
