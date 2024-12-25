package dev.dubhe.anvilcraft.block.entity;

import dev.dubhe.anvilcraft.block.RubyPrismBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

public class RubyPrismBlockEntity extends BaseLaserBlockEntity {
    private boolean enabled = false;

    private RubyPrismBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static @NotNull RubyPrismBlockEntity createBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState
    ) {
        return new RubyPrismBlockEntity(type, pos, blockState);
    }

    public void tick(@NotNull Level level) {
        if (enabled) {
            emitLaser(getFacing());
        }
        if (laserLevel == 0) {
            enabled = false;
        }
        super.tick(level);
        resetState();
    }

    @Override
    protected int getBaseLaserLevel() {
        return 0;
    }

    @Override
    public void onCancelingIrradiation(BaseLaserBlockEntity baseLaserBlockEntity) {
        enabled = false;
        super.onCancelingIrradiation(baseLaserBlockEntity);
    }

    @Override
    public void onIrradiated(BaseLaserBlockEntity baseLaserBlockEntity) {
        enabled = true;
        super.onIrradiated(baseLaserBlockEntity);
    }

    @Override
    public Direction getFacing() {
        return getBlockState().getValue(RubyPrismBlock.FACING);
    }
}
