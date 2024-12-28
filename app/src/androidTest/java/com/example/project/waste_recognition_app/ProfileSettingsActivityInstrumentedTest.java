package com.example.project.waste_recognition_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileSettingsActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<ProfileSettingsActivity> activityRule =
            new ActivityTestRule<>(ProfileSettingsActivity.class);

    @Before
    public void setUp() {
        // Setup shared preferences with test data
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activityRule.getActivity());
        sharedPreferences.edit()
                .putString("username", "JohnDoe")
                .putString("email", "johndoe@example.com")
                .apply();
    }

    @After
    public void tearDown() {
        // Clear shared preferences after tests
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activityRule.getActivity());
        sharedPreferences.edit().clear().apply();
    }

    @Test
    public void testLogoutUser() {
        // Click on the user initial to open the popup menu
        onView(withId(R.id.user_initial)).perform(click());

        // Click on the "Logout" option
        onView(withText("Logout")).perform(click());

        // Check if the LoginActivity is displayed
        onView(withId(R.id.loginCard)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToChangePasswordActivity() {
        // Click on the "Change Password" link
        onView(withId(R.id.change_password_link)).perform(click());

        // Check if the ChangePasswordActivity is displayed
        onView(withId(R.id.user_new_password)).check(matches(isDisplayed()));
    }
}
