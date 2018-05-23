package com.xaris.xoulis.letsbake.view.ui.steps;

import android.arch.lifecycle.ViewModel;

import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import javax.inject.Inject;

public class StepsViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    @Inject
    public StepsViewModel(RecipesRepository recipesRepository) {
        mRepository = recipesRepository;
    }
}
