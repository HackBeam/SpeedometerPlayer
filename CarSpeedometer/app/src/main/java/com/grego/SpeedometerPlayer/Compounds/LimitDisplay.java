package com.grego.SpeedometerPlayer.Compounds;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Listeners.ILimitListener;

/**
 * UI compound to display the current speed limit.
 */
public class LimitDisplay extends ConstraintLayout implements ILimitListener
{
    private TextView limitValueText;
    private int cachedLimit = 120;

    public LimitDisplay(Context context)
    {
        super(context);
        Initialize();
    }

    public LimitDisplay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Initialize();
    }

    public LimitDisplay(Context context, AttributeSet attrs, int defStyleAttr)
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
        inflate(getContext(), R.layout.compound_limit_display, this);

        if (!isInEditMode())
        {
            // Get UI object references
            limitValueText = (TextView) findViewById(R.id.limit_value);
            limitValueText.setTypeface(Core.Data.DefaultFont);
            UpdateUI();
        }
    }

    /**
     * Called when the compound starts displaying.
     * Subscribes the compound to the LimitService to listen limit changes.
     */
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        if (!isInEditMode())
        {
            Core.Services.Limit.Subscribe(this);
        }
    }

    /**
     * Called when the compound is no longer visible.
     * Unsubscribe the compound to the LimitService to stop listen limit changes.
     */
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        if (!isInEditMode())
        {
            Core.Services.Limit.Unsubscribe(this);
        }
    }

    @Override
    public void OnLimitChange(int newLimit)
    {
        cachedLimit = newLimit;
        UpdateUI();
    }

    /**
     * Updates the displaying UI of the component.
     */
    public void UpdateUI()
    {
        limitValueText.setText(Integer.toString(cachedLimit));
    }
}
