package dev.dubhe.anvilcraft.mixin;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.init.ModItemProperties;
import dev.dubhe.anvilcraft.init.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemProperties.class)
public abstract class ItemPropertiesMixin {
    @Shadow
    public static void register(Item p_174571_, ResourceLocation p_174572_, ItemPropertyFunction p_174573_) {
    }

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onRegister(CallbackInfo ci){
        register(
            ModItems.IONOCRAFT_BACKPACK.asItem(),
            AnvilCraft.of("flight_time"),
            ModItemProperties.FLIGHT_TIME
        );
    }
}
