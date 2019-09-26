package com.grego.SpeedometerPlayer.Services.Definitions;

import android.view.View;

/**
 * Interface to be implemented by an Animation provider service
 */
public interface IAnimationService
{
    /**
     * Plays a fade in and out animation on the given view.
     * @param view The view to animate.
     * @param animationMilliseconds Duration of each fade animation (this is half of the complete in-out animation).
     */
    void FadeViewInOut(View view, int animationMilliseconds);
}
