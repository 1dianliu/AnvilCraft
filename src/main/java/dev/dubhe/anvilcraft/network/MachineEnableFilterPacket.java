package dev.dubhe.anvilcraft.network;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.client.gui.screen.IFilterScreen;
import dev.dubhe.anvilcraft.inventory.IFilterMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import lombok.Getter;

@Getter
public class MachineEnableFilterPacket implements CustomPacketPayload {
    public static final Type<MachineEnableFilterPacket> TYPE = new Type<>(AnvilCraft.of("machine_record_material"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MachineEnableFilterPacket> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.BOOL, MachineEnableFilterPacket::isFilterEnabled, MachineEnableFilterPacket::new);
    public static final IPayloadHandler<MachineEnableFilterPacket> HANDLER = new DirectionalPayloadHandler<>(
        MachineEnableFilterPacket::clientHandler, MachineEnableFilterPacket::serverHandler);

    private final boolean filterEnabled;

    public MachineEnableFilterPacket(boolean filterEnabled) {
        this.filterEnabled = filterEnabled;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     *
     */
    public static void serverHandler(MachineEnableFilterPacket data, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        context.enqueueWork(() -> {
            if (!player.hasContainerOpen()) return;
            if (!(player.containerMenu instanceof IFilterMenu menu)) return;
            menu.setFilterEnabled(data.isFilterEnabled());
            menu.flush();
            if (!data.isFilterEnabled() && menu.getFilterBlockEntity() != null) {
                for (int i = 0; i < menu.getFilteredItems().size(); i++) {
                    ItemStack stack = menu.getFilteredItems().get(i);
                    if (stack.isEmpty()) continue;
                    SlotFilterChangePacket pack = new SlotFilterChangePacket(i, stack);
                    PacketDistributor.sendToPlayer(player, pack);
                }
            }
            PacketDistributor.sendToPlayer(player, data);
        });
    }

    /**
     *
     */
    public static void clientHandler(MachineEnableFilterPacket data, IPayloadContext context) {
        Minecraft client = Minecraft.getInstance();
        context.enqueueWork(() -> {
            if (client.screen instanceof IFilterScreen<?> screen) {
                screen.setFilterEnabled(data.isFilterEnabled());
                screen.flush();
            }
        });
    }
}
