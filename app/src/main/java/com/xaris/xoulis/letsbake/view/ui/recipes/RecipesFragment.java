package com.xaris.xoulis.letsbake.view.ui.recipes;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.databinding.FragmentRecipesBinding;
import com.xaris.xoulis.letsbake.di.Injectable;
import com.xaris.xoulis.letsbake.view.adapter.RecipesAdapter;
import com.xaris.xoulis.letsbake.view.ui.detail.DetailFragment;
import com.xaris.xoulis.letsbake.view.ui.steps.StepsFragment;
import com.xaris.xoulis.letsbake.widget.WidgetConstants;

import javax.inject.Inject;

public class RecipesFragment extends Fragment implements Injectable, RecipesAdapter.RecipeClickCallback {

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private FragmentRecipesBinding binding;

    private RecipesViewModel recipesViewModel;
    private RecipesAdapter recipesAdapter;
    private Parcelable recyclerViewPosition;

    private final String RECYCLERVIEW_STATE_KEY = "recyclerview_state_key";

    public RecipesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipes, container, false);
        }
        binding.setLoading(true);
        binding.setShowError(false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recipesViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RecipesViewModel.class);

        initRecyclerView();
        if (savedInstanceState != null) {
            recyclerViewPosition = savedInstanceState.getParcelable(RECYCLERVIEW_STATE_KEY);
        }

        Bundle args = getArguments();
        boolean displayDetailsFragment = false;
        if (args != null && args.containsKey(WidgetConstants.RECIPE_ID_EXTRA)) {
            displayDetailsFragment = true;
            args.remove(WidgetConstants.RECIPE_ID_EXTRA);
        }

        observeViewModel(displayDetailsFragment);
    }

    private void initRecyclerView() {
        recipesAdapter = new RecipesAdapter(this);
        binding.recipesRecyclerView.setAdapter(recipesAdapter);
        binding.recipesRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager;
        boolean isLand = getResources().getBoolean(R.bool.is_landscape);
        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (isTablet || isLand)
            layoutManager = new GridLayoutManager(getContext(), 3);
        else
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recipesRecyclerView.setLayoutManager(layoutManager);
    }

    private void observeViewModel(boolean showDetailsFragment) {
        recipesViewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null && !recipes.isEmpty()) {
                if (showDetailsFragment) {
                    Recipe recipe = recipes.get(getArguments().getInt(WidgetConstants.RECIPE_ID_EXTRA));
                    showDetailsFragment(recipe);
                } else {
                    binding.setShowError(false);
                    binding.setLoading(false);
                    recipesAdapter.setRecipes(recipes);
                    binding.recipesRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewPosition);
                }
            } else {
                // Display error after a while
                new Handler().postDelayed(() -> {
                    if (recipesAdapter.getItemCount() == 0) {
                        binding.setLoading(false);
                        binding.setShowError(true);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLERVIEW_STATE_KEY, binding.recipesRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        showDetailsFragment(recipe);
    }

    private void showDetailsFragment(Recipe recipe) {
        FragmentManager fragmentManager = getFragmentManager();
        Bundle arguments = new Bundle();
        arguments.putParcelable("recipe", recipe);

        // Default fragment
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(arguments);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right)
                .replace(R.id.container, detailFragment)
                .addToBackStack(null)
                .commit();

        // Steps fragment
        if (getResources().getBoolean(R.bool.is_tablet)) {
            StepsFragment stepsFragment = new StepsFragment();
            stepsFragment.setArguments(arguments);
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_container, stepsFragment)
                    .addToBackStack(null)
                    .commit();
            getActivity().findViewById(R.id.recipe_step_container).setVisibility(View.VISIBLE);
        }
    }
}
