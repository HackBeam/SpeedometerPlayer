package com.grego.SpeedometerPlayer.Compounds;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.MusicCommand;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Listeners.IInputListener;

import java.io.IOException;

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
        }
    }

    /**
     * Called when the compound starts displaying.
     * Subscribes the compound to the BatteryService to listen battery changes.
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
     * Unsubscribe the compound to the BatteryService to stop listen battery changes.
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

    }

    @Override
    public void OnDoubleTap()
    {

    }

    @Override
    public void OnLongPress()
    {
        RunMusicCommand(MusicCommand.PLAYPAUSE);
    }

    @Override
    public void OnSwipeLeft()
    {
        RunMusicCommand(MusicCommand.NEXT);
    }

    @Override
    public void OnSwipeRight()
    {
        RunMusicCommand(MusicCommand.PREV);
    }

    public void RunMusicCommand(MusicCommand mode)
    {
        //AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        String keyCommand = "input keyevent ";

        if (mode == MusicCommand.NEXT)
        {
            //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
            //mAudioManager.dispatchMediaKeyEvent(event);
            keyCommand += KeyEvent.KEYCODE_MEDIA_NEXT;
            FadeInImage(nextImage);
        }
        else if (mode == MusicCommand.PLAYPAUSE)
        {
            //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
            //mAudioManager.dispatchMediaKeyEvent(event);
            keyCommand += KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
            FadeInImage(playImage);
            FadeInImage(pauseImage);
        }
        else if (mode == MusicCommand.PREV)
        {
            //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
            //mAudioManager.dispatchMediaKeyEvent(event);
            keyCommand += KeyEvent.KEYCODE_MEDIA_PREVIOUS;
            FadeInImage(prevImage);
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


    public void FadeInImage(ImageView img)
    {
        int animationDuration = 1000;

        img.animate().alpha(1f).setDuration(animationDuration).setListener(null);

        //FadeOut con un Delay
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                FadeOutAll();
            }
        }, animationDuration);
    }

    private void FadeOutAll()
    {
        int animationDuration = 1000;

        playImage.animate().alpha(0f).setDuration(animationDuration).setListener(null);
        pauseImage.animate().alpha(0f).setDuration(animationDuration).setListener(null);
        prevImage.animate().alpha(0f).setDuration(animationDuration).setListener(null);
        nextImage.animate().alpha(0f).setDuration(animationDuration).setListener(null);
    }
}
