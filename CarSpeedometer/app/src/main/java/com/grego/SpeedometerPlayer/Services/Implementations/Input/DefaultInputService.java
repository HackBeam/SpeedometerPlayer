package com.grego.SpeedometerPlayer.Services.Implementations.Input;

import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.Services.Definitions.IInputService;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Input events provider.
 */
public class DefaultInputService implements IInputService, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnTouchListener
{
    private ArrayList<IInputListener> subscribers;
    private GestureDetectorCompat gestureDetector;
    private boolean canNotifySubscribers = false;

    public DefaultInputService()
    {
        subscribers = new ArrayList<>();
        gestureDetector = new GestureDetectorCompat(Core.ApplicationContext, this);
        gestureDetector.setOnDoubleTapListener(this);
    }

    @Override
    public void StartListening(View inputView)
    {
        inputView.setOnTouchListener(this);
        canNotifySubscribers = true;
    }

    /**
     * The service is always listening gestures once started listening.
     * This method only denies to notify subscribers.
     */
    @Override
    public void StopListening()
    {
        canNotifySubscribers = false;
    }

    @Override
    public void Subscribe(IInputListener listener)
    {
        subscribers.add(listener);
    }

    @Override
    public void Unsubscribe(IInputListener listener)
    {
        subscribers.remove(listener);
    }

    //region OnTouchListener methods

    /**
     * Called by the view when a touch happens.
     * Bridges the motion event data to the Gesture detector.
     *
     * @param view The view capturing the input.
     * @param motionEvent Input data.
     * @return True if the event has been consumed, false otherwise.
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        return gestureDetector.onTouchEvent(motionEvent);
    }
    //endregion

    //region GESTURE DETECTOR EVENTS
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent)
    {
        if (canNotifySubscribers && subscribers.size() > 0)
        {
            Iterator<IInputListener> iterator = subscribers.iterator();
            while (iterator.hasNext())
            {
                iterator.next().OnSingleTap();
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent)
    {
        if (canNotifySubscribers && subscribers.size() > 0)
        {
            Iterator<IInputListener> iterator = subscribers.iterator();
            while (iterator.hasNext())
            {
                iterator.next().OnDoubleTap();
            }
            return true;
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent)
    {
        if (canNotifySubscribers && subscribers.size() > 0)
        {
            Iterator<IInputListener> iterator = subscribers.iterator();
            while (iterator.hasNext())
            {
                iterator.next().OnLongPress();
            }
        }
    }

    @Override
    public boolean onFling(MotionEvent motionEventFirst, MotionEvent motionEventSecond, float velocityX, float velocityY)
    {
        boolean result = false;
        if (canNotifySubscribers && subscribers.size() > 0)
        {

            float diffY = motionEventSecond.getY() - motionEventFirst.getY();
            float diffX = motionEventSecond.getX() - motionEventFirst.getX();

            if (Math.abs(diffX) > Math.abs(diffY))
            {
                if (Math.abs(diffX) > Core.Data.Preferences.swipeThreshold && Math.abs(velocityX) > Core.Data.Preferences.swipeVelocityThreshold)
                {
                    if (diffX > 0)
                    {
                        Iterator<IInputListener> iterator = subscribers.iterator();
                        while (iterator.hasNext())
                        {
                            iterator.next().OnSwipeRight();
                        }
                    }
                    else
                    {
                        Iterator<IInputListener> iterator = subscribers.iterator();
                        while (iterator.hasNext())
                        {
                            iterator.next().OnSwipeLeft();
                        }
                    }

                    result = true;
                }
            }
            //TODO: Uncomment this to detect also swipe up and swipe down when needed
            /*else if (Math.abs(diffY) > Core.Data.Preferences.swipeThreshold && Math.abs(velocityY) > Core.Data.Preferences.swipeVelocityThreshold)
            {
                if (diffY > 0)
                {
                    Iterator<IInputListener> iterator = subscribers.iterator();
                    while (iterator.hasNext())
                    {
                        iterator.next().OnSwipeDown();
                    }
                }
                else
                {
                    Iterator<IInputListener> iterator = subscribers.iterator();
                    while (iterator.hasNext())
                    {
                        iterator.next().OnSwipeUp();
                    }
                }

                result = true;
            }*/
        }

        return result;
    }

    //region Unused events
    @Override
    public boolean onDown(MotionEvent motionEvent)
    {
        // Returning true from an ACTION_DOWN event you are interested
        // in the rest of the events in that gesture.
        return canNotifySubscribers;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent)
    {
        // Does nothing.
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent)
    {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1)
    {
        return false;
    }
    //endregion

    //endregion
}
