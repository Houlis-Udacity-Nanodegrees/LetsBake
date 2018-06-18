package com.xaris.xoulis.letsbake.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

import static com.xaris.xoulis.letsbake.widget.WidgetConstants.ACTION_UPDATE_WIDGET;
import static com.xaris.xoulis.letsbake.widget.WidgetConstants.APP_WIDGET_ID_EXTRA;
import static com.xaris.xoulis.letsbake.widget.WidgetConstants.RECIPE_ID_EXTRA;

public class RecipeWidgetService extends IntentService {

    @Inject
    RecipesRepository recipesRepository;

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(1, createNotification());
        AndroidInjection.inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && ACTION_UPDATE_WIDGET.equals(intent.getAction())) {
            int recipeId = intent.getIntExtra(RECIPE_ID_EXTRA, -1);
            int appWidgetId = intent.getIntExtra(APP_WIDGET_ID_EXTRA, AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
                return;

            LiveData<Recipe> recipeLiveData = recipesRepository.getRecipe(recipeId);
            recipeLiveData.observeForever(new Observer<Recipe>() {
                @Override
                public void onChanged(@Nullable Recipe recipe) {
                    if (recipe != null) {
                        recipeLiveData.removeObserver(this);
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(RecipeWidgetService.this);
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(RecipeWidgetService.this, RecipeWidgetProvider.class));
                        //Trigger data update to handle the GridView widgets and force a data refresh
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list);
                        //Now update all widgets
                        RecipeWidgetProvider.updateRecipeWidgets(RecipeWidgetService.this, appWidgetManager, recipe, appWidgetIds);
                    }
                }
            });
        }
    }

    public static void startActionUpdateWidget(Context context, int recipeId, int appWidgetId) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra(APP_WIDGET_ID_EXTRA, appWidgetId);
        intent.putExtra(RECIPE_ID_EXTRA, recipeId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    private Notification createNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setContentTitle("yy")
                .setContentText("nn")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return mBuilder.build();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notif_channel_name";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
