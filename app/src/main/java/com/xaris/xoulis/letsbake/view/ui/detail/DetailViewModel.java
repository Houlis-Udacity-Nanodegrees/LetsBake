package com.xaris.xoulis.letsbake.view.ui.detail;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    @Inject
    public DetailViewModel(RecipesRepository recipesRepository) {
        mRepository = recipesRepository;
    }

}
