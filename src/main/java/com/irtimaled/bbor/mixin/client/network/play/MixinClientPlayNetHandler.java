package com.irtimaled.bbor.mixin.client.network.play;

import com.irtimaled.bbor.client.interop.ClientInterop;
import com.irtimaled.bbor.common.interop.CommonInterop;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.UnloadChunkS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetHandler {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void onDisconnect(CallbackInfo ci) {
        ClientInterop.disconnectedFromRemoteServer();
    }

    @Inject(method = "onUnloadChunk", at = @At("RETURN"))
    private void onChunkUnload(UnloadChunkS2CPacket packet, CallbackInfo ci) {
        ClientInterop.unloadChunk(packet.getX(), packet.getZ());
    }
    
    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onGameJoined(GameJoinS2CPacket packet, CallbackInfo ci) {
        assert this.client.world != null;
        assert this.client.player != null;
        CommonInterop.loadWorldStructures(this.client.world);
    }
}
