package dev.dubhe.anvilcraft.network;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.client.gui.screen.IFilterScreen;
import dev.dubhe.anvilcraft.inventory.IFilterMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import org.jetbrains.annotations.NotNull;

public class SlotFilterChangePacket implements CustomPacketPayload {
    public static final Type<SlotFilterChangePacket> TYPE = new Type<>(AnvilCraft.of("slot_filter_change"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SlotFilterChangePacket> STREAM_CODEC =
        StreamCodec.ofMember(SlotFilterChangePacket::encode, SlotFilterChangePacket::decode);
    public static final IPayloadHandler<SlotFilterChangePacket> HANDLER = new DirectionalPayloadHandler<>(
        SlotFilterChangePacket::clientHandler, SlotFilterChangePacket::serverHandler);

    private final int index;
    private final ItemStack filter;

    /**
     * 更改过滤
     *
     * @param index  槽位
     * @param filter 过滤
     */
    public SlotFilterChangePacket(int index, @NotNull ItemStack filter, boolean forceCount) {
        this.index = index;
        this.filter = filter.copy();
        if (forceCount) this.filter.setCount(1);
    }

    public SlotFilterChangePacket(int index, @NotNull ItemStack filter) {
        this(index, filter, true);
    }

    public static SlotFilterChangePacket decode(@NotNull RegistryFriendlyByteBuf buf) {
        int index = buf.readInt();
        ItemStack filter = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
        return new SlotFilterChangePacket(index, filter, false);
    }

    public void encode(@NotNull RegistryFriendlyByteBuf buf) {
        buf.writeInt(this.index);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, this.filter);
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void serverHandler(SlotFilterChangePacket data, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        context.enqueueWork(() -> {
            if (!player.hasContainerOpen()) return;
            if (!(player.containerMenu instanceof IFilterMenu menu)) return;
            menu.setFilter(data.index, data.filter);
            menu.flush();
            PacketDistributor.sendToPlayer(player, data);
        });
    }

    public static void clientHandler(SlotFilterChangePacket data, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        context.enqueueWork(() -> {
            if (!(client.screen instanceof IFilterScreen<?> screen)) return;
            screen.setFilter(data.index, data.filter);
        });
    }
}
