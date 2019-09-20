package com.grego.SpeedometerPlayer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.grego.SpeedometerPlayer.Compounds.BatteryDisplay;
import com.grego.SpeedometerPlayer.Compounds.SpeedometerDisplay;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
    public TextView limit_text;
    public ImageView imgPlay;
    public ImageView imgPause;
    public ImageView imgNext;
    public ImageView imgPrev;
    private BatteryDisplay batteryDisplay;
    private SpeedometerDisplay speedometerDisplay;

    private TextClock reloj;
    private GestureDetectorCompat gd;

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Core.Initialize(this);
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1); //Pedir permiso GPS

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false); //Load default preferences

        Core.Helpers.SetMaxScreenBrightness(this);

        //Obtener referencias de objetos de interfaz
        batteryDisplay = (BatteryDisplay) findViewById(R.id.battery_display);
        speedometerDisplay = (SpeedometerDisplay) findViewById(R.id.limit_speedometer);
        reloj = (TextClock) findViewById(R.id.textClock);

        // LEGACY
        limit_text = (TextView) findViewById(R.id.limite);
        imgPlay = (ImageView) findViewById(R.id.imagePlay);
        imgPause = (ImageView) findViewById(R.id.imagePause);
        imgNext = (ImageView) findViewById(R.id.imageNext);
        imgPrev = (ImageView) findViewById(R.id.imagePrev);

        //Establecer la fuente de la interfaz
        limit_text.setTypeface(Core.Data.DefaultFont);
        reloj.setTypeface(Core.Data.DefaultFont);

        //Setear detector de gestos
        gd = new GestureDetectorCompat(this, this);
        gd.setOnDoubleTapListener(this);

        UpdateUI();
    }

    @Override
    protected void onStart()
    {
        Core.Services.Battery.StartListening(this);
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        Core.Services.Battery.StopListening(this);
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        UpdateUI();
    }

    /**
     * Updates the displayed limit value.
     */
    private void UpdateShowingLimit()
    {
        //limit_text.setText(Core.Data.currentLimit); //TODO: Replace with a Limit compound
    }

    /**
     * Updates all elements in the UI
     */
    public void UpdateUI()
    {
        batteryDisplay.UpdateUI();
        speedometerDisplay.UpdateUI();

        //LEGACY
        UpdateShowingLimit();

        //Modo espejo?
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("mirror_mode", false))
        {
            findViewById(R.id.espejable).setScaleX(-1f);
        }
        else
        {
            findViewById(R.id.espejable).setScaleX(1f);
        }

    }

    /**
     * Controla la reproduccion del reproductor por defecto
     *
     * @param mode Accion a realizar: Atras, Adelante o Play/pausa
     */
    public void musicControl(PlayerControles mode)
    {
        //AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        String keyCommand = "input keyevent ";

        if (mode == PlayerControles.NEXT)
        {
            //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
            //mAudioManager.dispatchMediaKeyEvent(event);
            keyCommand += KeyEvent.KEYCODE_MEDIA_NEXT;
            FadeInImage(imgNext);
        }
        else if (mode == PlayerControles.PLAYPAUSE)
        {
            //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
            //mAudioManager.dispatchMediaKeyEvent(event);
            keyCommand += KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
            FadeInImage(imgPause);
            FadeInImage(imgPlay);
        }
        else if (mode == PlayerControles.PREV)
        {
            //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
            //mAudioManager.dispatchMediaKeyEvent(event);
            keyCommand += KeyEvent.KEYCODE_MEDIA_PREVIOUS;
            FadeInImage(imgPrev);
        }

        try
        {
            Runtime.getRuntime().exec(keyCommand);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        UpdateUI();
    }

    /** ----- OnDoubleTapListener Methods ----- */

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e)
    {
        if (Core.Data.currentLimit == Core.Data.Preferences.highLimitOneTap)
        {
            Core.Data.currentLimit = Core.Data.Preferences.lowLimitOneTap;
        }
        else
        {
            Core.Data.currentLimit = Core.Data.Preferences.highLimitOneTap;
        }

        UpdateShowingLimit();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e)
    {
        if (Core.Data.currentLimit == Core.Data.Preferences.highLimitDoubleTap)
        {
            Core.Data.currentLimit = Core.Data.Preferences.lowLimitDoubleTap;
        }
        else
        {
            Core.Data.currentLimit = Core.Data.Preferences.highLimitDoubleTap;
        }

        UpdateShowingLimit();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e)
    {
        return false;
    }

    /** ----- OnGestureListener Methods ----- */

    @Override
    public boolean onDown(MotionEvent e)
    {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
        musicControl(PlayerControles.PLAYPAUSE);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        boolean result = false;
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();
        if (Math.abs(diffX) > Math.abs(diffY))
        {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
            {
                if (diffX > 0)
                {
                    onSwipeRight();
                }
                else
                {
                    onSwipeLeft();
                }
                result = true;
            }
        }
        /*else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffY > 0) {
                onSwipeBottom();
            } else {
                onSwipeTop();
            }
            result = true;
        }*/
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        this.gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void onSwipeRight()
    {
        musicControl(PlayerControles.PREV);
    }

    private void onSwipeLeft()
    {
        musicControl(PlayerControles.NEXT);
    }

    public void abrirConfig(View v)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        //TODO: Stop all services
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

        imgPrev.animate().alpha(0f).setDuration(animationDuration).setListener(null);

        imgNext.animate().alpha(0f).setDuration(animationDuration).setListener(null);

        imgPause.animate().alpha(0f).setDuration(animationDuration).setListener(null);

        imgPlay.animate().alpha(0f).setDuration(animationDuration).setListener(null);
    }
}
