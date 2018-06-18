package com.xaris.xoulis.letsbake.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Ingredient;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class IngredientRemoteViewsService extends RemoteViewsService {

    @Inject
    RecipesRepository recipesRepository;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        AndroidInjection.inject(this);
        int recipeId = Integer.valueOf(intent.getData().getSchemeSpecificPart());
        return new ListRemoteViewsFactory(getApplicationContext(), recipeId, recipesRepository);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        final Context mContext;
        final int mRecipeId;
        List<Ingredient> mIngredients;
        final RecipesRepository mRecipesRepository;

        public ListRemoteViewsFactory(Context context, int recipeId, RecipesRepository recipesRepository) {
            this.mContext = context;
            this.mRecipeId = recipeId;
            this.mRecipesRepository = recipesRepository;
        }

        @Override
        public void onCreate() {
            LiveData<Recipe> recipeLiveData = recipesRepository.getRecipe(1);
            recipeLiveData.observeForever(new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable Recipe recipe) {
                    if (recipe != null) {
                        mIngredients = recipe.getIngredients();
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, RecipeWidgetProvider.class));
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list);
                        RecipeWidgetProvider.updateRecipeWidgets(mContext, appWidgetManager, recipe, appWidgetIds);
                        //recipeLiveData.removeObserver(this);
                    }
                }
            });
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredients != null)
                return mIngredients.size();
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (mIngredients == null || mIngredients.isEmpty())
                return null;
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_provider);
            views.setTextViewText(R.id.widget_ingredient_name, mIngredients.get(position).getIngredient());
            views.setOnClickFillInIntent(R.id.widget_ingredient_name, new Intent());
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
