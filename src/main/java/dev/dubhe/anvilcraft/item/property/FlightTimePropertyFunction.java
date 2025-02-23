package dev.dubhe.anvilcraft.item.property;

import dev.dubhe.anvilcraft.init.ModComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import static dev.dubhe.anvilcraft.item.IonocraftBackpackItem.FLIGHT_MAX_TIME;

public class FlightTimePropertyFunction implements ItemPropertyFunction {
    @Override
    public float call(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (stack.getOrDefault(ModComponents.FLIGHT_TIME, FLIGHT_MAX_TIME) > 0) {
            return 1;
        }
        return 0;
    }
}
