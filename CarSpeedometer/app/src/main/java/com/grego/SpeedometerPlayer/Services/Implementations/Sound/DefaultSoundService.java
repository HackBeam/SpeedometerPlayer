package com.grego.SpeedometerPlayer.Services.Implementations.Sound;

import android.media.MediaPlayer;
import android.net.Uri;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.GlobalEnums.SoundID;
import com.grego.SpeedometerPlayer.R;
import com.grego.SpeedometerPlayer.Services.Definitions.ISoundService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Stores a correspondence between sound IDs and clips.
 * Provides a sound play function.
 */
public class DefaultSoundService implements ISoundService, MediaPlayer.OnPreparedListener
{
    private static final HashMap<SoundID, Integer> configuration = LoadConfiguration();

    private HashMap<SoundID, MediaPlayer> players;

    //region Pre-loading state variables
    private Lock preloadLock;
    private boolean soundsPreloaded = false;
    private int toPreloadCount = 0;
    private int preloadedCount = 0;
    //endregion

    public DefaultSoundService()
    {
        players = new HashMap<>();
        PreloadSounds();
    }

    @Override
    public void PlaySound(SoundID id)
    {
        if (soundsPreloaded && Core.Data.Preferences.soundEnabled && players.containsKey(id))
        {
            players.get(id).start();
        }
    }

    /**
     * Pre-loads all sounds asynchronously.
     */
    private void PreloadSounds()
    {
        preloadLock = new ReentrantLock();
        toPreloadCount = configuration.size();
        preloadedCount = 0;
        soundsPreloaded = false;

        for (Map.Entry<SoundID, Integer> entry : configuration.entrySet())
        {
            MediaPlayer player = new MediaPlayer();
            Uri soundUri = Uri.parse(Core.Data.Preferences.resourcesUri + entry.getValue());

            try
            {
                player.setDataSource(Core.ApplicationContext, soundUri);
            }
            catch (IOException e)
            {
                continue;
            }

            player.setOnPreparedListener(this);
            player.prepareAsync();

            players.put(entry.getKey(), player);
        }
    }

    /**
     * Called to initialize the configuration map.
     * @return The configuration map initialized.
     */
    private static HashMap<SoundID, Integer> LoadConfiguration()
    {
        HashMap<SoundID, Integer> map = new HashMap<>();

        map.put(SoundID.LIMIT_REACHED, R.raw.test);
        map.put(SoundID.LIMIT_DANGER, R.raw.test);
        map.put(SoundID.CRUISE_BELOW, R.raw.test);
        map.put(SoundID.CRUISE_ABOVE, R.raw.test);
        map.put(SoundID.CRUISE_DANGER, R.raw.test);

        return map;
    }

    //region MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mediaPlayer)
    {
        preloadLock.lock();
        soundsPreloaded = (++preloadedCount == toPreloadCount);
        preloadLock.unlock();
    }
    //endregion
}
