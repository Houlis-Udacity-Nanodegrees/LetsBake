package com.xaris.xoulis.letsbake.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.xaris.xoulis.letsbake.view.ui.detail.DetailViewModel;
import com.xaris.xoulis.letsbake.view.ui.recipes.RecipesViewModel;
import com.xaris.xoulis.letsbake.view.ui.steps.StepsViewModel;
import com.xaris.xoulis.letsbake.viewmodel.ViewModelFactory;
import com.xaris.xoulis.letsbake.widget.WidgetViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WidgetViewModel.class)
    abstract ViewModel bindWidgetViewModel(WidgetViewModel widgetViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipesViewModel.class)
    abstract ViewModel bindRecipesViewModel(RecipesViewModel recipesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StepsViewModel.class)
    abstract ViewModel bindStepsViewModel(StepsViewModel stepsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel.class)
    abstract ViewModel bindDetailViewModel(DetailViewModel detailViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
