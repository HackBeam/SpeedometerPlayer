package com.grego.SpeedometerPlayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.grego.SpeedometerPlayer.R;

/**
 * Activity to display and change the Preferences
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Core.Helpers.SetMaxScreenBrightness(this);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Core.Data.Preferences.LoadPreferences();
    }

}
