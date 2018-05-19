package com.xaris.xoulis.letsbake.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.xaris.xoulis.letsbake.data.UdactiyService;
import com.xaris.xoulis.letsbake.data.db.RecipeDao;
import com.xaris.xoulis.letsbake.data.db.RecipesDatabase;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.utils.AppExecutors;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesRepository {
    private final MediatorLiveData<List<Recipe>> recipeList = new MediatorLiveData<>();

    private AppExecutors appExecutors;
    private UdactiyService udactiyService;
    private RecipesDatabase recipesDatabase;
    private RecipeDao recipeDao;

    public RecipesRepository(AppExecutors appExecutors, UdactiyService udactiyService,
                             RecipesDatabase recipesDatabase, RecipeDao recipeDao) {
        this.appExecutors = appExecutors;
        this.udactiyService = udactiyService;
        this.recipesDatabase = recipesDatabase;
        this.recipeDao = recipeDao;
    }

    public LiveData<List<Recipe>> getRecipeList() {
        final LiveData<List<Recipe>> dbRecipeList = recipeDao.getRecipes();
        recipeList.addSource(dbRecipeList, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes == null || recipes.isEmpty()) {
                    recipeList.removeSource(dbRecipeList);
                    fetchRecipeList();
                } else if (recipes != dbRecipeList.getValue()) {
                    recipeList.setValue(recipes);
                }
            }
        });
    }

    private void fetchRecipeList(LiveData<List<Recipe>> dbRecipeList) {
        recipeList.addSource(dbRecipeList, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {

            }
        });

        udactiyService.getRecipeList().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isEmpty()) {
                    //recipes.setValue(response.body());
                } else {
                    //recipes.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                recipes.setValue(null);
            }
        });
    }
}
