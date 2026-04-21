package com.emin.totemkillstreak.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

@Environment(EnvType.CLIENT)
public class TotemKillStreakClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Burada packet register etmiyoruz.
        // Totem event'i mixin ile yakalanıyor.
    }
}
