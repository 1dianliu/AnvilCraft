package dev.dubhe.anvilcraft.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.inventory.JewelCraftingMenu;
import dev.dubhe.anvilcraft.inventory.component.jewel.JewelInputSlot;
import dev.dubhe.anvilcraft.util.RenderHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class JewelCraftingScreen extends AbstractContainerScreen<JewelCraftingMenu> {

    private static final ResourceLocation CONTAINER_LOCATION = AnvilCraft.of("textures/gui/container/jewelcrafting/background.png");


    public JewelCraftingScreen(JewelCraftingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(CONTAINER_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderHintItemSlot(guiGraphics);
    }

    private void renderHintItemSlot(GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(leftPos, topPos, 0);
        for (int i = JewelCraftingMenu.CRAFT_SLOT_START; i <= JewelCraftingMenu.CRAFT_SLOT_END; i++) {
            Slot slot = menu.getSlot(i);
            if (!slot.hasItem() && slot instanceof JewelInputSlot inputSlot) {
                int count = inputSlot.getHintCount();
                ItemStack @Nullable [] ingredientItems = inputSlot.getIngredientItems();
                if (ingredientItems != null) {
                    int index = (int) ((System.currentTimeMillis() / 1000) % ingredientItems.length);
                    ItemStack stack = ingredientItems[index];
                    RenderHelper.renderItemWithTransparency(stack, poseStack, slot.x, slot.y, 0.52f);
                    guiGraphics.renderItemDecorations(font, stack.copyWithCount(count), slot.x, slot.y);
                }
            }
        }
        poseStack.popPose();
    }

    @Override
    protected void renderSlotHighlight(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, float partialTick) {
        super.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null) {
            ItemStack itemstack = null;
            if (this.hoveredSlot.hasItem()) {
                itemstack = this.hoveredSlot.getItem();
            } else if (this.hoveredSlot instanceof JewelInputSlot inputSlot) {
                ItemStack @Nullable [] ingredientItems = inputSlot.getIngredientItems();
                if (ingredientItems != null) {
                    int index = (int) ((System.currentTimeMillis() / 1000) % ingredientItems.length);
                    itemstack = ingredientItems[index];
                }
            }
            if (itemstack != null) {
                guiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, x, y);
            }
        }
    }

    @Override
    protected void renderSlotContents(GuiGraphics guiGraphics, ItemStack itemstack, Slot slot, @Nullable String countString) {
        if (slot instanceof JewelInputSlot inputSlot) {
            if (itemstack.getCount() < inputSlot.getHintCount()) {
                int seed = slot.x + slot.y * imageWidth;
                if (slot.isFake()) {
                    guiGraphics.renderFakeItem(itemstack, slot.x, slot.y, seed);
                } else {
                    guiGraphics.renderItem(itemstack, slot.x, slot.y, seed);
                }
                if (!itemstack.isEmpty()) {
                    guiGraphics.pose().pushPose();
                    String s = String.valueOf(itemstack.getCount());
                    guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
                    guiGraphics.drawString(font, s, slot.x + 19 - 2 - font.width(s), slot.y + 6 + 3, 0xFFFF5555, true);
                    guiGraphics.pose().popPose();
                }
                return;
            }
        }
        super.renderSlotContents(guiGraphics, itemstack, slot, countString);
    }
}
