package dev.dubhe.anvilcraft.block;

import dev.dubhe.anvilcraft.api.IHasMultiBlock;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import dev.dubhe.anvilcraft.block.entity.OverseerBlockEntity;
import dev.dubhe.anvilcraft.block.multipart.SimpleMultiPartBlock;
import dev.dubhe.anvilcraft.block.state.Vertical3PartHalf;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OverseerBlock
    extends SimpleMultiPartBlock<Vertical3PartHalf>
    implements IHammerRemovable, IHasMultiBlock, EntityBlock {
    private static final VoxelShape OVERSEER_BASE =
        Shapes.or(Block.box(0, 0, 0, 16, 4, 16), Block.box(2, 8, 2, 14, 16, 14));
    private static final VoxelShape OVERSEER_MID = Block.box(2, 0, 2, 14, 16, 14);
    private static final VoxelShape OVERSEER_TOP = Block.box(2, 0, 2, 14, 16, 14);
    public static final EnumProperty<Vertical3PartHalf> HALF = EnumProperty.create("half", Vertical3PartHalf.class);
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 3);

    /**
     * @param properties 属性
     */
    public OverseerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition
            .any()
            .setValue(HALF, Vertical3PartHalf.BOTTOM)
            .setValue(LEVEL, 0)
        );
    }

    @Override
    public Property<Vertical3PartHalf> getPart() {
        return OverseerBlock.HALF;
    }

    @Override
    public Vertical3PartHalf[] getParts() {
        return Vertical3PartHalf.values();
    }

    @Override
    @Nullable
    public BlockState getPlacementState(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HALF, Vertical3PartHalf.BOTTOM).setValue(LEVEL, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF).add(LEVEL);
    }


    @Override
    public @Nonnull RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }


    @Override
    public VoxelShape getShape(
        BlockState state,
        BlockGetter level,
        BlockPos pos,
        CollisionContext context
    ) {
        return switch (state.getValue(HALF)) {
            case TOP -> OVERSEER_TOP;
            case MID -> OVERSEER_MID;
            case BOTTOM -> OVERSEER_BASE;
        };
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected BlockState placedState(Vertical3PartHalf part, BlockState state) {
        return super.placedState(part, state).setValue(LEVEL, 1);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OverseerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        if (state.getValue(HALF) != Vertical3PartHalf.BOTTOM) return null;
        return (level1, pos, state1, entity) -> {
            if (entity instanceof OverseerBlockEntity be) be.tick(level1, pos, state1);
        };
    }

    @Override
    public void onRemove(Level level, BlockPos pos, BlockState state) {
    }

    @Override
    public void onPlace(Level level, BlockPos pos, BlockState state) {
    }
}
