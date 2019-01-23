package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.os.Bundle;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.uni_stuttgart.informatik.sopra.sopraapp.cameraQrCode.RotatingCaptureActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.main.HelpActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.main.MainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.requests.CustomizeRequestActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.wifi.WifiStateActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public WifiStateActivity wifiStateActivity;
    private Bundle bundle;

    @Rule
    public ActivityTestRule<MainActivity> customizeRequestActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void blitzLicht() {
        onView(withId(R.id.flashButton)).perform(click()).perform(click());
    }

    @Test
    public void wifiInfReact() {
        Espresso.pressBack();
        onView(withId(R.id.buttonWifInf)).perform(click());
    }

    @Test
    public void onClickDeviceManager() {
        Espresso.pressBack();
        onView(withId(R.id.device_management)).perform(click());
    }

    @Test
    public void onOptionsItemSelected() {
        Espresso.pressBack();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText(R.string.help)).perform(click());
    }

    @Test
    public void requestManagement() {
        Espresso.pressBack();
        onView(withId(R.id.request_management)).perform(click());
    }
}