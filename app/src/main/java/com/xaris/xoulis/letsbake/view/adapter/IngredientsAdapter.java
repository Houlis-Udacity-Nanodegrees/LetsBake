package com.xaris.xoulis.letsbake.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.xaris.xoulis.letsbake.data.model.Ingredient;
import com.xaris.xoulis.letsbake.databinding.IngredientItemBinding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {
    private List<Ingredient> ingredientList;

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        IngredientItemBinding ingredientBinding = IngredientItemBinding.inflate(inflater, parent, false);
        return new IngredientsAdapterViewHolder(ingredientBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapterViewHolder holder, int position) {
        holder.bind(ingredientList.get(position));
    }

    @Override
    public int getItemCount() {
        if (ingredientList != null)
            return ingredientList.size();
        return 0;
    }

    class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {
        private final IngredientItemBinding ingredientBinding;

        IngredientsAdapterViewHolder(IngredientItemBinding ingredientBinding) {
            super(ingredientBinding.getRoot());
            this.ingredientBinding = ingredientBinding;
        }

        void bind(Ingredient ingredient) {
            ingredientBinding.setIngredient(ingredient);
        }
    }
}
