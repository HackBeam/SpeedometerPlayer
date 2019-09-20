package com.grego.SpeedometerPlayer.Compounds;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.Services.Listeners.IBatteryListener;
import com.grego.SpeedometerPlayer.R;

/**
 * UI component to display the battery level both with text and fillable icon
 */
public class BatteryDisplay extends ConstraintLayout implements IBatteryListener
{
    private final static int LOW_BATTERY_LEVEL = 25;
    private final static int VERY_LOW_BATTERY_LEVEL = 15;

    private TextView batteryLevelText;
    private TextView batteryPercentText;
    private ImageView batteryIcon;
    private ImageView batteryFill;
    private ViewGroup.LayoutParams fillParams;
    private int maxFillHeight;

    private int batteryLevel;

    public BatteryDisplay(Context context)
    {
        super(context);
        Initialize();
    }

    public BatteryDisplay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Initialize();
    }

    public BatteryDisplay(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        Initialize();
    }

    /**
     * Initializes the component.
     * - Inflates it's XML.
     * - Gets the UI objects.
     * - Establish all the default values.
     */
    private void Initialize()
    {
        inflate(getContext(), R.layout.compound_battery_display, this);

        if (!isInEditMode())
        {
            // Get UI object references
            batteryIcon = (ImageView) findViewById(R.id.battery_icon);
            batteryFill = (ImageView) findViewById(R.id.battery_fill);
            batteryLevelText = (TextView) findViewById(R.id.battery_level);
            batteryPercentText = (TextView) findViewById(R.id.battery_percent_symbol);

            // Get and set default parameters
            fillParams = batteryFill.getLayoutParams();
            maxFillHeight = fillParams.height;
            batteryLevelText.setTypeface(Core.Data.DefaultFont);
            batteryPercentText.setTypeface(Core.Data.DefaultFont);
        }
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        // Subscribe to listen battery changes
        Core.Services.Battery.Subscribe(this);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        // Unsubscribe to listen battery changes
        Core.Services.Battery.Unsubscribe(this);
    }

    @Override
    public void OnBatteryLevelReceived(int batteryLevel)
    {
        this.batteryLevel = batteryLevel;
        UpdateUI();
    }

    /**
     * Updates the displaying UI of the component with the stored values.
     */
    private void UpdateUI()
    {
        int colorToApply = Core.Data.Colors.normal;

        if (batteryLevel < LOW_BATTERY_LEVEL)
        {
            if (batteryLevel < VERY_LOW_BATTERY_LEVEL)
            {
                colorToApply = Core.Data.Colors.limitDanger;
            }
            else
            {
                colorToApply = Core.Data.Colors.limitWarning;
            }
        }

        Core.Helpers.SetColorToTextView(batteryLevelText, colorToApply);
        Core.Helpers.SetColorToTextView(batteryPercentText, colorToApply);

        batteryLevelText.setText(Integer.toString(batteryLevel));
        UpdateBatteryIcon(colorToApply);
    }

    /**
     * Updates the icon color and fill rate.
     * @param colorToApply The color to asign to the icon.
     */
    private void UpdateBatteryIcon(int colorToApply)
    {
        batteryIcon.setColorFilter(colorToApply);
        batteryFill.setColorFilter(colorToApply);
        fillParams.height = batteryLevel * maxFillHeight / 100;
        batteryFill.setLayoutParams(fillParams);
    }
}
