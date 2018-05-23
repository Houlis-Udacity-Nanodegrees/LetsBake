package com.xaris.xoulis.letsbake.di;

import com.xaris.xoulis.letsbake.view.ui.detail.DetailFragment;
import com.xaris.xoulis.letsbake.view.ui.recipes.RecipesFragment;
import com.xaris.xoulis.letsbake.view.ui.steps.StepsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract RecipesFragment contributeRecipesFragment();

    @ContributesAndroidInjector
    abstract StepsFragment contributeStepsFragment();

    @ContributesAndroidInjector
    abstract DetailFragment contributeDetailFragment();
}
