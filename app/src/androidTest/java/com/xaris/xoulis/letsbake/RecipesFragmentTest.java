package com.xaris.xoulis.letsbake;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.xaris.xoulis.letsbake.data.model.Recipe;
import com.xaris.xoulis.letsbake.testing.SingleFragmentActivity;
import com.xaris.xoulis.letsbake.utils.RecyclerViewMatcher;
import com.xaris.xoulis.letsbake.utils.TaskExecutorWithIdlingResourceRule;
import com.xaris.xoulis.letsbake.utils.TestUtil;
import com.xaris.xoulis.letsbake.utils.ViewModelUtil;
import com.xaris.xoulis.letsbake.view.ui.recipes.RecipesFragment;
import com.xaris.xoulis.letsbake.view.ui.recipes.RecipesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class RecipesFragmentTest {

    @Rule
    public final ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();

    @Before
    public void init() {

        RecipesFragment recipesFragment = new RecipesFragment();
        RecipesViewModel viewModel = mock(RecipesViewModel.class);

        when(viewModel.getRecipes()).thenReturn(recipes);

        recipesFragment.mViewModelFactory = ViewModelUtil.createFor(viewModel);

        activityRule.getActivity().setFragment(recipesFragment);
    }

    @Test
    public void loadError() {
        recipes.postValue(new ArrayList<>());
        onView(withId(R.id.error_loading_recipes_textView)).check(matches(not(isDisplayed())));
    }

    @Test
    public void loadRecipe() {
        Recipe recipe = TestUtil.createRecipe(0, "foo", null, null, 3, "", R.drawable.recipe_default_1);
        recipes.postValue(Arrays.asList(recipe));

        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("foo"))));
        onView(withId(R.id.error_loading_recipes_textView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recipes_progressBar)).check(matches(not(isDisplayed())));
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.recipes_recyclerView);
    }
}
