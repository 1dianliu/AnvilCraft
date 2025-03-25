package dev.dubhe.anvilcraft.api.itemhandler;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.function.Predicate;

public class ItemHandlerUtil {
    public static boolean exportToTarget(
        IItemHandler source,
        int maxAmount,
        Predicate<ItemStack> predicate,
        IItemHandler target
    ) {
        boolean success = false;
        for (int srcIndex = 0; srcIndex < source.getSlots(); srcIndex++) {
            if (maxAmount <= 0) break;
            ItemStack sourceStack = source.extractItem(srcIndex, maxAmount, true);
            if (sourceStack.isEmpty() || !predicate.test(sourceStack)) {
                continue;
            }
            ItemStack remainder = ItemHandlerHelper.insertItem(target, sourceStack, true);
            int amountToInsert = sourceStack.getCount() - remainder.getCount();
            if (amountToInsert > 0) {
                sourceStack = source.extractItem(srcIndex, amountToInsert, false);
                ItemHandlerHelper.insertItem(target, sourceStack, false);
                maxAmount -= amountToInsert;
                success = true;
            }
        }
        return success;
    }

    public static boolean importFromTarget(
        IItemHandler target,
        int maxAmount,
        Predicate<ItemStack> predicate,
        IItemHandler source
    ) {
        boolean success = false;
        for (int srcIndex = 0; srcIndex < source.getSlots() && maxAmount > 0; srcIndex++) {
            ItemStack sourceStack = source.extractItem(srcIndex, maxAmount, true);
            if (sourceStack.isEmpty() || !predicate.test(sourceStack)) {
                continue;
            }
            ItemStack remainder = ItemHandlerHelper.insertItem(target, sourceStack, true);
            int amountToInsert = sourceStack.getCount() - remainder.getCount();
            if (amountToInsert > 0) {
                sourceStack = source.extractItem(srcIndex, amountToInsert, false);
                ItemHandlerHelper.insertItem(target, sourceStack, false);
                maxAmount -= amountToInsert;
                success = true;
            }
        }
        return success;
    }

    public static int countItemsInHandler(IItemHandler handler) {
        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            count += handler.getStackInSlot(i).getCount();
        }
        return count;
    }

    public static Object2IntMap<Item> mergeHandlerItems(IItemHandler handler) {
        Object2IntMap<Item> items = new Object2IntOpenHashMap<>();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            items.mergeInt(stack.getItem(), stack.getCount(), Integer::sum);
        }
        return items;
    }
}
