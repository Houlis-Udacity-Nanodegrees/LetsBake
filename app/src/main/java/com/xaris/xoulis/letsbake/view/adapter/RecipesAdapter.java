package com.xaris.xoulis.letsbake.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.databinding.RecipeItemBinding;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private List<Recipe> recipes;

    private RecipeClickCallback callback;

    public RecipesAdapter(RecipeClickCallback callback) {
        this.callback = callback;
    }

    public void setRecipes(List<Recipe> newRecipeList) {
        this.recipes = newRecipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecipeItemBinding recipeItemBinding = RecipeItemBinding.inflate(inflater, parent, false);
        return new RecipesAdapterViewHolder(recipeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapterViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        if (recipes != null)
            return recipes.size();
        return 0;
    }

    class RecipesAdapterViewHolder extends RecyclerView.ViewHolder {
        private final RecipeItemBinding recipeItemBinding;

        RecipesAdapterViewHolder(RecipeItemBinding recipeItemBinding) {
            super(recipeItemBinding.getRoot());
            this.recipeItemBinding = recipeItemBinding;

            recipeItemBinding.getRoot().setOnClickListener(v -> {
                Recipe recipe = recipeItemBinding.getRecipe();
                if (recipe != null && callback != null) {
                    callback.onRecipeClick(recipe);
                }
            });
        }

        void bind(Recipe recipe) {
            recipeItemBinding.setRecipe(recipe);
            recipeItemBinding.executePendingBindings();
        }
    }

    public interface RecipeClickCallback {
        void onRecipeClick(Recipe recipe);
    }
}
