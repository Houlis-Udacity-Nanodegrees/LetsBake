package com.xaris.xoulis.letsbake.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Ingredient;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.databinding.ActivityWidgetBinding;
import com.xaris.xoulis.letsbake.di.Injectable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class IngredientsWidgetActivity extends AppCompatActivity implements Injectable, View.OnClickListener, AdapterView.OnItemSelectedListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    WidgetViewModel widgetViewModel;

    ActivityWidgetBinding binding;

    ArrayAdapter<String> mSpinnerAdapter;
    ArrayAdapter<String> mRecipeIngredientsAdapter;
    List<String> mRecipesNames;
    List<String> mRecipeIngredients;

    SharedPreferences prefs;

    int mAppWidgetId;

    private static final String PREF_WIDGET_PREFIX = "widget_pref_id_";

    public IngredientsWidgetActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_widget);

        AndroidInjection.inject(this);
        setResult(RESULT_CANCELED);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setupUi();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        widgetViewModel = ViewModelProviders.of(this, viewModelFactory).get(WidgetViewModel.class);
        observeViewModel();
    }

    private void setupUi() {
        Spinner RecipesSpinner = binding.widgetSpinnerRecipes;

        mRecipesNames = new ArrayList<>();
        mRecipeIngredients = new ArrayList<>();
        mSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mRecipesNames);
        mRecipeIngredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mRecipeIngredients);

        RecipesSpinner.setAdapter(mSpinnerAdapter);
        RecipesSpinner.setOnItemSelectedListener(this);
        binding.addRecipeWidget.setOnClickListener(this);
        binding.activityWidgetIngredientsList.setAdapter(mRecipeIngredientsAdapter);
    }

    private void observeViewModel() {
        widgetViewModel.getRecipes().observe(this, newRecipes -> {
            if (newRecipes != null && !newRecipes.isEmpty()) {
                int recipeId = 1;
                if (loadRecipeId(this, mAppWidgetId) != -1)
                    recipeId = loadRecipeId(this, mAppWidgetId);

                mRecipesNames.clear();
                mRecipeIngredients.clear();

                for (int i = 0; i < newRecipes.size(); i++) {
                    Recipe recipe = newRecipes.get(i);
                    mRecipesNames.add(recipe.getName());
                }
                updateRecipeIngredientsList(newRecipes.get(recipeId));
                mSpinnerAdapter.notifyDataSetChanged();
            }
        });

        widgetViewModel.getSelectedRecipe().observe(this, newSelectedRecipe -> {
            if (newSelectedRecipe != null) {
                updateRecipeIngredientsList(newSelectedRecipe);
            }
        });
    }

    private void updateRecipeIngredientsList(Recipe recipe) {
        if (recipe != null) {
            mRecipeIngredients.clear();
            for (Ingredient ingredient : recipe.getIngredients()) {
                mRecipeIngredients.add(ingredient.getIngredient());
            }
            mRecipeIngredientsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_recipe_widget) {
            Recipe recipe = widgetViewModel.getRecipeByPosition(binding.widgetSpinnerRecipes.getSelectedItemPosition());
            createWidget(this, recipe);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Recipe recipe = widgetViewModel.getRecipeByPosition(binding.widgetSpinnerRecipes.getSelectedItemPosition());
        widgetViewModel.setSelectedRecipe(recipe);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void createWidget(Context context, Recipe recipe) {
        // Store the string locally
        saveRecipeId(mAppWidgetId, recipe.getId());

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidgets(context, appWidgetManager, recipe, appWidgetIds);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    // Write the prefix to the SharedPreferences object for this widget
    private void saveRecipeId(int appWidgetId, int recipeId) {
        SharedPreferences.Editor e = prefs.edit();
        e.putInt(PREF_WIDGET_PREFIX + appWidgetId, recipeId);
        e.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static int loadRecipeId(Context context, int appWidgetId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(PREF_WIDGET_PREFIX + appWidgetId, -1);
    }

    public static void deleteRecipeIdPref(Context context, int appWidgetId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.remove(PREF_WIDGET_PREFIX + appWidgetId);
        e.apply();
    }
}
