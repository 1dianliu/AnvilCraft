package dev.dubhe.anvilcraft.api.anvil.impl;

import dev.dubhe.anvilcraft.api.anvil.AnvilBehavior;
import dev.dubhe.anvilcraft.api.event.entity.AnvilFallOnLandEvent;
import dev.dubhe.anvilcraft.init.ModRecipeTypes;
import dev.dubhe.anvilcraft.util.AnvilUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemStampingBehavior implements AnvilBehavior {
    @Override
    public boolean handle(
            Level level,
            BlockPos hitBlockPos,
            BlockState hitBlockState,
            float fallDistance,
            AnvilFallOnLandEvent event) {
        return AnvilUtil.itemProcess(
                ModRecipeTypes.STAMPING_TYPE.get(),
                level,
                hitBlockPos,
                hitBlockPos.getCenter().add(0, 0.25, 0));
    }
}
