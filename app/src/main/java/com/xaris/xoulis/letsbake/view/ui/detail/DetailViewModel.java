package com.xaris.xoulis.letsbake.view.ui.detail;

import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.xaris.xoulis.letsbake.data.model.Step;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import java.util.List;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private final RecipesRepository mRepository;

    private final MutableLiveData<Integer> mRecipeId;
    private final MutableLiveData<Integer> mStepId;
    private final LiveData<List<Step>> mSteps;
    private final MediatorLiveData<Step> mCurrentStep = new MediatorLiveData<>();

    @Inject
    public DetailViewModel(RecipesRepository recipesRepository) {
        this.mRepository = recipesRepository;
        this.mRecipeId = new MutableLiveData<>();
        this.mStepId = new MutableLiveData<>();

        mSteps = Transformations.switchMap(mRecipeId, input -> {
            if (input != null) {
                return mRepository.getSteps(input);
            }
            return null;
        });

        mCurrentStep.addSource(mSteps, input -> {
            if (input != null && !input.isEmpty() && mStepId.getValue() != null) {
                mCurrentStep.setValue(input.get(mStepId.getValue()));
            } else {
                mCurrentStep.setValue(null);
            }
        });

        mCurrentStep.addSource(mStepId, input -> {
            if (input != null && mSteps.getValue() != null && !mSteps.getValue().isEmpty()) {
                mCurrentStep.setValue(mSteps.getValue().get(input));
            } else {
                mCurrentStep.setValue(null);
            }
        });
    }

    public Boolean hasNextStep() {
        return mSteps.getValue() != null && mStepId.getValue() != null
                && mSteps.getValue().size() - 1 > mStepId.getValue();
    }

    public Boolean hasPreviousStep() {
        return mStepId.getValue() != null && 0 < mStepId.getValue();
    }

    public void getNextStep() {
        if (mStepId.getValue() == null)
            return;
        mStepId.setValue(mStepId.getValue() + 1);
    }

    public void getPreviousStep() {
        if (mStepId.getValue() == null)
            return;
        mStepId.setValue(mStepId.getValue() - 1);
    }

    public LiveData<Step> getCurrentStep() {
        return mCurrentStep;
    }

    public void setRecipeId(int recipeId) {
        if (mRecipeId.getValue() == null || mRecipeId.getValue() != recipeId) {
            mRecipeId.setValue(recipeId);
        }
    }

    public void setStepId(int stepId) {
        if (mStepId.getValue() == null || mStepId.getValue() != stepId) {
            mStepId.setValue(stepId);
        }
    }

    public int getRecipeId() {
        return mRecipeId.getValue() == null ? -1 : mRecipeId.getValue();
    }

    public int getStepId() {
        return mStepId.getValue() == null ? -1 : mStepId.getValue();
    }
}
