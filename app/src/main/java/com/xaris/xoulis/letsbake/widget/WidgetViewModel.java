package com.xaris.xoulis.letsbake.widget;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.xaris.xoulis.letsbake.data.model.Ingredient;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import java.util.List;

import javax.inject.Inject;

public class WidgetViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    private final LiveData<List<Recipe>> mRecipes;
    private final MutableLiveData<Recipe> mSelectedRecipe = new MutableLiveData<>();

    @Inject
    public WidgetViewModel(RecipesRepository repository) {
        this.mRepository = repository;
        mRecipes = mRepository.getRecipeList();
    }

    public MutableLiveData<Recipe> getSelectedRecipe() {
        return mSelectedRecipe;
    }

    public void setSelectedRecipe(Recipe recipe) {
        int selectedRecipeId = recipe.getId();
        if (mSelectedRecipe.getValue() == null || mSelectedRecipe.getValue().getId() != selectedRecipeId)
            this.mSelectedRecipe.setValue(recipe);
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    public Recipe getRecipeByPosition(int position) {
        return mRecipes.getValue().get(position);
    }
}
