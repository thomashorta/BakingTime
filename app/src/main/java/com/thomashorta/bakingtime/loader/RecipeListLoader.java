package com.thomashorta.bakingtime.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thomashorta.bakingtime.R;
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
    private static final String TAG = RecipeListLoader.class.getSimpleName();

    public static int LOADER_ID = 217;
    private static final String RECIPE_LIST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/" +
            "2017/May/59121517_baking/baking.json";

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
