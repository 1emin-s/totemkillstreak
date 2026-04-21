package com.emin.totemkillstreak.client.network;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import com.emin.totemkillstreak.client.tracker.ComboTracker;
import com.emin.totemkillstreak.client.audio.AudioManager;

@Environment(EnvType.CLIENT)
public class PacketHandler {
	private static final byte TOTEM_STATUS = 35; // Totem activation status

	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(
			EntityStatusS2CPacket.TYPE,
			(packet, context) -> onEntityStatus(packet)
		);
	}

	private static void onEntityStatus(EntityStatusS2CPacket packet) {
		// Totem activation kontrolü
		if (packet.getStatus() != TOTEM_STATUS) {
			return;
		}

		MinecraftClient client = MinecraftClient.getInstance();
		if (client.world == null || client.player == null) {
			return;
		}

		// Entity'yi al
		Entity entity = packet.getEntity(client.world);
		if (entity == null) {
			return;
		}

		// Kendi totemim mi?
		if (entity == client.player) {
			// Kendi totemi → vanilla ses zaten çalıyor
			return;
		}

		// Başkasının totemi → combo başlat
		if (entity.isAlive()) { // Gereksiz ama, bir entity validity check
			ComboTracker.INSTANCE.onTotem();
			int level = ComboTracker.INSTANCE.getCurrentLevel();
			AudioManager.playComboSound(level);
		}
	}
}
