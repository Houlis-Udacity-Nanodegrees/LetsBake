package com.xaris.xoulis.letsbake.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.RecipesRepository;

import java.util.List;

public class RecipesViewModel extends ViewModel {
    private final LiveData<List<Recipe>> recipeListObservable;

    public RecipesViewModel(RecipesRepository recipesRepository) {
        this.recipeListObservable = recipesRepository.getProjectList();
    }

    public LiveData<List<Recipe>> getRecipeListObservable() {
        return recipeListObservable;
    }
}
