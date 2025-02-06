package dev.dubhe.anvilcraft.block;

import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import dev.dubhe.anvilcraft.block.entity.AccelerationRingBlockEntity;
import dev.dubhe.anvilcraft.block.multipart.AbstractStateAddableMultiplePartBlock;
import dev.dubhe.anvilcraft.block.state.DirectionCube3x3PartHalf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class AccelerationRingBlock extends AbstractStateAddableMultiplePartBlock<DirectionCube3x3PartHalf, DirectionProperty, Direction> implements EntityBlock {
    public static final EnumProperty<DirectionCube3x3PartHalf> HALF = EnumProperty.create("half", DirectionCube3x3PartHalf.class);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty OVERLOAD = IPowerComponent.OVERLOAD;
    public static final EnumProperty<IPowerComponent.Switch> SWITCH = IPowerComponent.SWITCH;

    public AccelerationRingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition
                .any()
                .setValue(HALF, DirectionCube3x3PartHalf.BOTTOM_CENTER)
                .setValue(FACING, Direction.NORTH)
                .setValue(OVERLOAD, true)
                .setValue(SWITCH, IPowerComponent.Switch.ON));
    }

    @Override
    public final void setPlacedBy(
            @NotNull Level level,
            @NotNull BlockPos pos,
            BlockState state,
            @Nullable LivingEntity placer,
            @NotNull ItemStack stack
    ) {
        if (!state.hasProperty(this.getPart())) return;
        for (DirectionCube3x3PartHalf part : this.getParts()) {
            BlockPos blockPos = pos.offset(part.getOffset(state.getValue(getAdditionalProperty())));
            if (pos.equals(blockPos)) continue;
            BlockState newState = placer == null ? placedState(part, state) :
                    placedState(part, state).setValue(FACING, Direction.orderedByNearest(placer)[0]);
            level.setBlockAndUpdate(blockPos, newState);
        }
    }

    @Override
    public Property<DirectionCube3x3PartHalf> getPart() {
        return HALF;
    }

    @Override
    public DirectionCube3x3PartHalf[] getParts() {
        return DirectionCube3x3PartHalf.values();
    }

    @Override
    public DirectionProperty getAdditionalProperty() {
        return FACING;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING, OVERLOAD, SWITCH);
    }

    @Override
    protected BlockState placedState(DirectionCube3x3PartHalf part, BlockState state) {
        return state
                .setValue(this.getPart(), part);
    }

    @Override
    public @Nullable BlockState getPlacementState(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public void neighborChanged(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Block neighborBlock,
            @NotNull BlockPos neighborPos,
            boolean movedByPiston
    ) {
        boolean isSignal = Arrays.stream(getParts()).anyMatch(it -> level.hasNeighborSignal(pos.subtract(state.getValue(getPart()).getOffset()).offset(it.getOffset())));
        if (isSignal && state.getValue(SWITCH) == IPowerComponent.Switch.ON) {
            updateState(level, pos, SWITCH, IPowerComponent.Switch.OFF, 3);
        } else if (!isSignal && state.getValue(SWITCH) == IPowerComponent.Switch.OFF) {
            updateState(level, pos, SWITCH, IPowerComponent.Switch.ON, 3);
        }
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING).getAxis()) {
            case Z -> switch (state.getValue(HALF)) {
                case MID_CENTER, MID_S, MID_N -> Shapes.empty();
                default -> Shapes.block();
            };
            case X -> switch (state.getValue(HALF)) {
                case MID_CENTER, MID_E, MID_W -> Shapes.empty();
                default -> Shapes.block();
            };
            case Y -> switch (state.getValue(HALF)) {
                case BOTTOM_CENTER, MID_CENTER, TOP_CENTER -> Shapes.empty();
                default -> Shapes.block();
            };
        };
    }

    @Override
    protected @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new AccelerationRingBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return (level1, pos, state1, entity) -> {
            if (entity instanceof AccelerationRingBlockEntity be) be.tick();
        };
    }
}
