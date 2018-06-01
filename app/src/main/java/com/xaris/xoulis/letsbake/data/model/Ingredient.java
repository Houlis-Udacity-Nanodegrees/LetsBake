package com.xaris.xoulis.letsbake.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Locale;

@Entity(indices = {@Index("recipe_id")},
        foreignKeys = @ForeignKey(entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id"))
public class Ingredient {

    @PrimaryKey
    @ColumnInfo(name = "recipe_id")
    private transient int recipeId;

    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredient(int recipeId, int quantity, String measure, String ingredient) {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    @Ignore
    public Ingredient(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getFullIngredientDescription(Ingredient ingredient) {
        double quantity = ingredient.quantity;
        String formattedQuantity = quantity - Math.floor(quantity) > 0 ? String.format(Locale.getDefault(), "%.1f", quantity) :
                String.format(Locale.getDefault(), "%.0f", quantity);
        String fullDescription = formattedQuantity + " " + ingredient.getMeasure().toLowerCase();
        if (ingredient.getQuantity() > 1) {
            fullDescription += "s";
        }
        fullDescription += " " + ingredient.getIngredient();
        return fullDescription;
    }
}
