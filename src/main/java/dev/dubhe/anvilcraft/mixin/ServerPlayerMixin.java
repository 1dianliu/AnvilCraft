package dev.dubhe.anvilcraft.mixin;

import com.mojang.authlib.GameProfile;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.power.DynamicPowerComponent;
import dev.dubhe.anvilcraft.api.power.IDynamicPowerComponentHolder;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.IonocraftBackpackItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements IDynamicPowerComponentHolder {
    @Shadow @Final public MinecraftServer server;
    @Unique
    private DynamicPowerComponent anvilCraft$component;

    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    void constructPowerComponent(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation, CallbackInfo ci) {
        this.anvilCraft$component = new DynamicPowerComponent(
            this,
            this::anvilCraft$getPowerSupplyingBoundingBox
        );
    }

    @Unique
    public AABB anvilCraft$getPowerSupplyingBoundingBox() {
        return this.getBoundingBox().inflate(0.5);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    void tickFlight(CallbackInfo ci) {
        PowerGrid powerGrid = PowerGrid.findPowerGridContains(
            level(),
            anvilCraft$getPowerSupplyingBoundingBox()
        ).orElse(null);
        anvilCraft$component.switchTo(powerGrid);
        if (this.getAbilities().flying) {
            ItemStack itemStack = this.getItemBySlot(EquipmentSlot.CHEST);
            if (itemStack.is(ModItems.IONOCRAFT_BACKPACK)) {
                int flightTime = IonocraftBackpackItem.getFlightTime(itemStack);
                flightTime--;
                if (!(anvilCraft$component.getPowerGrid() != null && anvilCraft$component.getPowerGrid().isWorking())) {
                    if (flightTime <= AnvilCraft.config.ionoCraftBackpackMaxFlightTime / 2) {
                        Inventory inventory = getInventory();
                        int slot = inventory.findSlotMatchingItem(ModItems.CAPACITOR.asStack());
                        if (slot != -1) {
                            inventory.removeItem(slot, 1);
                            inventory.placeItemBackInInventory(ModItems.CAPACITOR_EMPTY.asStack());
                            flightTime = Math.clamp(
                                flightTime + AnvilCraft.config.ionoCraftBackpackMaxFlightTime / 2,
                                0,
                                AnvilCraft.config.ionoCraftBackpackMaxFlightTime
                            );
                        }
                    }
                }
                IonocraftBackpackItem.setFlightTime(itemStack, flightTime);
            }
        }
    }

    @Override
    public void anvilCraft$gridTick() {
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.CHEST);
        if (itemStack.is(ModItems.IONOCRAFT_BACKPACK)
            && anvilCraft$component.getPowerGrid() != null
            && anvilCraft$component.getPowerGrid().isWorking()
        ) {
            int flightTime = IonocraftBackpackItem.getFlightTime(itemStack);
            flightTime = flightTime + AnvilCraft.config.ionoCraftBackpackMaxFlightTime / 120;
            IonocraftBackpackItem.setFlightTime(itemStack, flightTime);
        }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        super.remove(reason);
        anvilCraft$component.switchTo(null);
    }

    @Override
    public DynamicPowerComponent anvilCraft$getPowerComponent() {
        return anvilCraft$component;
    }
}
