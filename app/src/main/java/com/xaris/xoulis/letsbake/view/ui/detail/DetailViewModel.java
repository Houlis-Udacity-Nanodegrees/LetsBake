package com.xaris.xoulis.letsbake.view.ui.detail;

import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.xaris.xoulis.letsbake.data.model.Ingredient;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import java.util.List;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {


    private final RecipesRepository mRepository;

    private final MutableLiveData<Integer> recipeId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showIngredients = new MutableLiveData<>();
    private final LiveData<Recipe> recipe;


    @Inject
    public DetailViewModel(RecipesRepository recipesRepository) {
        this.mRepository = recipesRepository;
        this.recipe = Transformations.switchMap(recipeId, mRepository::getRecipe);
        this.showIngredients.postValue(false);
    }

    public int getRecipeId() {
        return recipeId.getValue() != null ? recipeId.getValue() : -1;
    }

    public void setRecipeId(int id) {
        this.recipeId.postValue(id);
    }

    public LiveData<Boolean> getShowIngredients() {
        return showIngredients;
    }

    public void setShowIngredients(boolean shouldShow) {
        this.showIngredients.postValue(shouldShow);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public List<Ingredient> getIngredients() {
        return recipe.getValue().getIngredients();
    }

    public List<Step> getSteps() {
        return recipe.getValue().getSteps();
    }
}
