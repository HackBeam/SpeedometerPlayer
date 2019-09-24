package com.grego.SpeedometerPlayer;

import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;

/**
 * A simple {@link Fragment} subclass for the settings screen.
 */
public class SettingsFragment extends PreferenceFragment
{
    public SettingsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
