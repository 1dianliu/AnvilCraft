package dev.dubhe.anvilcraft.api.power;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.client.renderer.Line;
import dev.dubhe.anvilcraft.client.PowerGridClient;

import dev.dubhe.anvilcraft.util.ColorUtil;
import dev.dubhe.anvilcraft.util.ShapeUtil;
import dev.dubhe.anvilcraft.util.ThreadFactoryImpl;
import dev.dubhe.anvilcraft.util.VirtualThreadFactoryImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Getter
public class SimplePowerGrid {
    private final Random random = new Random();
    private static ExecutorService EXECUTOR;
    private final int[] EMPTY = {};

    static {
        recreateExecutor();
    }

    public static final Codec<SimplePowerGrid> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.INT.fieldOf("hash").forGetter(o -> o.id),
            Codec.STRING.fieldOf("level").forGetter(o -> o.level),
            BlockPos.CODEC.fieldOf("pos").forGetter(o -> o.pos),
            PowerComponentInfo.CODEC
                .listOf()
                .fieldOf("powerComponentInfoList")
                .forGetter(it -> it.powerComponentInfoList),
            Codec.INT.fieldOf("generate").forGetter(o -> o.generate),
            Codec.INT.fieldOf("consume").forGetter(o -> o.consume))
        .apply(ins, SimplePowerGrid::new)
    );

    private final int id;
    private final String level;
    private final BlockPos pos;
    private final List<BlockPos> blocks = new ArrayList<>();
    private final List<PowerComponentInfo> powerComponentInfoList = new ArrayList<>();
    private final List<Line> powerTransmitterLines = new ArrayList<>();
    private final int generate; // 发电功率
    private final int consume; // 耗电功率
    private final int[] color;

    private Future<VoxelShape> shapeFuture;
    private VoxelShape cachedOutlineShape;

    /**
     * 简单电网
     */
    public SimplePowerGrid(
        int id,
        String level,
        BlockPos pos,
        @NotNull List<PowerComponentInfo> powerComponentInfoList,
        int generate,
        int consume
    ) {
        this.pos = pos;
        this.level = level;
        this.id = id;
        random.setSeed(id);
        this.color = ColorUtil.hsvToRgb(random.nextInt(360), 80, 80);
        this.generate = generate;
        this.consume = consume;
        blocks.addAll(powerComponentInfoList.stream().map(PowerComponentInfo::pos).toList());
        this.powerComponentInfoList.addAll(powerComponentInfoList);
        this.shapeFuture = createMergedOutlineShape();
        createTransmitterVisualLines();
    }

    /**
     * @param buf 缓冲区
     */
    public void encode(@NotNull FriendlyByteBuf buf) {
        Tag tag = CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
        CompoundTag data = new CompoundTag();
        data.put("data", tag);
        buf.writeNbt(data);
    }

    public boolean collideFast(AABB aabb) {
        for (PowerComponentInfo it : this.powerComponentInfoList) {
            if (new AABB(
                it.pos().offset(-it.range(), -it.range(), -it.range()).getCenter(),
                it.pos().offset(it.range(), it.range(), it.range()).getCenter()
            ).intersects(aabb)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得指定坐标的电网元件信息
     */
    public Optional<PowerComponentInfo> getInfoForPos(BlockPos pos) {
        return powerComponentInfoList.stream()
            .filter(it -> it.pos().equals(pos))
            .findFirst();
    }

    public boolean isOverloaded() {
        return this.getConsume() > this.getGenerate();
    }

    /**
     * @param grid 电网
     */
    public SimplePowerGrid(@NotNull PowerGrid grid) {
        this.id = grid.hashCode();
        this.level = grid.getLevel().dimension().location().toString();
        this.pos = grid.getPos();
        Set<IPowerComponent> powerComponents = new HashSet<>();
        powerComponents.addAll(grid.storages);
        powerComponents.addAll(grid.producers);
        powerComponents.addAll(grid.consumers);
        powerComponents.addAll(grid.transmitters);
        this.color = EMPTY;
        for (IPowerComponent component : powerComponents) {
            switch (component.getComponentType()) {
                case STORAGE -> {
                    IPowerStorage it = (IPowerStorage) component;
                    powerComponentInfoList.add(
                        new PowerComponentInfo(
                            it.getPos(),
                            0,
                            0,
                            it.getPowerAmount(),
                            it.getCapacity(),
                            it.getRange(),
                            PowerComponentType.STORAGE
                        )
                    );
                }
                case CONSUMER -> {
                    IPowerConsumer it = (IPowerConsumer) component;
                    powerComponentInfoList.add(
                        new PowerComponentInfo(
                            it.getPos(),
                            it.getInputPower(),
                            0,
                            0,
                            0,
                            it.getRange(),
                            PowerComponentType.CONSUMER
                        )
                    );
                }
                case PRODUCER -> {
                    IPowerProducer it = (IPowerProducer) component;
                    powerComponentInfoList.add(
                        new PowerComponentInfo(
                            it.getPos(),
                            0,
                            it.getOutputPower(),
                            0,
                            0,
                            it.getRange(),
                            PowerComponentType.PRODUCER
                        )
                    );
                }

                case TRANSMITTER -> {
                    IPowerTransmitter it = (IPowerTransmitter) component;
                    powerComponentInfoList.add(
                        new PowerComponentInfo(
                            it.getPos(),
                            0,
                            0,
                            0,
                            0,
                            it.getRange(),
                            PowerComponentType.TRANSMITTER
                        )
                    );
                }

                default -> powerComponentInfoList.add(
                    new PowerComponentInfo(
                        component.getPos(),
                        0,
                        0,
                        0,
                        0,
                        component.getRange(),
                        PowerComponentType.INVALID
                    )
                );
            }
        }
        this.consume = grid.getConsume();
        this.generate = grid.getGenerate();
        cachedOutlineShape = Shapes.block();
    }

    public boolean shouldRender(Vec3 cameraPos) {
        int renderDistance = Minecraft.getInstance().options.getEffectiveRenderDistance() * 16;
        return powerComponentInfoList.stream()
            .anyMatch(it -> it.pos().getCenter().distanceTo(cameraPos) < renderDistance);
    }

    private void createTransmitterVisualLines() {
        List<Map.Entry<BlockPos, AABB>> shapes = this.powerComponentInfoList.stream()
            .filter(it -> it.type() == PowerComponentType.TRANSMITTER)
            .map(it -> Map.entry(
                it.pos(),
                new AABB(
                    -it.range() + it.pos().getX(),
                    -it.range() + it.pos().getY(),
                    -it.range() + it.pos().getZ(),
                    it.range() + 1 + it.pos().getX(),
                    it.range() + 1 + it.pos().getY(),
                    it.range() + 1 + it.pos().getZ()
                )
            )).toList();

        for (int i = 0; i < shapes.size(); i++) {
            Map.Entry<BlockPos, AABB> e1 = shapes.get(i);
            for (int j = i + 1; j < shapes.size(); j++) {
                Map.Entry<BlockPos, AABB> e2 = shapes.get(j);
                AABB a = e1.getValue();
                AABB b = e2.getValue();
                if (a.intersects(b)) {
                    Vec3 start = e1.getKey().getCenter();
                    Vec3 end = e2.getKey().getCenter();
                    powerTransmitterLines.add(new Line(start, end, (float) start.distanceTo(end)));
                }
            }
        }
    }

    private Future<VoxelShape> createMergedOutlineShape() {
        List<VoxelShape> input = new ArrayList<>();
        for (PowerComponentInfo it : powerComponentInfoList) {
            input.add(Shapes.box(
                -it.range(),
                -it.range(),
                -it.range(),
                it.range() + 1,
                it.range() + 1,
                it.range() + 1
            ).move(
                offset(it.pos()).getX(),
                offset(it.pos()).getY(),
                offset(it.pos()).getZ()
            ));
        }
        return ShapeUtil.threadedJoin(input, BooleanOp.OR, EXECUTOR);
    }

    private @NotNull BlockPos offset(@NotNull BlockPos pos) {
        return pos.subtract(this.pos);
    }

    /**
     * 寻找电网
     */
    public static Optional<SimplePowerGrid> findPowerGrid(BlockPos pos) {
        for (SimplePowerGrid value : PowerGridClient.getGridMap().values()) {
            for (BlockPos block : value.blocks) {
                if (block.equals(pos)) {
                    return Optional.of(value);
                }
            }
        }
        return Optional.empty();
    }

    public VoxelShape getCachedOutlineShape() {
        if (this.cachedOutlineShape != null) return cachedOutlineShape;
        if (shapeFuture.isCancelled()) {
            return cachedOutlineShape;
        }
        try {
            if (this.cachedOutlineShape == null && shapeFuture.isDone()) {
                this.cachedOutlineShape = shapeFuture.get();
            }
            return cachedOutlineShape;
        } catch (Throwable e) {
            if (e instanceof ExecutionException) {
                AnvilCraft.LOGGER.error("Exception thrown while building power grid shape.", e);
            }
            return null;
        }
    }

    public void destroy() {
        if (!shapeFuture.isDone()) {
            shapeFuture.cancel(true);
        }
    }

    public static void recreateExecutor() {
        if (EXECUTOR != null){
            EXECUTOR.shutdownNow();
        }
        EXECUTOR = Executors.newThreadPerTaskExecutor(new VirtualThreadFactoryImpl());
    }
}
