package com.emin.totemkillstreak;

import com.emin.totemkillstreak.client.audio.CustomSounds;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TotemKillStreakMod implements ModInitializer {
    public static final String MOD_ID = "totemkillstreak";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        CustomSounds.initialize();
        LOGGER.info("Totem Kill Streak initialized!");
    }
}
