package com.emin.totemkillstreak.client.audio;

import com.emin.totemkillstreak.TotemKillStreakMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class CustomSounds {
    public static final SoundEvent COMBO_1 = register("combo.1");
    public static final SoundEvent COMBO_2 = register("combo.2");
    public static final SoundEvent COMBO_3 = register("combo.3");
    public static final SoundEvent COMBO_4 = register("combo.4");
    public static final SoundEvent COMBO_5 = register("combo.5");

    private CustomSounds() {}

    private static SoundEvent register(String path) {
        Identifier id = Identifier.of(TotemKillStreakMod.MOD_ID, path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void initialize() {
        // class load yeterli
    }
}
