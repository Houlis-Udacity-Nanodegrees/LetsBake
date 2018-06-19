package com.xaris.xoulis.letsbake.view.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.xaris.xoulis.letsbake.data.model.Ingredient;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;

import java.util.List;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private final MutableLiveData<Boolean> showIngredients = new MutableLiveData<>();
    private final MutableLiveData<Recipe> recipe = new MutableLiveData<>();

    @Inject
    public DetailViewModel() {
        this.showIngredients.postValue(false);
    }

    public void setRecipe(Recipe recipe) {
        this.recipe.setValue(recipe);
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
