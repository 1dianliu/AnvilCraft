package dev.dubhe.anvilcraft.block;

import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.api.BlockPlaceAssist;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import dev.dubhe.anvilcraft.block.better.BetterBaseEntityBlock;
import dev.dubhe.anvilcraft.block.entity.InductionLightBlockEntity;
import dev.dubhe.anvilcraft.block.state.LightColor;
import dev.dubhe.anvilcraft.init.ModBlockEntities;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class InductionLightBlock extends BetterBaseEntityBlock implements IHammerRemovable, SimpleWaterloggedBlock {

    public static final VoxelShape SHAPE_X = Block.box(0, 6, 6, 16, 10, 10);
    public static final VoxelShape SHAPE_Y = Block.box(6, 0, 6, 10, 16, 10);
    public static final VoxelShape SHAPE_Z = Block.box(6, 6, 0, 10, 10, 16);

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty OVERLOAD = IPowerComponent.OVERLOAD;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<LightColor> COLOR = EnumProperty.create("color", LightColor.class);

    /**
     *
     */
    public InductionLightBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition
            .any()
            .setValue(POWERED, false)
            .setValue(OVERLOAD, true)
            .setValue(AXIS, Direction.Axis.Y)
            .setValue(WATERLOGGED, false)
            .setValue(COLOR, LightColor.PRIMARY));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(InductionLightBlock::new);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override

    public VoxelShape getShape(
        BlockState state,
        BlockGetter level,
        BlockPos pos,
        CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case Y -> SHAPE_Y;
            case Z -> SHAPE_Z;
            case X -> SHAPE_X;
        };
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return false;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
            .setValue(POWERED, false)
            .setValue(OVERLOAD, true)
            .setValue(AXIS, context.getClickedFace().getAxis())
            .setValue(
                WATERLOGGED,
                context.getLevel().getFluidState(context.getClickedPos())
                    .getType() == Fluids.WATER
            )
            .setValue(COLOR, LightColor.PRIMARY);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return InductionLightBlockEntity.createBlockEntity(ModBlockEntities.INDUCTION_LIGHT.get(), pos, state);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED).add(OVERLOAD).add(AXIS).add(WATERLOGGED).add(COLOR);
    }

    @Override
    public InteractionResult use(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.is(ModBlocks.INDUCTION_LIGHT.asItem())) {
            return BlockPlaceAssist.tryPlace(
                state,
                level,
                pos,
                player,
                hand,
                hit,
                ModBlocks.INDUCTION_LIGHT.asItem(),
                AXIS,
                ModBlocks.INDUCTION_LIGHT.getDefaultState()
            );
        } else if (itemInHand.is(Items.REDSTONE)) {
            level.setBlockAndUpdate(pos, state.setValue(COLOR, LightColor.PINK));
            return InteractionResult.SUCCESS;
        } else if (itemInHand.is(Items.GLOWSTONE_DUST)) {
            level.setBlockAndUpdate(pos, state.setValue(COLOR, LightColor.YELLOW));
            return InteractionResult.SUCCESS;
        } else if (itemInHand.is(ItemTags.AXES)) {
            level.setBlockAndUpdate(pos, state.setValue(COLOR, LightColor.PRIMARY));
            itemInHand.hurtAndBreak(1, (ServerLevel) level, (ServerPlayer) player, item -> {
                player.onEquippedItemBroken(item, LivingEntity.getSlotForHand(hand));
            });
            return InteractionResult.CONSUME_PARTIAL;
        } else if (itemInHand.is(ModItems.VOID_MATTER.asItem())) {
            level.setBlockAndUpdate(pos, state.setValue(COLOR, LightColor.DARK));
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }
        return createTickerHelper(
            type,
            ModBlockEntities.INDUCTION_LIGHT.get(),
            (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1));
    }

    @Override
    public void neighborChanged(
        BlockState state,
        Level level,
        BlockPos pos,
        Block neighborBlock,
        BlockPos neighborPos,
        boolean movedByPiston) {
        if (level.isClientSide) {
            return;
        }
        level.setBlock(pos, state.setValue(POWERED, level.hasNeighborSignal(pos)), 2);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public void tick(
        BlockState state,
        ServerLevel level,
        BlockPos pos,
        RandomSource random
    ) {
        if (state.getValue(POWERED) && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(POWERED), 2);
        }
    }
}
