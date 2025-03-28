package dev.dubhe.anvilcraft.api.power;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * 电力元件
 */
@SuppressWarnings("unused")
public interface IPowerComponent extends Comparable<IPowerComponent> {
    BooleanProperty OVERLOAD = BooleanProperty.create("overload");
    EnumProperty<Switch> SWITCH = EnumProperty.create("switch", Switch.class);

    Level getCurrentLevel();

    default void gridTick() {
    }

    /**
     * @return 元件位置
     */
    @NotNull BlockPos getPos();

    /**
     * @return 覆盖范围
     */
    default AABB getShape() {
        float range = getRange() * 2 + 1;
        return AABB.ofSize(getPos().getCenter(), range, range, range);
    }

    default int getRange() {
        return 0;
    }

    /**
     * 设置电网
     *
     * @param grid 电网
     */
    void setGrid(@Nullable PowerGrid grid);

    /**
     * 获取电网
     *
     * @return 电网
     */
    @Nullable PowerGrid getGrid();

    /**
     * @return 元件类型
     */
    @NotNull PowerComponentType getComponentType();

    enum Switch implements StringRepresentable {
        ON("on"),
        OFF("off");
        private final String name;

        Switch(String name) {
            this.name = name;
        }

        public @NotNull String toString() {
            return this.getSerializedName();
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    /**
     * @param level 世界
     * @param pos   位置
     */
    default void flushState(@NotNull Level level, @NotNull BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.hasProperty(OVERLOAD)) return;
        if (this.getGrid() == null) {
            if (!state.getValue(OVERLOAD)) {
                level.setBlockAndUpdate(pos, state.setValue(OVERLOAD, true));
            }
            return;
        }
        if (this.getGrid().isWorking() && state.getValue(OVERLOAD)) {
            level.setBlockAndUpdate(pos, state.setValue(OVERLOAD, false));
        } else if (!this.getGrid().isWorking() && !state.getValue(OVERLOAD)) {
            level.setBlockAndUpdate(pos, state.setValue(OVERLOAD, true));
        }
    }

    @Override
    default int compareTo(@NotNull IPowerComponent iPowerComponent) {
        if (this.equals(iPowerComponent)) return 0;
        int i = getComponentType().compareTo(iPowerComponent.getComponentType());
        return i == 0 ? 1 : i;
    }

    default boolean isGridWorking() {
        return Optional.ofNullable(this.getGrid()).map(PowerGrid::isWorking).orElse(false);
    }
}
