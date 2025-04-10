package dev.dubhe.anvilcraft.event;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.entity.fakeplayer.AnvilCraftBlockPlacerFakePlayer;
import dev.dubhe.anvilcraft.api.entity.player.AnvilCraftBlockPlacer;
import dev.dubhe.anvilcraft.api.world.load.LevelLoadManager;

import dev.dubhe.anvilcraft.block.entity.DeflectionRingBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = AnvilCraft.MOD_ID)
public class LevelEventListener {

    /**
     * 世界加载事件
     */
    @SubscribeEvent
    public static void onLevelLoad(@NotNull LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            AnvilCraftBlockPlacer.anvilCraftBlockPlacer = new AnvilCraftBlockPlacerFakePlayer(serverLevel);
        }
    }

    /**
     * 世界卸载事件
     */
    @SubscribeEvent
    public static void onLevelUnload(@NotNull LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            LevelLoadManager.removeAll(serverLevel);
            DeflectionRingBlockEntity.clear();
        }
    }
}
