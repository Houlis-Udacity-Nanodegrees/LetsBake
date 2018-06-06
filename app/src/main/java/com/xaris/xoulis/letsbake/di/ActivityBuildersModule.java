package com.xaris.xoulis.letsbake.di;

import com.xaris.xoulis.letsbake.view.ui.recipes.RecipesActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract RecipesActivity contributeRecipesActivity();
}
