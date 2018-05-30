package com.xaris.xoulis.letsbake.view.ui.recipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

public class RecipesViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    private final LiveData<List<Recipe>> recipeList;

    @Inject
    public RecipesViewModel(RecipesRepository recipesRepository) {
        mRepository = recipesRepository;
        recipeList = mRepository.getRecipeList();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipeList;
    }

    @BindingAdapter({"imageUrl", "placeholder"})
    public static void loadRecipeImage(ImageView view, String imageUrl, int imageSrcId) {
        if (TextUtils.isEmpty(imageUrl)) {
            view.setImageResource(imageSrcId);
        } else {
            Picasso.get().load(imageUrl).into(view);
        }
    }
}
