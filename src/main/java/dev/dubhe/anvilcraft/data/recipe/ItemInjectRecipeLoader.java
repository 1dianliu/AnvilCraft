package dev.dubhe.anvilcraft.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.recipe.anvil.ItemInjectRecipe;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ItemInjectRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        ItemInjectRecipe.builder()
            .requires(Items.RAW_COPPER_BLOCK, 3)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(Blocks.DEEPSLATE_COPPER_ORE)
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(Items.RAW_IRON_BLOCK)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(Blocks.DEEPSLATE_IRON_ORE)
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(Items.RAW_GOLD_BLOCK)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(Blocks.DEEPSLATE_GOLD_ORE)
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(Items.GOLD_INGOT, 2)
            .inputBlock(Blocks.NETHERRACK)
            .resultBlock(Blocks.NETHER_GOLD_ORE)
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(Items.GOLD_INGOT)
            .inputBlock(Blocks.BLACKSTONE)
            .resultBlock(Blocks.GILDED_BLACKSTONE)
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(ModBlocks.RAW_ZINC)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(ModBlocks.DEEPSLATE_ZINC_ORE.get())
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(ModBlocks.RAW_TIN)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(ModBlocks.DEEPSLATE_TIN_ORE.get())
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(ModBlocks.RAW_TITANIUM)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(ModBlocks.DEEPSLATE_TITANIUM_ORE.get())
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(ModBlocks.RAW_TUNGSTEN)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(ModBlocks.DEEPSLATE_TUNGSTEN_ORE.get())
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(ModBlocks.RAW_LEAD)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(ModBlocks.DEEPSLATE_LEAD_ORE.get())
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(ModBlocks.RAW_SILVER)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(ModBlocks.DEEPSLATE_SILVER_ORE.get())
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(ModBlocks.RAW_URANIUM)
            .inputBlock(Blocks.DEEPSLATE)
            .resultBlock(ModBlocks.DEEPSLATE_URANIUM_ORE.get())
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(Items.SHULKER_BOX)
            .inputBlock(Blocks.SHULKER_BOX)
            .resultBlock(ModBlocks.NESTING_SHULKER_BOX.get())
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(ModBlocks.NESTING_SHULKER_BOX)
            .inputBlock(Blocks.SHULKER_BOX)
            .resultBlock(ModBlocks.OVER_NESTING_SHULKER_BOX.get())
            .save(provider);

        ItemInjectRecipe.builder()
            .requires(ModBlocks.OVER_NESTING_SHULKER_BOX)
            .inputBlock(Blocks.SHULKER_BOX)
            .resultBlock(ModBlocks.SUPERCRITICAL_NESTING_SHULKER_BOX.get())
            .save(provider);
    }
}
