package com.xaris.xoulis.letsbake.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xaris.xoulis.letsbake.data.api.ApiResponse;
import com.xaris.xoulis.letsbake.data.api.UdactiyService;
import com.xaris.xoulis.letsbake.data.db.RecipeDao;
import com.xaris.xoulis.letsbake.data.db.RecipesDatabase;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;
import com.xaris.xoulis.letsbake.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecipesRepository {
    private final MediatorLiveData<List<Recipe>> recipeList = new MediatorLiveData<>();

    private AppExecutors appExecutors;
    private UdactiyService udactiyService;
    private RecipesDatabase recipesDatabase;
    private RecipeDao recipeDao;

    @Inject
    public RecipesRepository(AppExecutors appExecutors, UdactiyService udactiyService,
                             RecipesDatabase recipesDatabase, RecipeDao recipeDao) {
        this.appExecutors = appExecutors;
        this.udactiyService = udactiyService;
        this.recipesDatabase = recipesDatabase;
        this.recipeDao = recipeDao;
    }

    public LiveData<List<Recipe>> getRecipeList() {
        return new NetworkBoundResource<List<Recipe>, List<Recipe>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Recipe> item) {
                recipeDao.insertRecipes(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.getRecipes();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Recipe>>> createCall() {
                return udactiyService.getRecipeList();
            }
        }.asLiveData();
    }

    public LiveData<Recipe> getRecipe(int recipeId) {
        return recipeDao.getRecipeById(recipeId);
    }

    public LiveData<List<Step>> getSteps(int recipeId) {
        return recipeDao.getSteps(recipeId);
    }
}
