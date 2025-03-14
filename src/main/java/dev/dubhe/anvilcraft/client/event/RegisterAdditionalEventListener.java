package dev.dubhe.anvilcraft.client.event;

import dev.dubhe.anvilcraft.AnvilCraft;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = AnvilCraft.MOD_ID)
public class RegisterAdditionalEventListener {

    /**
     * 注册模型
     */
    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(AnvilCraft.of("item/crab_claw_holding_block")));
        event.register(ModelResourceLocation.standalone(AnvilCraft.of("item/crab_claw_holding_item")));
        event.register(ModelResourceLocation.standalone(AnvilCraft.of("block/heliostats_head")));
        event.register(ModelResourceLocation.standalone(AnvilCraft.of("block/heliostats_head_sunflower")));
        event.register(ModelResourceLocation.standalone(AnvilCraft.of("block/creative_generator_cube")));
        event.register(ModelResourceLocation.standalone(AnvilCraft.of("block/charge_collector_cube")));
        event.register(ModelResourceLocation.standalone(AnvilCraft.of("block/void_energy_collector_head")));
        event.register(ModelResourceLocation.standalone(AnvilCraft.of("block/laser")));
        event.register(ModelResourceLocation.standalone(AnvilCraft.of("block/axis")));
    }
}
