package com.xaris.xoulis.letsbake.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.xaris.xoulis.letsbake.data.model.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipesDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();

}
