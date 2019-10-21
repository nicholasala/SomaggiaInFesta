package com.example.somaggiainfesta;

import android.Manifest;
import android.content.Context;
import android.net.wifi.WifiManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Rule;

import androidx.annotation.RequiresPermission;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CashDeskTest {
    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.CHANGE_WIFI_STATE);

    @Test
    public void pressCashDButton(){
        //turn off the wifi and check if infoText is displayed with NETERR string
        Context appContext = InstrumentationRegistry.getTargetContext();
        WifiManager wifi=(WifiManager) appContext.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);

        onView(withId(R.id.cassa)).perform(click());

        //check that infoText is dysplayed with the net error text
        onView(withId(R.id.waitingText)).check(matches(isDisplayed()));

        //wait until text of label is updated through retry button state
        //TODO sistemare attraverso https://developer.android.com/reference/androidx/test/espresso/IdlingResource, dato che bisognerebbe far aspettare che il testo sia pronto

        onView(withId(R.id.waitingText)).check(matches(withText(appContext.getResources().getString(R.string.network_error))));

        //turn on wifi
        wifi.setWifiEnabled(true);
    }
}
