package com.grego.SpeedometerPlayer.Compounds;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.Services.Implementations.IBatteryListener;
import com.grego.SpeedometerPlayer.R;

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

            fillParams = batteryFill.getLayoutParams();
            maxFillHeight = fillParams.height;
            batteryLevelText.setTypeface(Core.Data.DefaultFont);
            batteryPercentText.setTypeface(Core.Data.DefaultFont);

            // Subscribe to listen battery changes
            Core.Services.Battery.Subscribe(this);
        }
    }

    @Override
    public void OnBatteryLevelReceived(int batteryLevel)
    {
        this.batteryLevel = batteryLevel;
        UpdateUI();
    }

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

    private void UpdateBatteryIcon(int colorToApply)
    {
        batteryIcon.setColorFilter(colorToApply);
        fillParams.height = batteryLevel * maxFillHeight / 100;
        batteryFill.setLayoutParams(fillParams);
    }
}
