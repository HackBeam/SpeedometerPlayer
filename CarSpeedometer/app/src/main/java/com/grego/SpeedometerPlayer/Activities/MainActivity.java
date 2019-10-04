package com.grego.SpeedometerPlayer.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;

import com.grego.SpeedometerPlayer.Compounds.BatteryDisplay;
import com.grego.SpeedometerPlayer.Compounds.MusicController;
import com.grego.SpeedometerPlayer.Compounds.Speedometer.CruiseSpeedometerDisplay;
import com.grego.SpeedometerPlayer.Compounds.Speedometer.LimitSpeedometerDisplay;
import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;
import com.grego.SpeedometerPlayer.SpeedometerMode;

/**
 * Initial activity containing all the app Ui elements
 */
public class MainActivity extends AppCompatActivity implements IInputListener
{
    //region UI references
    private View activityRoot;
    private BatteryDisplay batteryDisplay;
    private LimitSpeedometerDisplay limitDisplay;
    private CruiseSpeedometerDisplay cruiseDisplay;
    private ImageButton settingsButton;
    private MusicController musicController;
    private TextClock clock;
    //endregion

    private boolean displayingLimiter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Core initialization
        Core.Initialize(this);

        // Self initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrieveComponentsReferences();
        InitializeServices();
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
        limitDisplay = (LimitSpeedometerDisplay) findViewById(R.id.limit_display);
        cruiseDisplay = (CruiseSpeedometerDisplay) findViewById(R.id.cruise_display);
        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        clock = (TextClock) findViewById(R.id.textClock);
        musicController = (MusicController) findViewById(R.id.music_controller);
    }

    /**
     * Sets default configuration for simple components.
     */
    private void InitializeComponents()
    {
        Core.Helpers.SetMaxScreenBrightness(this);
        clock.setTypeface(Core.Data.DefaultFont);
        Core.Services.Input.Subscribe(this);
    }

    /**
     * Start services behaviour.
     */
    protected void InitializeServices()
    {
        Core.Services.Battery.StartListening();
        Core.Services.Location.StartListening(this);
        Core.Services.Input.StartListening(activityRoot);
    }

    /**
     * Stop services behaviour.
     */
    @Override
    protected void onDestroy()
    {
        Core.Services.Battery.StopListening();
        Core.Services.Location.StopListening();
        Core.Services.Input.StopListening();

        super.onDestroy();
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
            case Core.PermissionRequestCodes.LocationServiceStart:
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
        limitDisplay.UpdateUI();
        cruiseDisplay.UpdateUI();
        batteryDisplay.UpdateUI();

        if (Core.Data.Preferences.mirrorMode)
        {
            activityRoot.setScaleX(-1f);
        }
        else
        {
            activityRoot.setScaleX(1f);
        }
    }

    //region IInputListener events

    @Override
    public void OnSwipeUp()
    {
        SwitchSpeedometerMode(SpeedometerMode.CRUISE);
    }

    @Override
    public void OnSwipeDown()
    {
        SwitchSpeedometerMode(SpeedometerMode.CRUISE);
    }

    //region Unused Input Events
    @Override
    public void OnSingleTap()
    {

    }

    @Override
    public void OnDoubleTap()
    {
        SwitchSpeedometerMode(SpeedometerMode.LIMIT);
    }

    @Override
    public void OnLongPress()
    {

    }

    @Override
    public void OnSwipeLeft()
    {

    }

    @Override
    public void OnSwipeRight()
    {

    }
    //endregion

    //endregion

    /**
     * Shows/hides needed views and activates/deactivates needed components
     * to switch between Limiter and Cruise modes.
     */
    public void SwitchSpeedometerMode(SpeedometerMode mode)
    {
        limitDisplay.SetActive(mode == SpeedometerMode.LIMIT);
        cruiseDisplay.SetActive(mode == SpeedometerMode.CRUISE);
    }

    /**
     * Called by the settings button.
     * Opens the settings activity.
     *
     * @param view Unused parameter.
     */
    public void LaunchSettingsActivity(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
