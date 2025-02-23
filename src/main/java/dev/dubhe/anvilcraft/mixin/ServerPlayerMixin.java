package dev.dubhe.anvilcraft.mixin;

import com.mojang.authlib.GameProfile;
import dev.dubhe.anvilcraft.api.power.DynamicPowerComponent;
import dev.dubhe.anvilcraft.api.power.IDynamicPowerComponentHolder;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.init.ModComponents;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.IonocraftBackpackItem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
                int flightTime = itemStack.getOrDefault(ModComponents.FLIGHT_TIME, IonocraftBackpackItem.FLIGHT_MAX_TIME);
                flightTime--;
                if (!(anvilCraft$component.getPowerGrid() != null && anvilCraft$component.getPowerGrid().isWorking())) {
                    if (flightTime <= IonocraftBackpackItem.FLIGHT_MAX_TIME / 2) {
                        Inventory inventory = getInventory();
                        int slot = inventory.findSlotMatchingItem(ModItems.CAPACITOR.asStack());
                        if (slot != -1) {
                            inventory.removeItem(slot, 1);
                            inventory.placeItemBackInInventory(ModItems.CAPACITOR_EMPTY.asStack());
                            flightTime = Math.clamp(
                                flightTime + IonocraftBackpackItem.FLIGHT_MAX_TIME / 2,
                                0,
                                IonocraftBackpackItem.FLIGHT_MAX_TIME
                            );
                        }
                    }
                }
                itemStack.set(ModComponents.FLIGHT_TIME, Math.clamp(flightTime, 0, IonocraftBackpackItem.FLIGHT_MAX_TIME));
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
            int flightTime = itemStack.getOrDefault(ModComponents.FLIGHT_TIME, IonocraftBackpackItem.FLIGHT_MAX_TIME);
            flightTime = Math.clamp(flightTime + IonocraftBackpackItem.FLIGHT_MAX_TIME / 120, 0, IonocraftBackpackItem.FLIGHT_MAX_TIME);
            itemStack.set(ModComponents.FLIGHT_TIME, flightTime);
        }
    }

    @Override
    public DynamicPowerComponent anvilCraft$getPowerComponent() {
        return anvilCraft$component;
    }
}
