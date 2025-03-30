package dev.dubhe.anvilcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemHandlerUtil implements IItemHandler {
    /**
     * 获取指定容器/实体容器的物品处理器(IItemHandler)。
     *
     * @param filterEmpty 是否忽略空容器 从容器中抽取物品时需要传入true
     */
    @Nullable
    public static IItemHandler getTargetItemHandler(BlockPos inputBlockPos, Direction context, Level level, boolean filterEmpty) {
        if (level == null) return null;
        IItemHandler input = level.getCapability(
            Capabilities.ItemHandler.BLOCK,
            inputBlockPos,
            context
        );
        if (input != null && input.getSlots() != 0) {
            return input;
        }
        AABB aabb = new AABB(inputBlockPos);
        //获取实体容器 input时过滤掉空容器
        List<ContainerEntity> entities = level.getEntitiesOfClass(
                Entity.class, aabb, e -> e instanceof ContainerEntity && (!filterEmpty || !((ContainerEntity) e).isEmpty()))
            .stream()
            .map(it -> (ContainerEntity) it)
            .toList();
        if (!entities.isEmpty()) {
            input = ((Entity) entities.getFirst()).getCapability(
                Capabilities.ItemHandler.ENTITY,
                null
            );
        }
        return input;
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return null;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return null;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return false;
    }
}
