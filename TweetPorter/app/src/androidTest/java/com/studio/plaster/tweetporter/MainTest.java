package com.studio.plaster.tweetporter;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.internal.util.Checks.checkNotNull;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertTrue;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainTest {

    public MainActivity mMainATR;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp(){

    }


    @Test
    public void emptyFirstTest() {
        getStart();
        onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tabLayout),
                                0),
                        0),
                        isDisplayed())).check(matches(isDisplayed()));

        onView(
                allOf(withId(R.id.post_layout),
                        childAtPosition(
                                allOf(withId(R.id.postRecycler),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed())).check(matches(isDisplayed()));

    }

    @Test
    public void addTabHello(){
        getStart();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Add tab"))).perform(click());
        onView(allOf(withId(R.id.setTabName))).perform(click())
        .perform(replaceText("Hello"),closeSoftKeyboard());
        onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3))).perform(scrollTo(), click());

        onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.tabLayout),
                                0),
                        1),
                        isDisplayed())).perform(click());

        assertTrue((mMainATR.getPageAdapter().getPageTitle(mMainATR.getPager().getCurrentItem())).toString().equals("Hello"));

    }

    @Test
    public void deleteTab(){
        getStart();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Add tab"))).perform(click());
        onView(allOf(withId(R.id.setTabName))).perform(click())
                .perform(replaceText("Hello"),closeSoftKeyboard());
        onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3))).perform(scrollTo(), click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Delete tab"))).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(mMainATR.getTabLists().size() == 1);

    }

    @Test
    public void addThailandTopic(){
        getStart();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Add tab"))).perform(click());
        onView(allOf(withId(R.id.setTabName))).perform(click())
                .perform(replaceText("Hello"),closeSoftKeyboard());
        onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3))).perform(scrollTo(), click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Delete tab"))).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Edit tab"))).perform(click());
        onView(
                allOf(withId(R.id.fabAdd),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout_edit),
                                        0),
                                2),
                        isDisplayed())).perform(click());
        onView(
                allOf(withId(R.id.radioKeyword), withText("Keyword"),
                        childAtPosition(
                                allOf(withId(R.id.radioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed())).perform(click());
        onView(
                allOf(withId(R.id.editAddKeyword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                2),
                        isDisplayed())).perform(replaceText("thailand"), closeSoftKeyboard());
        onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3))).perform(scrollTo(), click());
        onView(withId(R.id.editRecycler)).check(matches(atPosition(0, hasDescendant(withText("thailand")))));
    }
    @Test
    public void addPersonTopic(){
        getStart();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Add tab"))).perform(click());
        onView(allOf(withId(R.id.setTabName))).perform(click())
                .perform(replaceText("Hello"),closeSoftKeyboard());
        onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3))).perform(scrollTo(), click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Delete tab"))).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Edit tab"))).perform(click());
        onView(
                allOf(withId(R.id.fabAdd),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout_edit),
                                        0),
                                2),
                        isDisplayed())).perform(click());
        onView(
                allOf(withId(R.id.radioUser), withText("User"),
                        childAtPosition(
                                allOf(withId(R.id.radioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed())).perform(click());
        onView(
                allOf(withId(R.id.editAddKeyword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                2),
                        isDisplayed())).perform(replaceText("js100radio"), closeSoftKeyboard());
        onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3))).perform(scrollTo(), click());
        onView(withId(R.id.editRecycler)).check(matches(atPosition(0, hasDescendant(withText("js100radio")))));
    }

    @Test
    public void completeNewsFeed(){
        getStart();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Edit tab"))).perform(click());
        onView(
                allOf(withId(R.id.fabAdd),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout_edit),
                                        0),
                                2),
                        isDisplayed())).perform(click());
        onView(
                allOf(withId(R.id.radioKeyword), withText("Keyword"),
                        childAtPosition(
                                allOf(withId(R.id.radioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed())).perform(click());
        onView(
                allOf(withId(R.id.editAddKeyword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                2),
                        isDisplayed())).perform(replaceText("thailand"), closeSoftKeyboard());
        onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3))).perform(scrollTo(), click());
        onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar_edit),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed())).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.postRecycler)).check(matches(atPosition(1, hasDescendant(isDisplayed()))));

    }
    @Test
    public void savePostAndView(){
        getStart();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.title), withText("Edit tab"))).perform(click());
        onView(
                allOf(withId(R.id.fabAdd),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.drawer_layout_edit),
                                        0),
                                2),
                        isDisplayed())).perform(click());
        onView(
                allOf(withId(R.id.radioKeyword), withText("Keyword"),
                        childAtPosition(
                                allOf(withId(R.id.radioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed())).perform(click());
        onView(
                allOf(withId(R.id.editAddKeyword),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                2),
                        isDisplayed())).perform(replaceText("thailand"), closeSoftKeyboard());
        onView(
                allOf(withId(android.R.id.button1), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3))).perform(scrollTo(), click());
        onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar_edit),
                                        childAtPosition(
                                                withClassName(is("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed())).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(
                allOf(withId(R.id.postRecycler),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                0)))
                .perform(actionOnItemAtPosition(0, longClick()));
        onView(
                allOf(withId(android.R.id.title), withText("Save this"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.android.internal.view.menu.ListMenuItemView")),
                                        0),
                                0),
                        isDisplayed())).perform(click());
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
        onView(withId(R.id.navigation))
                .perform(NavigationViewActions.navigateTo(R.id.saved_button));
        onView(withId(R.id.postRecycler_save)).check(matches(atPosition(0, hasDescendant(isDisplayed()))));




    }

    public void tabClear(MainActivity act){
        if(act.getTabLists() != null){
            while (act.getTabLists().size() > 1){
                act.deleteTabButton();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getStart(){
        mActivityTestRule.getActivity().byPassLogIn();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mMainATR = (MainActivity)getActivityInstance();
        tabClear(mMainATR);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private Activity getActivityInstance(){
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable(){
            public void run(){
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
