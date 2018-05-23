package com.xaris.xoulis.letsbake.view.ui.steps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import javax.inject.Inject;

public class StepsViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    private final MutableLiveData<Integer> mRecipeId = new MutableLiveData<>();
    private final LiveData<Recipe> mRecipe;

    private OnStepClickListener mOnStepClickListener;

    @Inject
    public StepsViewModel(RecipesRepository recipesRepository) {
        this.mRepository = recipesRepository;
        this.mRecipe = Transformations.switchMap(mRecipeId, mRepository::getRecipe);
    }

    public void setRecipeId(int id) {
        this.mRecipeId.setValue(id);
    }

    public int getRecipeId() {
        return mRecipeId.getValue() == null ? -1 : mRecipeId.getValue();
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipe;
    }

    public void onStepClicked(Step step) {
        mOnStepClickListener.onStepClick(step);
    }

    public OnStepClickListener getmOnStepClickListener() {
        return mOnStepClickListener;
    }

    public void setmOnStepClickListener(OnStepClickListener mOnStepClickListener) {
        this.mOnStepClickListener = mOnStepClickListener;
    }

    public interface OnStepClickListener {
        void onStepClick(Step step);
    }
}
