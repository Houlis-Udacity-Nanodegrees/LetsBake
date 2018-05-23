package com.xaris.xoulis.letsbake.view.ui.recipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipesViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    @Inject
    public RecipesViewModel(RecipesRepository recipesRepository) {
        mRepository = recipesRepository;
    }

}
