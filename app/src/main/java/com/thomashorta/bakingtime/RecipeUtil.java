package com.thomashorta.bakingtime;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thomashorta.bakingtime.model.Recipe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by thomas on 18/10/2017.
 */

public class RecipeUtil {
    private static final String TAG = RecipeUtil.class.getSimpleName();
    private static final String PREF_FILE_NAME = "baking_time.prefs";
    private static final String PREF_KEY_RANDOM_RECIPE = "pref_random_recipe";
    private static final String RECIPE_LIST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/" +
            "2017/May/59121517_baking/baking.json";

    /**
     * Gets the recipe list from the server, MUST be called from background thread
     * @return list containing the recipes, or null if none found or a problem happened
     */
    public static ArrayList<Recipe> requestRecipeList() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(RECIPE_LIST_URL)
                .build();
        
        String recipeListJson;
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if (response.code() == HttpURLConnection.HTTP_OK && responseBody != null) {
                recipeListJson = responseBody.string();
            } else {
                throw new IOException("Got error response code from server.");
            }
        } catch (IOException e) {
            recipeListJson = null;
            Log.w(TAG, "An error occurred when trying to request data.", e);
        }

        ArrayList<Recipe> recipeList = null;
        if (recipeListJson != null) {
            Gson gson = new Gson();
            recipeList = gson.fromJson(recipeListJson,
                    (new TypeToken<ArrayList<Recipe>>(){}).getType());
        }

        return recipeList;
    }

    public static void storeRandomRecipeToPref(Context context, Recipe recipe) {
        if (recipe == null) return;
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_KEY_RANDOM_RECIPE, recipe.toString());
        editor.apply();
    }

    public static Recipe getRandomRecipeFromPref(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        String json = sharedPref.getString(PREF_KEY_RANDOM_RECIPE, null);

        if (json == null) return null;

        Gson gson = new Gson();
        return gson.fromJson(json, Recipe.class);
    }
}
