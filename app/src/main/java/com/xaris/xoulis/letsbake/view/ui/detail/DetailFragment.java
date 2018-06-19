package com.xaris.xoulis.letsbake.view.ui.detail;

import android.animation.LayoutTransition;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.data.model.Step;
import com.xaris.xoulis.letsbake.databinding.FragmentDetailBinding;
import com.xaris.xoulis.letsbake.di.Injectable;
import com.xaris.xoulis.letsbake.view.adapter.IngredientsAdapter;
import com.xaris.xoulis.letsbake.view.adapter.StepsAdapter;
import com.xaris.xoulis.letsbake.view.ui.steps.StepsFragment;

import javax.inject.Inject;

public class DetailFragment extends Fragment implements Injectable, StepsAdapter.StepClickListener {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private DetailViewModel detailViewModel;

    private FragmentDetailBinding binding;

    private RecyclerView ingredientsRecyclerView;

    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;

    public DetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        binding.setLifecycleOwner(this);
        binding.setHandler(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        // TODO decide what to do if arguments == null
        Recipe recipe = null;
        if (arguments != null)
            recipe = arguments.getParcelable("recipe");

        initToolbar();
        initIngredientsRecyclerView();
        initStepsAdapter();
        observeViewModel(recipe);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (getResources().getBoolean(R.bool.is_tablet)) {
            actionBar.hide();
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            setHasOptionsMenu(true);
        }
    }

    private void initIngredientsRecyclerView() {
        ingredientsRecyclerView = binding.ingredientsRecyclerView;
        ingredientsAdapter = new IngredientsAdapter();
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Enable animation for recyclerView's parent's layout changes
        LayoutTransition transition = binding.contentCl.getLayoutTransition();
        transition.enableTransitionType(LayoutTransition.CHANGING);
    }

    private void initStepsAdapter() {
        RecyclerView stepsRecyclerView = binding.recipeStepsRecyclerView;
        stepsAdapter = new StepsAdapter(this, getResources().getBoolean(R.bool.is_tablet));
        stepsRecyclerView.setAdapter(stepsAdapter);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(stepsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        stepsRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void observeViewModel(Recipe recipe) {
        detailViewModel = ViewModelProviders.of(this, mViewModelFactory).get(DetailViewModel.class);
        detailViewModel.setRecipe(recipe);
        detailViewModel.getRecipe().observe(this, newRecipe -> {
            if (recipe != null) {
                binding.setViewModel(detailViewModel);
                binding.collapsingToolbar.setTitle(newRecipe.getName());
                ingredientsAdapter.setIngredientList(newRecipe.getIngredients());
                stepsAdapter.setSteps(newRecipe.getSteps());
            }
        });
    }

    public void showOrHideIngredients(View view) {
        if (detailViewModel.getShowIngredients().getValue() == null)
            return;
        boolean shouldShow = detailViewModel.getShowIngredients().getValue();
        float deg;
        if (shouldShow) {
            deg = 0f;
        } else {
            deg = 180f;
        }
        binding.showOrHideIngredientsImageView.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
        detailViewModel.setShowIngredients(!shouldShow);
    }

    @Override
    public void onStepClick(Step step) {
        int containerViewId;
        StepsFragment fragment = new StepsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putInt("stepId", step.getId());
        args.putParcelable("recipe", detailViewModel.getRecipe().getValue());
        fragment.setArguments(args);

        if (getResources().getBoolean(R.bool.is_tablet)) {
            containerViewId = R.id.recipe_step_container;
        } else {
            containerViewId = R.id.container;
            transaction.setCustomAnimations(R.animator.slide_in_up, 0, 0, R.animator.slide_out_down);
        }

        transaction.add(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }
}
