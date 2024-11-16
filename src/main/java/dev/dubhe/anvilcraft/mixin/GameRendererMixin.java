package dev.dubhe.anvilcraft.mixin;

import dev.dubhe.anvilcraft.client.init.ModShaders;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(
        method = "reloadShaders",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GameRenderer;loadBlurEffect(Lnet/minecraft/server/packs/resources/ResourceProvider;)V"
        )
    )
    void loadBloomEffect(ResourceProvider resourceProvider, CallbackInfo ci) throws IOException {
        ModShaders.loadBloomEffect(resourceProvider);
    }

    @Inject(
        method = "resize",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;resize(II)V"
        )
    )
    void resize(int width, int height, CallbackInfo ci){
        ModShaders.resize(width, height);
    }
}
