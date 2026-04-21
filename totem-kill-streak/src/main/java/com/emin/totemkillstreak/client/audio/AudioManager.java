package com.emin.totemkillstreak.client.audio;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AudioManager {
	private static final String MOD_ID = "totemkillstreak";
	
	private static final SoundEvent[] COMBO_SOUNDS = new SoundEvent[5];

	static {
		for (int i = 1; i <= 5; i++) {
			Identifier id = Identifier.of(MOD_ID, "combo." + i);
			COMBO_SOUNDS[i - 1] = SoundEvent.of(id);
		}
	}

	public static void playComboSound(int level) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null) return;

		// Level 5+ için combo_5 kullan
		int soundIndex = Math.min(level - 1, 4);
		if (soundIndex < 0) return;

		SoundEvent soundEvent = COMBO_SOUNDS[soundIndex];
		PositionedSoundInstance sound = PositionedSoundInstance.master(
			soundEvent,
			1.0f, // volume
			1.0f  // pitch
		);

		client.getSoundManager().play(sound);
	}
}
