package com.xaris.xoulis.letsbake.view.ui.recipes;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.view.adapter.RecipesAdapter;
import com.xaris.xoulis.letsbake.view.ui.detail.DetailFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RecipesActivity extends AppCompatActivity implements HasSupportFragmentInjector, RecipesFragment.OnRecipeClickedListener {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipes_fragment_container, new RecipesFragment())
                    .commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    private void showRecipeDetails(int recipeId) {
        Bundle arguments = new Bundle();
        arguments.putInt("recipeId",recipeId);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right)
                .replace(R.id.recipes_fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRecipeClick(int recipeId) {
        showRecipeDetails(recipeId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
