package com.grego.SpeedometerPlayer.Services.Listeners;

/**
 * Interface to be implemented for classes which wants to listen input events.
 */
public interface IInputListener
{
    /**
     * Triggered when a single tap (confirmed) is made.
     */
    void OnSingleTap();

    /**
     * Triggered when a double tap is made.
     */
    void OnDoubleTap();

    /**
     * Triggered when a long press is detected.
     */
    void OnLongPress();

    /**
     * Triggered when a swipe to the left is detected.
     */
    void OnSwipeLeft();

    /**
     * Triggered when a swipe to the right is detected.
     */
    void OnSwipeRight();

    //TODO: Uncomment those methods when vertical swipping were needed
    /**
     * Triggered when a swipe from down to up is detected.
     */
    //void OnSwipeUp();

    /**
     * Triggered when a swipe from up to down is detected.
     */
    //void OnSwipeDown();
}
