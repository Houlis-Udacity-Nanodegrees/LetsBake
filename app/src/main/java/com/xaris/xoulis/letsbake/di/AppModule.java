package com.xaris.xoulis.letsbake.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.xaris.xoulis.letsbake.data.api.UdactiyService;
import com.xaris.xoulis.letsbake.data.db.RecipeDao;
import com.xaris.xoulis.letsbake.data.db.RecipesDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton
    @Provides
    UdactiyService provideUdacityService() {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UdactiyService.class);
    }

    @Singleton
    @Provides
    RecipesDatabase provideDb(Application app) {
        return Room.databaseBuilder(app, RecipesDatabase.class, "recipes.db").build();
    }

    @Singleton
    @Provides
    RecipeDao provideRecipeDao(RecipesDatabase db) {
        return db.recipeDao();
    }
}
