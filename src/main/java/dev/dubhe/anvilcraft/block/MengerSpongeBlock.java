package dev.dubhe.anvilcraft.block;

import dev.dubhe.anvilcraft.init.ModFluidTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MengerSpongeBlock extends SpongeBlock {

    private static final VoxelShape REDUCE_AABB = Stream.of(
            Block.box(5.325, 0, 5.325, 10.675, 16, 10.675),
            Block.box(0, 5.325, 5.325, 16, 10.675, 10.675),
            Block.box(5.325, 5.325, 0, 10.675, 10.675, 16))
        .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR))
        .get();

    private static final VoxelShape AABB = Shapes.join(Shapes.block(), REDUCE_AABB, BooleanOp.ONLY_FIRST);

    private static final Direction[] ALL_DIRECTIONS = Direction.values();

    public MengerSpongeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected void tryAbsorbWater(Level level, BlockPos pos) {
        if (this.removeFluidBreadthFirstSearch(level, pos)) {
            level.playSound(
                null,
                pos,
                SoundEvents.SPONGE_ABSORB,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
            );
        }
    }

    private boolean removeFluidBreadthFirstSearch(Level level, BlockPos pos) {
        return BlockPos.breadthFirstTraversal(
            pos,
            6,
            65,
            (posx, consumer) -> {
                for (Direction direction : ALL_DIRECTIONS) {
                    consumer.accept(posx.relative(direction));
                }
            },
            (checkedPos) -> {
                if (checkedPos.equals(pos)) {
                    return true;
                }
                BlockState blockState = level.getBlockState(checkedPos);
                FluidState fluidState = level.getFluidState(checkedPos);
                if (!fluidState.is(ModFluidTags.MENGER_SPONGE_CAN_ABSORB)) {
                    return false;
                }
                Block block = blockState.getBlock();
                if (block instanceof BucketPickup bucketPickup) {
                    if (!bucketPickup
                        .pickupBlock(null, level, checkedPos, blockState)
                        .isEmpty()) {
                        return true;
                    }
                }

                if (blockState.getBlock() instanceof LiquidBlock) {
                    level.setBlock(checkedPos, Blocks.AIR.defaultBlockState(), 3);
                } else {
                    if (!blockState.is(Blocks.KELP)
                        && !blockState.is(Blocks.KELP_PLANT)
                        && !blockState.is(Blocks.SEAGRASS)
                        && !blockState.is(Blocks.TALL_SEAGRASS)) {
                        return false;
                    }

                    BlockEntity blockEntity =
                        blockState.hasBlockEntity() ? level.getBlockEntity(checkedPos) : null;
                    dropResources(blockState, level, checkedPos, blockEntity);
                    level.setBlock(checkedPos, Blocks.AIR.defaultBlockState(), 3);
                }
                return true;
            }
        ) > 1;
    }


    @Override
    public VoxelShape getInteractionShape(
        BlockState state, BlockGetter level, BlockPos pos) {
        return REDUCE_AABB;
    }


    @Override
    public VoxelShape getShape(
        BlockState state,
        BlockGetter level,
        BlockPos pos,
        CollisionContext context) {
        return AABB;
    }
}
