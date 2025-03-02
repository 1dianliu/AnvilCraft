package dev.dubhe.anvilcraft.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.IonoCraftBackpackItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class IonoCraftBackpackHUD {
    public static void render(GuiGraphics guiGraphics, DeltaTracker partialTick) {
        if (!AnvilCraft.config.ionoCraftBackpackHud.enabled) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        ItemStack itemStack = IonoCraftBackpackItem.getByPlayer(player);
        if (!itemStack.is(ModItems.IONOCRAFT_BACKPACK)) {
            return;
        }
        int flightTime = IonoCraftBackpackItem.getFlightTime(itemStack);
        int percent = Math.round((float) flightTime / AnvilCraft.config.ionoCraftBackpackMaxFlightTime * 100);

        Font font = mc.font;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();

        int x = AnvilCraft.config.ionoCraftBackpackHud.hudX;
        int y = AnvilCraft.config.ionoCraftBackpackHud.hudY;
        float scale = AnvilCraft.config.ionoCraftBackpackHud.hudScale;
        poseStack.scale(scale, scale, scale);

        Component text = Component.translatable("hud.anvilcraft.ionocraft_backpack_power", percent);
        int textWidth = font.width(text);

        guiGraphics.drawString(font, text, x, y, 0xFFFFFFFF, true);

        poseStack.popPose();
    }
}
