package com.emin.totemkillstreak.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import com.emin.totemkillstreak.client.network.PacketHandler;

@Environment(EnvType.CLIENT)
public class TotemKillStreakClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PacketHandler.register();
	}
}
