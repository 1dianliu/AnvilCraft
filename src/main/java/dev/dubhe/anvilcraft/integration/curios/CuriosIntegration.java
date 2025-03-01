package dev.dubhe.anvilcraft.integration.curios;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.integration.Integration;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.AnvilHammerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

@Integration("curios")
public class CuriosIntegration {
    public void apply() {
        AnvilCraft.MOD_BUS.addListener(this::setup);
        AnvilCraft.MOD_BUS.addListener(this::onLayerRegister);
    }

    private void setup(FMLCommonSetupEvent event) {
        AnvilHammerItem.addIsWearingPredicate(player ->
            CuriosApi.getCuriosInventory(player).map(this::isWearing).orElse(false)
        );
        CuriosApi.registerCurio(ModItems.ANVIL_HAMMER.get(), new GogglesCurioItem());
        CuriosApi.registerCurio(ModItems.ROYAL_ANVIL_HAMMER.get(), new GogglesCurioItem());
        CuriosApi.registerCurio(ModItems.EMBER_ANVIL_HAMMER.get(), new GogglesCurioItem());
    }

    private boolean isWearing(ICuriosItemHandler itemHandler) {
        return !itemHandler.findCurios(it -> it.getItem() instanceof AnvilHammerItem).isEmpty();
    }

    public void applyClient() {
        CuriosRendererRegistry.register(
            ModItems.ANVIL_HAMMER.get(),
            () -> new GogglesCurioRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(GogglesCurioRenderer.LAYER))
        );
        CuriosRendererRegistry.register(
            ModItems.ROYAL_ANVIL_HAMMER.get(),
            () -> new GogglesCurioRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(GogglesCurioRenderer.LAYER))
        );
        CuriosRendererRegistry.register(
            ModItems.EMBER_ANVIL_HAMMER.get(),
            () -> new GogglesCurioRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(GogglesCurioRenderer.LAYER))
        );
    }

    public void onLayerRegister(final EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
            GogglesCurioRenderer.LAYER,
            () -> LayerDefinition.create(GogglesCurioRenderer.mesh(), 1, 1)
        );
    }
}
