package com.xaris.xoulis.letsbake.di;

import com.xaris.xoulis.letsbake.view.ui.detail.DetailActivity;
import com.xaris.xoulis.letsbake.view.ui.recipes.RecipesActivity;
import com.xaris.xoulis.letsbake.view.ui.steps.StepsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract RecipesActivity contributeRecipesActivity();

    @ContributesAndroidInjector
    abstract StepsActivity contributeStepsActivity();

    @ContributesAndroidInjector
    abstract DetailActivity contributeDetailActivity();
}
