package com.thomashorta.bakingtime.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thomashorta.bakingtime.R;
import com.thomashorta.bakingtime.RecipeUtil;
import com.thomashorta.bakingtime.model.Recipe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RecipeListLoader extends AsyncTaskLoader<ArrayList<Recipe>> {
    public static int LOADER_ID = 217;

    private ArrayList<Recipe> mCached;
    private CountingIdlingResource mIdlingResource;

    public RecipeListLoader(Context context, @Nullable CountingIdlingResource idlingResource) {
        super(context);
        mIdlingResource = idlingResource;
    }

    @Override
    protected void onStartLoading() {
        if (mIdlingResource != null) {
            mIdlingResource.increment();
            deliverResult(mockRecipe());
        } else if (mCached != null) {
            deliverResult(mCached);
        } else {
            forceLoad();
        }
    }

    @Override
    public ArrayList<Recipe> loadInBackground() {
        return RecipeUtil.requestRecipeList();
    }

    @Override
    public void deliverResult(ArrayList<Recipe> data) {
        mCached = data;
        super.deliverResult(data);
        if (mIdlingResource != null) mIdlingResource.decrement();
    }

    private ArrayList<Recipe> mockRecipe() {
        Scanner scanner = new Scanner(getContext().getResources().openRawResource(R.raw.mock))
                .useDelimiter("\\A");
        ArrayList<Recipe> recipeList = new ArrayList<>();
        String recipeListJson = scanner.next();
        if (recipeListJson != null) {
            Gson gson = new Gson();
            recipeList = gson.fromJson(recipeListJson,
                    (new TypeToken<ArrayList<Recipe>>(){}).getType());
        }
        return recipeList;
    }
}
