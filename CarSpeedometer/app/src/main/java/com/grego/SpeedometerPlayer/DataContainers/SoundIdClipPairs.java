package com.grego.SpeedometerPlayer.DataContainers;

import com.grego.SpeedometerPlayer.GlobalEnums.SoundID;
import com.grego.SpeedometerPlayer.R;

import java.util.HashMap;

public class SoundIdClipPairs
{
    public final HashMap<SoundID, Integer> pairs = LoadConfiguration();

    /**
     * Called to initialize the configuration map.
     * @return The configuration map initialized.
     */
    private static HashMap<SoundID, Integer> LoadConfiguration()
    {
        HashMap<SoundID, Integer> map = new HashMap<>();

        map.put(SoundID.LIMIT_REACHED, R.raw.user_should_decelerate);
        map.put(SoundID.LIMIT_DANGER, R.raw.danger_speed_reached);
        map.put(SoundID.CRUISE_BELOW, R.raw.user_should_accelerate);
        map.put(SoundID.CRUISE_ABOVE, R.raw.user_should_accelerate);
        map.put(SoundID.CRUISE_DANGER, R.raw.danger_speed_reached);
        map.put(SoundID.SIGNAL_LOST, R.raw.signal_lost);

        return map;
    }
}
