package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.os.Bundle;

/**
 * @author Baran Demir(3310700)
 * Creates the PreferenceFragment.
 */
public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}