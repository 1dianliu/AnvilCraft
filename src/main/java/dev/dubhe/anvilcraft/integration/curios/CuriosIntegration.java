package dev.dubhe.anvilcraft.integration.curios;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.integration.Integration;
import dev.dubhe.anvilcraft.client.init.ModModelLayers;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.integration.curios.item.GogglesCurioItem;
import dev.dubhe.anvilcraft.integration.curios.item.IonoCraftBackpackCurioItem;
import dev.dubhe.anvilcraft.integration.curios.renderer.GogglesCurioRenderer;
import dev.dubhe.anvilcraft.integration.curios.renderer.IonoCraftBackpackCurioRenderer;
import dev.dubhe.anvilcraft.item.AnvilHammerItem;
import dev.dubhe.anvilcraft.item.IonoCraftBackpackItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;

@Integration("curios")
public class CuriosIntegration {
    public void apply() {
        AnvilCraft.MOD_BUS.addListener(this::setup);
        AnvilCraft.MOD_BUS.addListener(this::onLayerRegister);
    }

    private void setup(FMLCommonSetupEvent event) {
        AnvilHammerItem.addIsWearingPredicate(player ->
            CuriosApi.getCuriosInventory(player).map(this::isAnvilHammerWearing).orElse(false)
        );
        CuriosApi.registerCurio(ModItems.ANVIL_HAMMER.get(), new GogglesCurioItem());
        CuriosApi.registerCurio(ModItems.ROYAL_ANVIL_HAMMER.get(), new GogglesCurioItem());
        CuriosApi.registerCurio(ModItems.EMBER_ANVIL_HAMMER.get(), new GogglesCurioItem());
        IonoCraftBackpackItem.addStackProvider(player ->
            CuriosApi.getCuriosInventory(player).map(this::getIonoCraftBackpackWearing).orElse(ItemStack.EMPTY)
        );
        CuriosApi.registerCurio(ModItems.IONOCRAFT_BACKPACK.get(), new IonoCraftBackpackCurioItem());
    }

    private boolean isAnvilHammerWearing(ICuriosItemHandler itemHandler) {
        return !itemHandler.findCurios(it -> it.getItem() instanceof AnvilHammerItem).isEmpty();
    }

    private ItemStack getIonoCraftBackpackWearing(ICuriosItemHandler itemHandler) {
        List<SlotResult> curios = itemHandler.findCurios(it -> it.getItem() instanceof IonoCraftBackpackItem);
        if (!curios.isEmpty()) {
            return curios.getFirst().stack();
        }
        return ItemStack.EMPTY;
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
        CuriosRendererRegistry.register(
            ModItems.IONOCRAFT_BACKPACK.get(),
            () -> new IonoCraftBackpackCurioRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(ModModelLayers.IONOCRAFT_BACKPACK))
        );
    }

    public void onLayerRegister(final EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
            GogglesCurioRenderer.LAYER,
            () -> LayerDefinition.create(GogglesCurioRenderer.mesh(), 1, 1)
        );
    }
}
