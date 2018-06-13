package com.xaris.xoulis.letsbake.widget;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.xaris.xoulis.letsbake.data.model.Ingredient;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import java.util.List;

import javax.inject.Inject;

public class WidgetViewModel extends ViewModel {

    private final RecipesRepository repository;

    private final LiveData<List<Recipe>> recipes;

    @Inject
    public WidgetViewModel(RecipesRepository repository) {
        this.repository = repository;
        recipes = repository.getRecipeList();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public Recipe getRecipeByPosition(int position){
        return recipes.getValue().get(position);
    }

    public List<Ingredient> getIngredientsIdByPosition(int position){
        return recipes.getValue().get(position).getIngredients();
    }
}
