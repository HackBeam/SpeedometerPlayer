package com.grego.SpeedometerPlayer.Services.Implementations.Sound;

import android.media.MediaPlayer;
import android.net.Uri;

import com.grego.SpeedometerPlayer.Core;
import com.grego.SpeedometerPlayer.GlobalEnums.SoundID;
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
        toPreloadCount = Core.Data.Sounds.pairs.size();
        preloadedCount = 0;
        soundsPreloaded = false;

        for (Map.Entry<SoundID, Integer> entry : Core.Data.Sounds.pairs.entrySet())
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
