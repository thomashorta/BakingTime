package com.thomashorta.bakingtime;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thomashorta.bakingtime.adapter.RecipeListAdapter;
import com.thomashorta.bakingtime.loader.RecipeListLoader;
import com.thomashorta.bakingtime.model.Recipe;

import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>,
        RecipeListAdapter.OnRecipeClickListener{
    private static final int GRID_SPAN = 3;

    private RecyclerView mRecipeList;
    private ProgressBar mLoadingIndicator;
    private View mErrorReloadMessage;

    private RecipeListAdapter mRecipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorReloadMessage = findViewById(R.id.error_reload_message);
        mRecipeList = (RecyclerView) findViewById(R.id.rv_recipe_list);

        // check orientation for displaying grid or list
        RecyclerView.LayoutManager lm;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            lm = new LinearLayoutManager(this);
        } else {
            lm = new GridLayoutManager(this, GRID_SPAN);
        }

        mRecipeList.setLayoutManager(lm);
        mRecipeList.setHasFixedSize(true);

        mRecipeListAdapter = new RecipeListAdapter(this);
        mRecipeList.setAdapter(mRecipeListAdapter);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(RecipeListLoader.LOADER_ID, null, this);
    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecipeList.setVisibility(View.INVISIBLE);
        mErrorReloadMessage.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator() {
        mRecipeListAdapter.setRecipeList(null);
        showRecipeList();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showRecipeList() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorReloadMessage.setVisibility(View.INVISIBLE);
        mRecipeList.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        showLoadingIndicator();
        return new RecipeListLoader(RecipeListActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        if (data != null && data.size() > 0) {
            mRecipeListAdapter.setRecipeList(data);
            showRecipeList();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {
        // not implemented
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Toast.makeText(this, recipe.getName() + " clicked!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailFragment.EXTRA_RECIPE, recipe);
        startActivity(intent);
    }
}
