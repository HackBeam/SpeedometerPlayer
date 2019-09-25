package com.grego.SpeedometerPlayer.Services.Definitions;

import android.view.View;

import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;

/**
 * Interface to be implemented by a Input event service
 */
public interface IInputService
{
    /**
     * Starts listening android touches and gestures.
     * @param inputView The view used to listen input.
     */
    void StartListening(View inputView);

    /**
     * Stops listening android touches and gestures.
     */
    void StopListening();

    /**
     * Sets the given object to be notified of input events.
     * @param listener The object to notify to.
     */
    void Subscribe(IInputListener listener);

    /**
     * Unset the given object to be notified of input events.
     * @param listener The object to stop notifying to.
     */
    void Unsubscribe(IInputListener listener);
}
