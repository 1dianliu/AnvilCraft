package dev.dubhe.anvilcraft.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.neoforged.neoforge.items.IItemHandler;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemHandlerUtil {
    public static int countItemsInHandler(IItemHandler handler) {
        int count = 0;
        for (int i = 0; i < handler.getSlots(); i++) {
            count += handler.getStackInSlot(i).getCount();
        }
        return count;
    }
}
