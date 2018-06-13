package com.xaris.xoulis.letsbake.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Ingredient;
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
        return new ListRemoteViewsFactory(this.getApplicationContext(), recipeId, recipesRepository);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        final Context context;
        final int recipeId;
        List<Ingredient> ingredients;
        final RecipesRepository recipesRepository;

        public ListRemoteViewsFactory(Context context, int recipeId, RecipesRepository recipesRepository) {
            this.context = context;
            this.recipeId = recipeId;
            this.recipesRepository = recipesRepository;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            ingredients = recipesRepository.getRecipe(recipeId).getValue().getIngredients();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredients != null)
                return ingredients.size();
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setTextViewText(R.id.widget_ingredient_name, ingredients.get(position).getIngredient());
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
