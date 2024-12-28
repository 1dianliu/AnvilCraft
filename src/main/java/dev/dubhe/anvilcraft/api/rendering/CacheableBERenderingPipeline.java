package dev.dubhe.anvilcraft.api.rendering;

import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import lombok.Getter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.ChunkPos;
import org.joml.Matrix4f;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class CacheableBERenderingPipeline {
    public static final RenderType[] SUPPORTED_RENDERTYPES = new RenderType[]{
        RenderType.solid(),
        ModRenderTypes.LASER
    };

    public static final RenderType[] BLOOM_RENDERTYPES = new RenderType[]{
        ModRenderTypes.LASER
    };

    @Getter
    private static CacheableBERenderingPipeline instance;
    private final ClientLevel level;
    private final Queue<Runnable> pendingCompiles = new ArrayDeque<>();
    private final Queue<Runnable> pendingUploads = new ArrayDeque<>();
    private final Map<ChunkPos, RenderRegion> renderRegions = new HashMap<>();
    private boolean valid = true;

    public RenderRegion getRenderRegion(ChunkPos chunkPos) {
        if (renderRegions.containsKey(chunkPos)) {
            return renderRegions.get(chunkPos);
        }
        RenderRegion renderRegion = new RenderRegion(chunkPos, this);
        renderRegions.put(chunkPos, renderRegion);
        return renderRegion;
    }

    public CacheableBERenderingPipeline(ClientLevel level) {
        this.level = level;
    }

    public void runTasks() {
        while (!pendingCompiles.isEmpty() && valid) {
            pendingCompiles.poll().run();
        }
        while (!pendingUploads.isEmpty() && valid) {
            pendingUploads.poll().run();
        }
    }

    public static void updateLevel(ClientLevel level) {
        if (instance != null) {
            instance.releaseBuffers();
        }
        instance = new CacheableBERenderingPipeline(level);
    }

    public void blockRemoved(CacheableBlockEntity be){
        ChunkPos chunkPos = new ChunkPos(be.getBlockPos());
        getRenderRegion(chunkPos).blockRemoved(be);
    }

    public void update(CacheableBlockEntity be){
        ChunkPos chunkPos = new ChunkPos(be.getBlockPos());
        getRenderRegion(chunkPos).update(be);
    }

    public void submitUploadTask(Runnable task) {
        pendingUploads.add(task);
    }

    public void submitCompileTask(Runnable task) {
        pendingCompiles.add(task);
    }

    public void releaseBuffers() {
        renderRegions.values().forEach(RenderRegion::releaseBuffers);
        valid = false;
    }

    public void renderBloomed(Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
        renderRegions.values().forEach(it -> it.renderBloomed(frustumMatrix, projectionMatrix));
    }

    public void render(Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
        renderRegions.values().forEach(it -> it.render(frustumMatrix, projectionMatrix));
    }
}
