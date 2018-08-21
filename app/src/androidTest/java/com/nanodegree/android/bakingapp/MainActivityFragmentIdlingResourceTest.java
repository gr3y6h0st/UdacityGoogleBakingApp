package com.nanodegree.android.bakingapp;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityFragmentIdlingResourceTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        //get corresponding fragment
        MainRecipeListFragment fragment = new MainRecipeListFragment();
        //load fragment into MainActivity
        mMainActivityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_main_recipe_list, fragment)
                .commit();
        //get idiling resource from fragment
        mIdlingResource = fragment.getIdlingResource();
        //register fragment
        IdlingRegistry.getInstance().register(mIdlingResource);

    }

    @Test
    public void loadMainRecipeListTv() {
        onView(ViewMatchers.withId(R.id.recipe_main_frag_rv))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText("Nutella")).check(matches(isDisplayed()));

        //onView(withId(R.id.recipe_main_tv))
        //.check(matches(withText(startsWith("N"))));

        //onData(anything()).inAdapterView(withId(R.id.recipe_main_frag_rv)).atPosition(0)
        //.check(matches(withText(startsWith("Nutella"))));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}
