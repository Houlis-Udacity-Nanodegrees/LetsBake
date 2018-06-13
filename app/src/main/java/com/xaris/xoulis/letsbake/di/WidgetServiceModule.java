package com.xaris.xoulis.letsbake.di;

import com.xaris.xoulis.letsbake.widget.IngredientRemoteViewsService;
import com.xaris.xoulis.letsbake.widget.RecipeWidgetService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class WidgetServiceModule {

    @ContributesAndroidInjector
    abstract RecipeWidgetService contributeRecipeWidgetService();

    @ContributesAndroidInjector
    abstract IngredientRemoteViewsService contributeIngredientRemoteViewsService();
}
