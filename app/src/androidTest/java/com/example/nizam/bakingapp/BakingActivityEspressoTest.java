package com.example.nizam.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Created by nizamudeenms on 03/01/18.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BakingActivityEspressoTest {
    @Rule
    public ActivityTestRule<BakingActivity> mActivityRule = new ActivityTestRule<>(BakingActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        System.out.println("mIdlingResource : "+ mIdlingResource.isIdleNow());
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void ensureRecyclerViewExists() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
//        onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollToPosition(1));
//        onView(withText("Brownies")).check(matches(isDisplayed()));

//        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
//        intended(hasExtraWithKey("bakingId"));
    }

    @Test
    public void ensureStepsListExists() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.steps_list_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void ensureIngredientListExists() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.ingredient_list_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void checkPlayerViewIsVisible_RecipeDetailActivity1() {
        onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onData(anything()).inAdapterView(withId(R.id.steps_list_recycler_view)).atPosition(0).perform(click());
        onView(withId(R.id.exo_player_view)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
