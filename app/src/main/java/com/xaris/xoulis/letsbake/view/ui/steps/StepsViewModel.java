package com.xaris.xoulis.letsbake.view.ui.steps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;
import com.xaris.xoulis.letsbake.data.repository.RecipesRepository;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

public class StepsViewModel extends ViewModel {

    private final MutableLiveData<Recipe> recipe = new MutableLiveData<>();
    private final MutableLiveData<Integer> stepId = new MutableLiveData<>();

    private final MediatorLiveData<Step> step = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> hasNextStep = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> hasPreviousStep = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> hasUrl = new MediatorLiveData<>();
    private final MediatorLiveData<List<Step>> steps = new MediatorLiveData<>();

    @Inject
    public StepsViewModel() {
        steps.addSource(recipe, newRecipe -> {
            if (newRecipe != null)
                steps.setValue(newRecipe.getSteps());
        });

        step.addSource(steps, input -> {
            if (input != null && !input.isEmpty() && stepId.getValue() != null) {
                step.setValue(input.get(stepId.getValue()));
            } else {
                step.setValue(null);
            }
        });

        step.addSource(stepId, input -> {
            if (input != null && steps.getValue() != null && !steps.getValue().isEmpty() &&
                    input < steps.getValue().size() && input >= 0) {
                step.setValue(steps.getValue().get(input));
            } else {
                step.setValue(null);
            }
        });

        hasNextStep.addSource(stepId, newStepId -> {
            if (newStepId != null && steps.getValue() != null && newStepId < steps.getValue().size() - 1)
                hasNextStep.setValue(true);
            else
                hasNextStep.setValue(false);
        });

        hasPreviousStep.addSource(stepId, newStepId -> {
            if (newStepId != null && newStepId > 0)
                hasPreviousStep.setValue(true);
            else
                hasPreviousStep.setValue(false);
        });

        hasUrl.addSource(step, newStep -> {
            if (newStep != null) {
                if (!TextUtils.isEmpty(newStep.getVideoURL()))
                    hasUrl.setValue(true);
                else
                    hasUrl.setValue(false);
            }
        });
    }

    public String getVideoUrl() {
        Step step = this.step.getValue();
        String url = "";
        if (step != null) {
            if (!TextUtils.isEmpty(step.getVideoURL()))
                url = step.getVideoURL();
            else if (!TextUtils.isEmpty(step.getThumbnailURL()))
                url = step.getThumbnailURL();
        }
        return url;
    }

    public MutableLiveData<Recipe> getRecipe() {
        return recipe;
    }

    public MutableLiveData<Integer> getStepId() {
        return stepId;
    }

    public MediatorLiveData<Step> getStep() {
        return step;
    }

    public MediatorLiveData<Boolean> getHasNextStep() {
        return hasNextStep;
    }

    public MediatorLiveData<Boolean> getHasPreviousStep() {
        return hasPreviousStep;
    }

    public MediatorLiveData<Boolean> getHasUrl() {
        return hasUrl;
    }

    public void setRecipe(Recipe recipe) {
        if (this.recipe.getValue() == null || this.recipe.getValue().getId() != recipe.getId())
            this.recipe.setValue(recipe);
    }

    public void setStepId(int stepId) {
        if (this.stepId.getValue() == null || this.stepId.getValue() != stepId)
            this.stepId.setValue(stepId);

    }
}
