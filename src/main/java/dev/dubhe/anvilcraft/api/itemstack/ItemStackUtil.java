package dev.dubhe.anvilcraft.api.itemstack;

import net.minecraft.world.item.ItemStack;

public class ItemStackUtil {
    public static boolean isDefaultComponent(ItemStack stack) {
        ItemStack defaultDrop = new ItemStack(stack.getItem());
        return ItemStack.isSameItemSameComponents(defaultDrop, stack);
    }
}
