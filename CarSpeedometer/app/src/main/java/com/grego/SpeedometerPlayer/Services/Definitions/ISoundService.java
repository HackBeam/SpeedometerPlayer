package com.grego.SpeedometerPlayer.Services.Definitions;

import com.grego.SpeedometerPlayer.GlobalEnums.SoundID;

/**
 * Interface to be implemented by a Sound provider service
 */
public interface ISoundService
{
    /**
     * Start playing the sound attached to the given ID.
     * @param id The ID of the sound to play.
     */
    void PlaySound(SoundID id);
}
