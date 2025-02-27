package dev.dubhe.anvilcraft.item;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.power.DynamicPowerComponent;
import dev.dubhe.anvilcraft.api.power.IDynamicPowerComponentHolder;
import dev.dubhe.anvilcraft.init.ModComponents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IonocraftBackpackItem extends ArmorItem {
    public static final DynamicPowerComponent.PowerConsumption CONSUMPTION = new DynamicPowerComponent.PowerConsumption(64);
    public static final int FLIGHT_MAX_TIME = 1200 * 20;

    public static final ResourceLocation TEXTURE = AnvilCraft.of("textures/entity/equipment/ionocraft_backpack.png");
    public static final ResourceLocation TEXTURE_OFF = AnvilCraft.of("textures/entity/equipment/ionocraft_backpack_off.png");

    public static final ResourceLocation CREATIVE_FLIGHT_ID = AnvilCraft.of("creative_flight");
    public static final AttributeModifier CREATIVE_FLIGHT = new AttributeModifier(
        CREATIVE_FLIGHT_ID,
        1,
        AttributeModifier.Operation.ADD_VALUE
    );

    public IonocraftBackpackItem(Properties properties) {
        super(
            ArmorMaterials.IRON,
            Type.CHESTPLATE,
            properties.component(ModComponents.FLIGHT_TIME, FLIGHT_MAX_TIME)
        );
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return this.swapWithEquipmentSlot(this, level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide) return;
        if (!(entity instanceof ServerPlayer serverPlayer)) return;
        IDynamicPowerComponentHolder holder = IDynamicPowerComponentHolder.of(serverPlayer);
        DynamicPowerComponent powerComponent = holder.anvilCraft$getPowerComponent();
        if (serverPlayer.getItemBySlot(EquipmentSlot.CHEST) != stack) {
            powerComponent.getPowerConsumptions().remove(CONSUMPTION);
            AttributeInstance instance = serverPlayer.getAttributes().getInstance(NeoForgeMod.CREATIVE_FLIGHT);
            if (instance.hasModifier(CREATIVE_FLIGHT_ID)) {
                instance.removeModifier(CREATIVE_FLIGHT);
            }
            return;
        }
        if (powerComponent.getPowerGrid() != null) {
            powerComponent.getPowerConsumptions().add(CONSUMPTION);
        } else {
            powerComponent.getPowerConsumptions().remove(CONSUMPTION);
        }
        if (stack.getOrDefault(ModComponents.FLIGHT_TIME, FLIGHT_MAX_TIME) > 0) {
            AttributeInstance instance = serverPlayer.getAttributes().getInstance(NeoForgeMod.CREATIVE_FLIGHT);
            if (!instance.hasModifier(CREATIVE_FLIGHT_ID)) {
                instance.addTransientModifier(CREATIVE_FLIGHT);
            }
        } else {
            AttributeInstance instance = serverPlayer.getAttributes().getInstance(NeoForgeMod.CREATIVE_FLIGHT);
            if (instance.hasModifier(CREATIVE_FLIGHT_ID)) {
                instance.removeModifier(CREATIVE_FLIGHT);
            }
        }
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
        return armorType == EquipmentSlot.CHEST;
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        if (stack.getOrDefault(ModComponents.FLIGHT_TIME, FLIGHT_MAX_TIME) > 0) {
            return TEXTURE;
        }
        return TEXTURE_OFF;
    }
}
