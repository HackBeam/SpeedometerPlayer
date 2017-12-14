package com.grego.SpeedometerPlayer;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Image;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.IOException;

import static android.location.Criteria.ACCURACY_HIGH;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    public TextView km;
    public TextView textKMH;
    public TextView bateria;
    public TextView limit_text;
    public ImageView imgPlay;
    public ImageView imgPause;
    public ImageView imgNext;
    public ImageView imgPrev;

    private TextClock reloj;
    private GestureDetectorCompat gd;
    private BroadcastReceiver batteryReceiver;
    private LocationManager lm;
    private MiLocationListener mls;
    private int limite;

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Comprobar si ya tenemos permiso GPS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //Pedir permiso GPS
        }

        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false); //Load default preferences

        //BRILLO 100%
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = 1.0f;
        getWindow().setAttributes(lp);

        //Cargar preferencias
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        limite = Integer.parseInt(sp.getString("limit120", "120"));
        km = (TextView) findViewById(R.id.kmh);
        textKMH = (TextView) findViewById(R.id.textKMH);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mls = new MiLocationListener(this);

        //Obtener referencias de objetos de interfaz
        bateria = (TextView) findViewById(R.id.bateria);
        limit_text = (TextView) findViewById(R.id.limite);
        reloj = (TextClock) findViewById(R.id.textClock);
        imgPlay = (ImageView) findViewById(R.id.imagePlay);
        imgPause = (ImageView) findViewById(R.id.imagePause);
        imgNext = (ImageView) findViewById(R.id.imageNext);
        imgPrev = (ImageView) findViewById(R.id.imagePrev);

        //Establecer la fuente de la interfaz
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        km.setTypeface(type);
        textKMH.setTypeface(type);
        bateria.setTypeface(type);
        limit_text.setTypeface(type);
        reloj.setTypeface(type);

        //Setear detector de gestos
        gd = new GestureDetectorCompat(this, this);
        gd.setOnDoubleTapListener(this);

        //Setear BroadcastReceiver para bateria
        batteryReceiver = new BatteryBroadcastReceiver();

        actualizarInterfaz();
    }

    @Override
    protected void onStart() {
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(batteryReceiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        limite = Integer.parseInt(sp.getString("limit120", "120"));
        actualizarInterfaz();
    }

    /**
     * Cambia el valor del cuentakilometros y cambia el color del mismo si es necesario
     * Se ejecuta cada vez que el LocationListener se actualiza
     *
     * @param velocidad Nuevo valor de velocidad a mostrar
     */
    public void actualizarKM(float velocidad) {

        float v = velocidad;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String unit = sp.getString("unidades", "km/h");

        if (unit.equals("km/h"))
            v *= 3.6;
        else if (unit.equals("mph"))
            v *= 2.23694;

        int vel = (int) v;

        if (vel > 20)
            vel += sp.getInt("modo_seguro", 0);

        if (vel < 0)
            km.setText("---");
        else
            km.setText(Integer.toString(vel));

        textKMH.setText(unit);

        if (vel >= limite) //Estamos por encima del limite
        {
            if (vel >= limite + 15) //Estamos MUY por encima del limite
            {
                km.setTextColor(Color.RED);
                km.setShadowLayer(20, 0, 0, Color.RED);
                textKMH.setTextColor(Color.RED);
                textKMH.setShadowLayer(20, 0, 0, Color.RED);
            }
            else
            {
                km.setTextColor(Color.parseColor("#feaa0c"));
                km.setShadowLayer(20, 0, 0, Color.parseColor("#feaa0c"));
                textKMH.setTextColor(Color.parseColor("#feaa0c"));
                textKMH.setShadowLayer(20, 0, 0, Color.parseColor("#feaa0c"));
            }
        }
        else
        {
            km.setTextColor(Color.parseColor("#33B5E5"));
            km.setShadowLayer(20, 0, 0, Color.parseColor("#33B5E5"));
            textKMH.setTextColor(Color.parseColor("#33B5E5"));
            textKMH.setShadowLayer(20, 0, 0, Color.parseColor("#33B5E5"));
        }

    }

    /**
     * Actualiza el valor del limite de velocidad y el valor mostrado de bateria
     */
    private void actualizarLimite() {
        limit_text.setText(Integer.toString(limite));
        //actualizarBateria();
    }

    /**
     * Actualiza el valor mostrado de bateria
     * Se ejecuta cada vez que interactuamos con la pantalla
     */
    /*private void actualizarBateria() {
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = (level / (float)scale) * 100;
        bateria.setText(Integer.toString((int) batteryPct)+"%");
    }
*/
    private class BatteryBroadcastReceiver extends BroadcastReceiver {
        private final static String BATTERY_LEVEL = "level";
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BATTERY_LEVEL, 0);
            bateria.setText(level + "%");
        }
    }

    /**
     * Actualiza todos los elementos de la interfaz
     */
    public void actualizarInterfaz() {
        float vel = mls.velocidad;

        actualizarKM(vel);
        actualizarLimite();
        //actualizarBateria();

        //Modo espejo?
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("modo_espejo", false))
            findViewById(R.id.espejable).setScaleX(-1f);
        else
            findViewById(R.id.espejable).setScaleX(1f);

    }

    /**
     * Controla la reproduccion del reproductor por defecto
     *
     * @param mode Accion a realizar: Atras, Adelante o Play/pausa
     */
    public void musicControl(PlayerControles mode) {
        //AudioManager mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        String keyCommand = "input keyevent ";

        if(mode == PlayerControles.NEXT)
        {
            //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
            //mAudioManager.dispatchMediaKeyEvent(event);
            keyCommand += KeyEvent.KEYCODE_MEDIA_NEXT;
            FadeInImage(imgNext);
        }
        else if(mode == PlayerControles.PLAYPAUSE)
        {
            //KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
            //mAudioManager.dispatchMediaKeyEvent(event);
            keyCommand += KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
            FadeInImage(imgPause);
            FadeInImage(imgPlay);
        }
        else if(mode == PlayerControles.PREV)
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        actualizarInterfaz();
    }

    /**
     * Se ejecuta cuando se solicita permiso para obtener ubicacion
     * En caso de haber obtenido los permisos, se inicializa el LocationListener
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0.1f, mls);
                } else {
                    km.setText("No GPS");
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }

    /** ----- OnDoubleTapListener Methods ----- */

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        if (limite == Integer.parseInt(sp.getString("limit120", "120")))
            limite = Integer.parseInt(sp.getString("limit100", "100"));
        else
            limite = Integer.parseInt(sp.getString("limit120", "120"));

        actualizarLimite();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        if (limite == Integer.parseInt(sp.getString("limit80", "120")))
            limite = Integer.parseInt(sp.getString("limit60", "100"));
        else
            limite = Integer.parseInt(sp.getString("limit80", "120"));

        actualizarLimite();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    /** ----- OnGestureListener Methods ----- */

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        musicControl(PlayerControles.PLAYPAUSE);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight();
                } else {
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
    public boolean onTouchEvent(MotionEvent event){
        this.gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void onSwipeRight() {
        musicControl(PlayerControles.PREV);
    }

    private void onSwipeLeft() {
        musicControl(PlayerControles.NEXT);
    }

    public void abrirConfig(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(mls);
    }

    public void FadeInImage(ImageView img)
    {
        int animationDuration = 1000;

        img.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        //FadeOut con un Delay
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FadeOutAll();
            }
        }, animationDuration);
    }

    private void FadeOutAll()
    {
        int animationDuration = 1000;

        imgPrev.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(null);

        imgNext.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(null);

        imgPause.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(null);

        imgPlay.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(null);
    }
}
