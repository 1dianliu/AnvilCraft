package dev.dubhe.anvilcraft.item;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EmberMetalPickaxeItem extends PickaxeItem implements IInherentEnchantment, IFireReforging {
    /**
     *
     */
    public EmberMetalPickaxeItem(Properties properties) {
        super(
            ModTiers.EMBER_METAL,
            properties.fireResistant()
                .attributes(AxeItem.createAttributes(ModTiers.EMBER_METAL, 6, -2.8f))
        );
    }

    @Override
    public void appendHoverText(
        ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        if (pContext.level() != null) {
            pTooltipComponents.addAll(this.getInherentEnchantmentsTooltip(pContext.level()));
        }
    }

    @Override
    public Map<ResourceKey<Enchantment>, Integer> getInherentEnchantments() {
        return Map.of(Enchantments.UNBREAKING, 5);
    }

    @Override
    public ItemEnchantments getAllEnchantments(ItemStack stack, HolderLookup.RegistryLookup<Enchantment> lookup) {
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(super.getAllEnchantments(stack, lookup));

        for (var entry : getInherentEnchantments().entrySet()) {
            Holder.Reference<Enchantment> holder = lookup.getOrThrow(entry.getKey());
            enchantments.set(holder, entry.getValue());
        }

        return enchantments.toImmutable();
    }
}
