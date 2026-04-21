package com.emin.totemkillstreak.mixin;

import com.emin.totemkillstreak.client.audio.AudioManager;
import com.emin.totemkillstreak.client.tracker.ComboTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class TotemMixin {

    @Inject(method = "onEntityStatus", at = @At("TAIL"))
    private void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (packet.getStatus() != 35) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        Entity entity = packet.getEntity(client.world);
        if (entity == null) return;
        if (entity == client.player) return;
        if (!(entity instanceof PlayerEntity)) return;

        ComboTracker.INSTANCE.onTotem();
        int level = ComboTracker.INSTANCE.getCurrentLevel();
        AudioManager.playComboSound(level);
    }
}
