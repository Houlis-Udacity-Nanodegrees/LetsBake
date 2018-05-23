package com.xaris.xoulis.letsbake.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.xaris.xoulis.letsbake.data.api.UdactiyService;
import com.xaris.xoulis.letsbake.data.db.RecipeDao;
import com.xaris.xoulis.letsbake.data.db.RecipesDatabase;
import com.xaris.xoulis.letsbake.utils.LiveDataCallAdapterFactory;

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
                .baseUrl("http://go.udacity.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
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
