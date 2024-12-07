package dev.dubhe.anvilcraft.client.gui.component;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.client.gui.screen.ActiveSilencerScreen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SilencerButton extends Button {

    private final ResourceLocation texture;

    @Getter
    private final int index;

    private final ActiveSilencerScreen parent;
    private final int variant;
    private final String textureVariant;

    /**
     * 主动静音器 screen 的按钮
     */
    public SilencerButton(
        int x,
        int y,
        int index,
        int variant,
        OnPress onPress,
        ActiveSilencerScreen parent,
        String textureVariant
    ) {
        super(
            x,
            y,
            10,
            10,
            Component.literal(""),
            onPress,
            (var) -> parent.getSoundTextAt(index, variant).copy()
        );
        this.textureVariant = textureVariant;
        this.height = 15;
        this.width = 112;
        this.index = index;
        texture = AnvilCraft.of("textures/gui/container/machine/active_silencer_button_%s.png".formatted(textureVariant));
        this.parent = parent;
        this.variant = variant;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        String searchText = parent.getFilterText();
        ResourceLocation soundId = parent.getSoundIdAt(index, variant);
        if (soundId == null) return;
        this.renderTexture(guiGraphics, texture, this.getX(), this.getY(), 0, 0, 15, this.width, this.height, 112, 30);
        Component message;
        if (searchText.startsWith("#") || searchText.startsWith("~")) {
            message = parent.getSoundTextAt(index, variant);
        } else {
            message = highlighted(
                parent.getSoundTextAt(index, variant).getString(),
                searchText,
                ChatFormatting.WHITE,
                ChatFormatting.YELLOW);
        }
        this.setMessage(message);
        this.renderString(guiGraphics, Minecraft.getInstance().font, 16777215 | Mth.ceil(this.alpha * 255.0F) << 24);
        if (this.isHovered()) {
            Component soundIdText = highlighted(
                soundId.toString(), searchText.replaceFirst("#", ""), ChatFormatting.GRAY, ChatFormatting.YELLOW);
            guiGraphics.renderTooltip(
                Minecraft.getInstance().font,
                List.of(message.getVisualOrderText(), soundIdText.getVisualOrderText()),
                mouseX,
                mouseY
            );
        }
    }

    private static Component highlighted(
        String original,
        String hightlighted,
        ChatFormatting originalFormatting,
        ChatFormatting highlightFormatting
    ) {
        try {
            String[] parts = original.split(Pattern.quote(hightlighted), -1);
            List<Component> components = new ArrayList<>();
            for (String s : parts) {
                components.add(Component.literal(s).copy().setStyle(Style.EMPTY.applyFormat(originalFormatting)));
            }
            return ComponentUtils.formatList(
                components, Component.literal(hightlighted).withStyle(highlightFormatting));
        } catch (Throwable e) {
            return Component.literal(original);
        }
    }

    public void renderTexture(
        @NotNull GuiGraphics guiGraphics,
        @NotNull ResourceLocation texture,
        int x,
        int y,
        int puOffset,
        int pvOffset,
        int textureDifference,
        int width,
        int height,
        int textureWidth,
        int textureHeight) {
        int i = pvOffset;
        if (this.isHovered()) {
            i += textureDifference;
        }
        RenderSystem.enableDepthTest();
        guiGraphics.blit(texture, x, y, puOffset, i, width, height, textureWidth, textureHeight);
    }
}
