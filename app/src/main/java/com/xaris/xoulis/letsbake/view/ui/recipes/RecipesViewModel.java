package com.xaris.xoulis.letsbake.view.ui.recipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipesViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    private final LiveData<List<Recipe>> recipeList;

    @Inject
    public RecipesViewModel(RecipesRepository recipesRepository) {
        mRepository = recipesRepository;
        recipeList = mRepository.getRecipeList();
    }

    @VisibleForTesting
    public LiveData<List<Recipe>> getRecipes() {
        return recipeList;
    }

    @VisibleForTesting
    @BindingAdapter({"imageUrl", "placeholder"})
    public static void loadRecipeImage(ImageView view, String imageUrl, int imageSrcId) {
        if (TextUtils.isEmpty(imageUrl)) {
            view.setImageResource(imageSrcId);
        } else {
            Picasso.get().load(imageUrl).into(view);
        }
    }
}
