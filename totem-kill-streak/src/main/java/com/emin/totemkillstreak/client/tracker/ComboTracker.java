package com.emin.totemkillstreak.client.tracker;

public class ComboTracker {
	public static final ComboTracker INSTANCE = new ComboTracker();
	
	private int currentLevel = 0;
	private long lastComboTime = 0;
	private final long COMBO_TIMEOUT_MS = 3000; // 3 saniye
	private final int MAX_LEVEL = 5;

	private ComboTracker() {
	}

	public void onTotem() {
		long now = System.currentTimeMillis();
		long timeSinceLastCombo = now - lastComboTime;

		// Timeout geçtiyse, sıfırla
		if (timeSinceLastCombo > COMBO_TIMEOUT_MS) {
			currentLevel = 1;
		} else {
			// Timeout'a kadar, level artır (max 5)
			if (currentLevel < MAX_LEVEL) {
				currentLevel++;
			}
		}

		lastComboTime = now;
	}

	public int getCurrentLevel() {
		long now = System.currentTimeMillis();
		long timeSinceLastCombo = now - lastComboTime;

		// Timeout geçtiyse, sıfırla (ama totem patlamadı)
		if (timeSinceLastCombo > COMBO_TIMEOUT_MS) {
			currentLevel = 0;
		}

		return currentLevel;
	}

	public void reset() {
		currentLevel = 0;
		lastComboTime = 0;
	}

	public boolean isComboActive() {
		long now = System.currentTimeMillis();
		return (now - lastComboTime) <= COMBO_TIMEOUT_MS;
	}
}
