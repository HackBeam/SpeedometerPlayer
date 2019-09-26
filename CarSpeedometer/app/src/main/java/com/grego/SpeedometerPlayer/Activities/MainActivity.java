package com.grego.SpeedometerPlayer.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;

import com.grego.SpeedometerPlayer.Compounds.BatteryDisplay;
import com.grego.SpeedometerPlayer.Compounds.LimitDisplay;
import com.grego.SpeedometerPlayer.Compounds.Speedometer.LimitSpeedometerDisplay;
import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.LimitController;
import com.grego.SpeedometerPlayer.R;

/**
 * Initial activity containing all the app Ui elements
 */
public class MainActivity extends AppCompatActivity
{
    //region UI references
    private View activityRoot;
    private BatteryDisplay batteryDisplay;
    private LimitSpeedometerDisplay limitSpeedometerDisplay;
    private LimitDisplay limitDisplay;
    private ImageButton settingsButton;
    private TextClock clock;
    //endregion

    private LimitController limitController;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Core initialization
        Core.Initialize(this);

        // Self initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrieveComponentsReferences();
        InitializeComponents();
        UpdateUI();
    }

    /**
     * Gets all UI components and compounds references from the inflate XML.
     */
    private void RetrieveComponentsReferences()
    {
        activityRoot = findViewById(R.id.main_activity_root);
        batteryDisplay = (BatteryDisplay) findViewById(R.id.battery_display);
        limitSpeedometerDisplay = (LimitSpeedometerDisplay) findViewById(R.id.limit_speedometer);
        limitDisplay = (LimitDisplay) findViewById(R.id.limit_display);
        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        clock = (TextClock) findViewById(R.id.textClock);
    }

    /**
     * Sets default configuration for simple components.
     */
    private void InitializeComponents()
    {
        Core.Helpers.SetMaxScreenBrightness(this);
        clock.setTypeface(Core.Data.DefaultFont);
        limitController = new LimitController();
    }

    /**
     * Start services and components behaviour.
     */
    @Override
    protected void onStart()
    {
        Core.Services.Battery.StartListening();
        Core.Services.Location.StartListening(this);
        Core.Services.Input.StartListening(activityRoot);
        limitController.StartController();

        super.onStart();
    }

    /**
     * Stop services and components behaviour.
     */
    @Override
    protected void onStop()
    {
        Core.Services.Battery.StopListening();
        Core.Services.Location.StopListening();
        Core.Services.Input.StopListening();
        limitController.StopController();

        super.onStop();
    }

    /**
     * Redraw UI on resume.
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        UpdateUI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Core.Services.Location.StartListening(this);
                }
                else
                {
                    //TODO: Maybe show a custom error message?
                }
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    /**
     * Updates all elements in the UI
     */
    public void UpdateUI()
    {
        batteryDisplay.UpdateUI();
        limitSpeedometerDisplay.UpdateUI();
        Core.Services.Limit.ResetLimitIfNeeded();

        if (Core.Data.Preferences.mirrorMode)
        {
            activityRoot.setScaleX(-1f);
        }
        else
        {
            activityRoot.setScaleX(1f);
        }
    }

    /**
     * Called by the settings button.
     * Opens the settings activity.
     * @param view Unused parameter.
     */
    public void LaunchSettingsActivity(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
