package com.xaris.xoulis.letsbake.view.ui.recipes;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xaris.xoulis.letsbake.R;
import com.xaris.xoulis.letsbake.widget.WidgetConstants;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class RecipesActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        boolean isLand = getResources().getBoolean(R.bool.is_landscape);

        if (isLand && !isTablet) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

        Bundle args = getIntent().getExtras();
        if (savedInstanceState == null) {
            RecipesFragment fragment = new RecipesFragment();
            if (args != null && args.containsKey(WidgetConstants.RECIPE_ID_EXTRA))
                fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count != 0 && isTablet) {
            findViewById(R.id.recipe_step_container).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate(null, POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }
}
