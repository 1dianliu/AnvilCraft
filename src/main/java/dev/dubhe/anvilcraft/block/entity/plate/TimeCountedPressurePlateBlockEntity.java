package dev.dubhe.anvilcraft.block.entity.plate;

import dev.dubhe.anvilcraft.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimeCountedPressurePlateBlockEntity extends BlockEntity {
    private int needTick;
    private int tick = 0;

    protected TimeCountedPressurePlateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int needTick) {
        super(type, pos, blockState);
        this.needTick = needTick;
    }

    public TimeCountedPressurePlateBlockEntity(BlockPos pos, BlockState blockState, int needTick) {
        this(ModBlockEntities.TIME_COUNTED_PRESSURE_PLATE.get(), pos, blockState, needTick);
    }

    public static @NotNull TimeCountedPressurePlateBlockEntity createBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        return new TimeCountedPressurePlateBlockEntity(type, pos, blockState, 10);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("tick", this.tick);
        tag.putInt("NeedTick", this.needTick);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        this.tick = tag.getInt("tick");
        this.needTick = tag.getInt("NeedTick");
    }

    public int getSignalStrength() {
        return Math.clamp(tick / (needTick == 0 ? 1 : needTick), 0, 15);
    }

    public void tick(@NotNull Level level, BlockPos pos) {
        List<LivingEntity> entities = level.getEntities(EntityTypeTest.forClass(LivingEntity.class), new AABB(pos), entity -> true);
        if (!entities.isEmpty()) tick++;
        else if (tick > 0) tick--;
    }
}
