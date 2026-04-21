package com.emin.totemkillstreak.client.audio;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;

@Environment(EnvType.CLIENT)
public class AudioManager {

    public static void playComboSound(int level) {
        if (level <= 0) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        SoundEvent soundEvent = switch (Math.min(level, 5)) {
            case 1 -> CustomSounds.COMBO_1;
            case 2 -> CustomSounds.COMBO_2;
            case 3 -> CustomSounds.COMBO_3;
            case 4 -> CustomSounds.COMBO_4;
            default -> CustomSounds.COMBO_5;
        };

        client.getSoundManager().play(PositionedSoundInstance.master(soundEvent, 1.0f, 1.0f));
    }
}
