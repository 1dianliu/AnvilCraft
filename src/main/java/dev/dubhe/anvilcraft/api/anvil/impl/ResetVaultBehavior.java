package dev.dubhe.anvilcraft.api.anvil.impl;

import dev.dubhe.anvilcraft.api.anvil.IAnvilBehavior;
import dev.dubhe.anvilcraft.api.event.anvil.AnvilFallOnLandEvent;
import dev.dubhe.anvilcraft.init.ModBlockTags;
import dev.dubhe.anvilcraft.mixin.VaultServerDataAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class ResetVaultBehavior implements IAnvilBehavior {
    @Override
    public boolean handle(
        Level level,
        BlockPos hitBlockPos,
        BlockState hitBlockState,
        float fallDistance,
        AnvilFallOnLandEvent event
    ) {
        if (!level.getBlockState(hitBlockPos).is(ModBlockTags.STORAGE_BLOCKS_LEAD)) return false;
        BlockPos vaultPos = hitBlockPos.below();
        level.setBlockAndUpdate(hitBlockPos, Blocks.AIR.defaultBlockState());
        Optional.ofNullable(level.getBlockEntity(vaultPos))
            .filter(VaultBlockEntity.class::isInstance)
            .map(VaultBlockEntity.class::cast)
            .map(VaultBlockEntity::getServerData)
            .ifPresent(vaultServerData -> {
                VaultServerDataAccessor vaultServerDataAccessor = (VaultServerDataAccessor) vaultServerData;
                vaultServerDataAccessor.getRewardedPlayers().clear();
                vaultServerDataAccessor.invoker$markChanged();
            });
        return false;
    }
}
