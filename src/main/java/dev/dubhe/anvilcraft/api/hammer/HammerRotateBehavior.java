package dev.dubhe.anvilcraft.api.hammer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

/**
 * 可被锤子改变的方块
 */
@SuppressWarnings("unused")
public interface HammerRotateBehavior extends IHammerChangeable {
    DirectionProperty FACING_HOPPER = BlockStateProperties.FACING_HOPPER;
    DirectionProperty FACING = BlockStateProperties.FACING;
    DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    HammerRotateBehavior DEFAULT = new HammerRotateBehavior() {
    };
    HammerRotateBehavior EMPTY = new HammerRotateBehavior() {
        public boolean change(Player player, BlockPos blockPos, @NotNull Level level, ItemStack anvilHammer) {
            return false;
        }
    };

    @Override
    default boolean change(Player player, BlockPos blockPos, @NotNull Level level, ItemStack anvilHammer) {
        BlockState state = level.getBlockState(blockPos);
        if (state.hasProperty(FACING)) {
            state = HammerRotateBehavior.rotate(state);
        } else {
            if (state.hasProperty(FACING_HOPPER)) {
                state = HammerRotateBehavior.hopperRotate(state);
            } else {
                if (state.hasProperty(HORIZONTAL_FACING)) {
                    state = HammerRotateBehavior.horizontalRotate(state);
                }
            }
        }
        level.setBlockAndUpdate(blockPos, state);
        return true;
    }

    private static @NotNull BlockState rotate(@NotNull BlockState state) {
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case WEST -> state.setValue(FACING, Direction.UP);
            case UP -> state.setValue(FACING, Direction.DOWN);
            case DOWN -> state.setValue(FACING, Direction.NORTH);
            default -> state.setValue(FACING, direction.getClockWise());
        };
    }

    private static @NotNull BlockState hopperRotate(@NotNull BlockState state) {
        Direction direction = state.getValue(FACING_HOPPER);
        return switch (direction) {
            case WEST -> state.setValue(FACING_HOPPER, Direction.DOWN);
            case DOWN -> state.setValue(FACING_HOPPER, Direction.NORTH);
            default -> state.setValue(FACING_HOPPER, direction.getClockWise());
        };
    }

    @SuppressWarnings("SameParameterValue")
    private static @NotNull BlockState horizontalRotate(@NotNull BlockState state) {
        return state.setValue(
            HORIZONTAL_FACING,
            state.getValue(HORIZONTAL_FACING).getClockWise()
        );
    }

    @Override
    default Property<?> getChangeableProperty(BlockState state) {
        if (state.hasProperty(FACING)) {
            return FACING;
        } else if (state.hasProperty(FACING_HOPPER)) {
            return FACING_HOPPER;
        } else if (state.hasProperty(HORIZONTAL_FACING)) {
            return HORIZONTAL_FACING;
        }
        return null;
    }
}
