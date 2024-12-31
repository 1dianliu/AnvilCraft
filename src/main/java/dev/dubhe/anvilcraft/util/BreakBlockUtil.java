package dev.dubhe.anvilcraft.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.stream.Collectors;

public class BreakBlockUtil {

    private static ItemStack DUMMY_SILK_TOUCH_TOOL = null;
    private static final ItemStack SHEARS_INSTANCE = Items.SHEARS.getDefaultInstance();

    public static ItemStack getDummySilkTouchTool(ServerLevel level) {
        if (DUMMY_SILK_TOUCH_TOOL == null) {
            ItemStack tool = Items.NETHERITE_PICKAXE.getDefaultInstance();
            tool.set(DataComponents.CUSTOM_NAME, Component.literal("Dummy Silk Touch Tool"));
            level.holderLookup(Registries.ENCHANTMENT)
                .get(Enchantments.SILK_TOUCH)
                .ifPresent(e -> tool.enchant(e, 1));
            DUMMY_SILK_TOUCH_TOOL = tool;
        }
        return DUMMY_SILK_TOUCH_TOOL;
    }

    private static List<ItemStack> dropWithTool(ServerLevel level, BlockPos pos, ItemStack tool) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return List.of();
        LootParams.Builder builder = new LootParams.Builder(level)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
            .withParameter(LootContextParams.TOOL, tool)
            .withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos));
        return state.getDrops(builder);
    }

    public static List<ItemStack> drop(ServerLevel level, BlockPos pos) {
        return dropWithTool(level, pos, ItemStack.EMPTY);
    }

    public static List<ItemStack> dropSilkTouch(ServerLevel level, BlockPos pos) {
        return dropWithTool(level, pos, getDummySilkTouchTool(level));
    }

    public static List<ItemStack> dropSmelt(ServerLevel level, BlockPos pos) {
        return drop(level, pos).stream()
            .map(it -> {
                SingleRecipeInput cont = new SingleRecipeInput(it);
                return level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, cont, level)
                    .map(smeltingRecipe -> smeltingRecipe.value().assemble(cont, level.registryAccess()))
                    .orElse(it);
            })
            .collect(Collectors.toList());
    }

    public static List<ItemStack> dropSilkTouchOrShears(ServerLevel level, BlockPos pos) {
        List<ItemStack> drops = dropWithTool(level, pos, SHEARS_INSTANCE);
        if (drops.stream().allMatch(ItemStack::isEmpty)) return dropSilkTouch(level, pos);
        return drops;
    }

    public static List<ItemStack> dropFromSnowLayers(BlockState state) {
        if (!state.hasProperty(SnowLayerBlock.LAYERS)) return List.of();
        int layers = state.getValue(SnowLayerBlock.LAYERS);
        return List.of(layers <= 7 ? new ItemStack(Blocks.SNOW, layers) :
            Blocks.SNOW_BLOCK.asItem().getDefaultInstance());
    }
}
