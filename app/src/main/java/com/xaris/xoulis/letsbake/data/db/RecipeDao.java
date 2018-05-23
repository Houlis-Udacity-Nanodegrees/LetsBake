package com.xaris.xoulis.letsbake.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.xaris.xoulis.letsbake.data.model.Ingredient;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;

import java.util.List;

@Dao
public abstract class RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRecipes(List<Recipe> recipes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRecipe(Recipe recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSteps(List<Step> steps);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertIngredients(List<Ingredient> ingredients);

    @Query("SELECT * FROM Recipe")
    public abstract LiveData<List<Recipe>> getRecipes();

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    public abstract LiveData<Recipe> getRecipeById(int recipeId);

    @Query("SELECT * FROM Step WHERE recipe_id = :recipeId")
    public abstract LiveData<List<Step>> getSteps(int recipeId);
}
