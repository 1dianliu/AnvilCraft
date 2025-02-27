package dev.dubhe.anvilcraft.client;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.power.SimplePowerGrid;

import dev.dubhe.anvilcraft.client.init.ModRenderTargets;
import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import dev.dubhe.anvilcraft.client.renderer.RenderState;
import dev.dubhe.anvilcraft.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PowerGridClient {
    private static final Map<Integer, SimplePowerGrid> GRID_MAP = Collections.synchronizedMap(new HashMap<>());

    public static Map<Integer, SimplePowerGrid> getGridMap() {
        return PowerGridClient.GRID_MAP;
    }

    /**
     * 渲染
     */
    public static void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 camera) {
        if (Minecraft.getInstance().level == null) return;
        RandomSource random = Minecraft.getInstance().level.random;
        String level = Minecraft.getInstance().level.dimension().location().toString();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
        for (SimplePowerGrid grid : PowerGridClient.GRID_MAP.values()) {
            if (!grid.shouldRender(camera)) continue;
            if (!grid.getLevel().equals(level)) continue;
            VoxelShape shape = grid.getCachedOutlineShape();
            int[] rgb = grid.getColor();
            if (shape == null) continue;
            PowerGridClient.renderOutline(
                poseStack,
                consumer,
                camera,
                grid.getPos(),
                shape,
                rgb[0] / 255f,
                rgb[1] / 255f,
                rgb[2] / 255f,
                0.4f
            );
        }
    }

    public static void renderEnhancedTransmitterLine(
        PoseStack poseStack,
        MultiBufferSource.BufferSource bufferSource,
        Vec3 camera
    ) {
        if (!RenderState.isEnhancedRenderingAvailable() || !RenderState.isBloomEffectEnabled()) return;
        if (!AnvilCraft.config.renderPowerTransmitterLines) return;
        if (Minecraft.getInstance().level == null) return;
        if (ModRenderTargets.getBloomTarget() != null) {
            ModRenderTargets.getBloomTarget().setClearColor(0, 0, 0, 0);
            ModRenderTargets.getBloomTarget().clear(Minecraft.ON_OSX);
            ModRenderTargets.getBloomTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        }
        String level = Minecraft.getInstance().level.dimension().location().toString();

        VertexConsumer consumer1 = bufferSource.getBuffer(ModRenderTypes.LINE_BLOOM);
        for (SimplePowerGrid grid : PowerGridClient.GRID_MAP.values()) {
            if (!grid.shouldRender(camera)) continue;
            if (!grid.getLevel().equals(level)) continue;
            grid.getPowerTransmitterLines().forEach(it -> it.render(poseStack, consumer1, camera, 0x9966ccff));
        }
        bufferSource.endBatch();
    }

    public static void renderTransmitterLine(
        PoseStack poseStack,
        MultiBufferSource.BufferSource bufferSource,
        Vec3 camera
    ) {
        if (RenderState.isEnhancedRenderingAvailable() && RenderState.isBloomEffectEnabled()) return;
        if (!AnvilCraft.config.renderPowerTransmitterLines) return;
        if (Minecraft.getInstance().level == null) return;
        String level = Minecraft.getInstance().level.dimension().location().toString();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.LINES);
        for (SimplePowerGrid grid : PowerGridClient.GRID_MAP.values()) {
            if (!grid.shouldRender(camera)) continue;
            if (!grid.getLevel().equals(level)) continue;
            grid.getPowerTransmitterLines().forEach(it -> it.render(poseStack, consumer, camera, 0x9966ccff));
        }
    }

    public static void clearAllGrid() {
        SimplePowerGrid.recreateExecutor();
        for (SimplePowerGrid value : GRID_MAP.values()) {
            value.destroy();
        }
        GRID_MAP.clear();
    }

    @SuppressWarnings("SameParameterValue")
    private static void renderOutline(
        PoseStack poseStack,
        VertexConsumer consumer,
        Vec3 camera,
        @NotNull BlockPos pos,
        @NotNull VoxelShape shape,
        float red,
        float green,
        float blue,
        float alpha) {
        PowerGridClient.renderShape(
            poseStack,
            consumer,
            shape,
            (double) pos.getX() - camera.x,
            (double) pos.getY() - camera.y,
            (double) pos.getZ() - camera.z,
            red,
            green,
            blue,
            alpha
        );
    }

    private static void renderShape(
        @NotNull PoseStack poseStack,
        VertexConsumer consumer,
        @NotNull VoxelShape shape,
        double x,
        double y,
        double z,
        float red,
        float green,
        float blue,
        float alpha) {
        PoseStack.Pose pose = poseStack.last();
        shape.forAllEdges((minX, minY, minZ, maxX, maxY, maxZ) -> {
            float dx = (float) (maxX - minX);
            float dy = (float) (maxY - minY);
            float dz = (float) (maxZ - minZ);
            float distance = Mth.sqrt(dx * dx + dy * dy + dz * dz);
            consumer.addVertex(pose.pose(), (float) (minX + x), (float) (minY + y), (float) (minZ + z))
                .setColor(red, green, blue, alpha)
                .setNormal(pose.copy(), dx /= distance, dy /= distance, dz /= distance);
            consumer.addVertex(pose.pose(), (float) (maxX + x), (float) (maxY + y), (float) (maxZ + z))
                .setColor(red, green, blue, alpha)
                .setNormal(pose.copy(), dx, dy, dz);
        });
    }
}
