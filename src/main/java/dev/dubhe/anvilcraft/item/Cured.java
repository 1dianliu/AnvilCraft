package dev.dubhe.anvilcraft.item;

import dev.dubhe.anvilcraft.init.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface Cured {
    default void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!(entity instanceof Player player)) return;
        if (player.getAbilities().instabuild) return;
        MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, 200, 1, false, true);
        MobEffectInstance slowness = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, false, true);
        MobEffectInstance hungry = new MobEffectInstance(MobEffects.HUNGER, 200, 1, false, true);
        player.addEffect((weakness));
        int curedNumber = player.getInventory().countItem(ModItems.CURSED_GOLD_INGOT) + player.getInventory().countItem(ModItems.CURSED_GOLD_NUGGET) + player.getInventory().countItem(ModItems.CURSED_GOLD_BLOCK);
        if (curedNumber > 8) {
            player.addEffect((slowness));
        }
        if (curedNumber > 64) {
            player.addEffect((hungry));
        }
    }
}
