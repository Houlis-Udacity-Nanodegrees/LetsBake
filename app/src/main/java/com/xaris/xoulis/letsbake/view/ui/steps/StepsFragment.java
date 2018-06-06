package com.xaris.xoulis.letsbake.view.ui.steps;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;
import com.xaris.xoulis.letsbake.databinding.FragmentStepsBinding;

import com.xaris.xoulis.letsbake.di.Injectable;

import javax.inject.Inject;

public class StepsFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private StepsViewModel stepsViewModel;

    private FragmentStepsBinding binding;

    public StepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            Recipe recipe = args.getParcelable("recipe");
            int stepId = args.getInt("stepId");
            observeViewModel(recipe, stepId);
        }

        binding.nextStepButton.setOnClickListener(v -> {
            if (stepsViewModel.getStepId().getValue() != null)
                stepsViewModel.setStepId(stepsViewModel.getStepId().getValue() + 1);
        });

        binding.previousStepButton.setOnClickListener(v -> {
            if (stepsViewModel.getStepId().getValue() != null)
                stepsViewModel.setStepId(stepsViewModel.getStepId().getValue() - 1);
        });
    }

    private void observeViewModel(Recipe recipe, int stepId) {
        stepsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(StepsViewModel.class);
        stepsViewModel.setRecipe(recipe);
        stepsViewModel.setStepId(stepId);
        stepsViewModel.getStep().observe(this, step -> {
            if (step != null)
                binding.setViewModel(stepsViewModel);
        });
    }
}
