package dev.dubhe.anvilcraft.block;

import dev.dubhe.anvilcraft.init.ModBlockEntities;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResentfulAmberBlock extends MobAmberBlock {
    public ResentfulAmberBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModBlockEntities.RESENTFUL_AMBER_BLOCK.create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (!level.isClientSide) {
            return null;
        }
        return createTickerHelper(
            type,
            ModBlockEntities.RESENTFUL_AMBER_BLOCK.get(),
            (level1, blockPos, blockState, blockEntity) -> blockEntity.clientTick((ClientLevel) level1, blockPos)
        );
    }
}
