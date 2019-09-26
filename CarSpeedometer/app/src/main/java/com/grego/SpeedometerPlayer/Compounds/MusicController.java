package com.grego.SpeedometerPlayer.Compounds;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.MusicCommand;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;

import java.io.IOException;

/**
 * UI compound to control the Music player via touch gestures.
 * This component handles input and icon feedback animations.
 */
public class MusicController extends ConstraintLayout implements IInputListener
{
    private ImageView playImage;
    private ImageView pauseImage;
    private ImageView prevImage;
    private ImageView nextImage;

    public MusicController(Context context)
    {
        super(context);
        Initialize();
    }

    public MusicController(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Initialize();
    }

    public MusicController(Context context, AttributeSet attrs, int defStyleAttr)
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
        inflate(getContext(), R.layout.compound_music_controller, this);

        if (!isInEditMode())
        {
            // Get UI object references
            playImage = (ImageView) findViewById(R.id.imagePlay);
            pauseImage = (ImageView) findViewById(R.id.imagePause);
            prevImage = (ImageView) findViewById(R.id.imagePrev);
            nextImage = (ImageView) findViewById(R.id.imageNext);

            // Set default values
            playImage.setImageAlpha(0);
            pauseImage.setImageAlpha(0);
            nextImage.setImageAlpha(0);
            prevImage.setImageAlpha(0);
        }
    }

    /**
     * Called when the compound starts displaying.
     * Subscribes the compound to the InputService to listen input events.
     */
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        if (!isInEditMode())
        {
            Core.Services.Input.Subscribe(this);
        }
    }

    /**
     * Called when the compound is no longer visible.
     * Unsubscribe the compound to the InputService to stop listen input events.
     */
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        if (!isInEditMode())
        {
            Core.Services.Input.Unsubscribe(this);
        }
    }

    @Override
    public void OnSingleTap()
    {
        // Does nothing.
    }

    @Override
    public void OnDoubleTap()
    {
        // Does nothing.
    }

    /**
     * Executes the Play/pause command.
     */
    @Override
    public void OnLongPress()
    {
        RunMusicCommand(MusicCommand.PLAY_PAUSE);
        PlayIconAnimation(playImage);
        PlayIconAnimation(pauseImage);
    }

    /**
     * Executes the Next song command.
     */
    @Override
    public void OnSwipeLeft()
    {
        RunMusicCommand(MusicCommand.NEXT);
        PlayIconAnimation(nextImage);
    }

    /**
     * Executes the Previous song command.
     */
    @Override
    public void OnSwipeRight()
    {
        RunMusicCommand(MusicCommand.PREV);
        PlayIconAnimation(prevImage);
    }

    /**
     * Executes the given music command on the Android system as a Media key press simulation.
     * @param mode The command to execute.
     */
    private void RunMusicCommand(MusicCommand mode)
    {
        //AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        String keyCommand = "input keyevent ";

        switch (mode)
        {
            case PLAY_PAUSE:
                //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
                //mAudioManager.dispatchMediaKeyEvent(event);
                keyCommand += KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
                break;

            case NEXT:
                //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
                //mAudioManager.dispatchMediaKeyEvent(event);
                keyCommand += KeyEvent.KEYCODE_MEDIA_NEXT;
                break;

            case PREV:
                //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
                //mAudioManager.dispatchMediaKeyEvent(event);
                keyCommand += KeyEvent.KEYCODE_MEDIA_PREVIOUS;
                break;
        }

        try
        {
            Runtime.getRuntime().exec(keyCommand);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Starts the default animation for the given icon image.
     * @param icon The image to animate.
     */
    private void PlayIconAnimation(ImageView icon)
    {
        Core.Services.Animation.FadeViewInOut(icon, Core.Data.Preferences.fadeAnimationMilliseconds);
    }
}
