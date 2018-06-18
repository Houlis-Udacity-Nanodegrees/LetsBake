package com.xaris.xoulis.letsbake.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.view.ui.recipes.RecipesActivity;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_list_widget);
        if (recipe != null) {
            views = handleNonNullRecipe(views, context, recipe, appWidgetId);
        } else {
            views = handleNullRecipe(views, context);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews handleNonNullRecipe(RemoteViews views, Context context, @NonNull Recipe recipe, int appWidgetId) {
        views.setViewVisibility(R.id.widget_title, View.VISIBLE);
        views.setViewVisibility(R.id.widget_ingredients_list, View.VISIBLE);
        views.setViewVisibility(R.id.widget_error_layout, View.GONE);

        // Set the intent for the IngredientRemoteViewsService
        Intent serviceIntent = new Intent(context, IngredientRemoteViewsService.class);
        serviceIntent.setData(Uri.fromParts("content", String.valueOf(recipe.getId()), null));
        views.setRemoteAdapter(R.id.widget_ingredients_list, serviceIntent);

        // Set the main activity intent to launch when item is clicked
        Intent appIntent = new Intent(context, RecipesActivity.class);
        appIntent.putExtra(WidgetConstants.RECIPE_ID_EXTRA, recipe.getId());
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, appWidgetId, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_ingredients_list, appPendingIntent);
        views.setTextViewText(R.id.widget_title, recipe.getName());
        views.setOnClickPendingIntent(R.id.widget_title, appPendingIntent);
        return views;
    }

    private static RemoteViews handleNullRecipe(RemoteViews views, Context context) {
        views.setViewVisibility(R.id.widget_title, View.GONE);
        views.setViewVisibility(R.id.widget_ingredients_list, View.GONE);
        views.setViewVisibility(R.id.widget_error_layout, View.VISIBLE);

        // Set the intent for the RecipeWidgetService, if retry is clicked
        Intent serviceIntent = new Intent(context, RecipeWidgetService.class);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pendingIntent = PendingIntent.getForegroundService(context, 1, serviceIntent, 0);
        } else {
            pendingIntent = PendingIntent.getService(context, 1, serviceIntent, 0);
        }
        views.setOnClickPendingIntent(R.id.widget_retry_button, pendingIntent);

        // Set the intent for the Main Activity, if can't load recipe at all
        Intent intentMainActivity = new Intent(context, RecipesActivity.class);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 1, intentMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_error_layout, activityPendingIntent);
        return views;
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            int recipeId = IngredientsWidgetActivity.loadRecipeId(context, appWidgetId);
            RecipeWidgetService.startActionUpdateWidget(context, recipeId, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetActivity.deleteRecipeIdPref(context, appWidgetId);
        }

    }
}
