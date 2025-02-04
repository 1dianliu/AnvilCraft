package dev.dubhe.anvilcraft.data.recipe;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.block.HeavyIronBeamBlock;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.recipe.multiblock.BlockPredicateWithState;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockRecipe;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;

public class MultiBlockRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        MultiblockRecipe.builder(ModBlocks.GIANT_ANVIL)
                .layer("JGJ", "HDI", "JFJ")
                .layer("EEE", "EDE", "EEE")
                .layer("ACA", "BAB", "ACA")
                .symbol('A', ModBlocks.POLISHED_HEAVY_IRON_BLOCK)
                .symbol(
                        'B',
                        BlockPredicateWithState.of(ModBlocks.HEAVY_IRON_BEAM)
                                .hasState(HeavyIronBeamBlock.AXIS, Direction.Axis.X))
                .symbol(
                        'C',
                        BlockPredicateWithState.of(ModBlocks.HEAVY_IRON_BEAM)
                                .hasState(HeavyIronBeamBlock.AXIS, Direction.Axis.Z))
                .symbol('D', ModBlocks.HEAVY_IRON_COLUMN)
                .symbol('E', ModBlocks.HEAVY_IRON_PLATE)
                .symbol(
                        'F',
                        BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_STAIRS)
                                .hasState(StairBlock.FACING, Direction.NORTH)
                                .hasState(StairBlock.HALF, Half.BOTTOM))
                .symbol(
                        'G',
                        BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_STAIRS)
                                .hasState(StairBlock.FACING, Direction.SOUTH)
                                .hasState(StairBlock.HALF, Half.BOTTOM))
                .symbol(
                        'H',
                        BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_STAIRS)
                                .hasState(StairBlock.FACING, Direction.EAST)
                                .hasState(StairBlock.HALF, Half.BOTTOM))
                .symbol(
                        'I',
                        BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_STAIRS)
                                .hasState(StairBlock.FACING, Direction.WEST)
                                .hasState(StairBlock.HALF, Half.BOTTOM))
                .symbol(
                        'J',
                        BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_SLAB)
                                .hasState(SlabBlock.TYPE, SlabType.BOTTOM))
                .save(provider, AnvilCraft.of("multiblock/giant_anvil_1"));

        MultiblockRecipe.builder(ModBlocks.GIANT_ANVIL)
                .layer("AAA", "AAA", "AAA")
                .layer("CCC", "CBC", "CCC")
                .layer("DDD", "DDD", "DDD")
                .symbol('A', ModBlocks.CUT_HEAVY_IRON_BLOCK)
                .symbol('B', ModBlocks.HEAVY_IRON_COLUMN)
                .symbol('C', ModBlocks.HEAVY_IRON_PLATE)
                .symbol('D', ModBlocks.POLISHED_HEAVY_IRON_BLOCK)
                .save(provider, AnvilCraft.of("multiblock/giant_anvil_2"));

        MultiblockRecipe.builder(ModBlocks.MENGER_SPONGE)
                .layer("AAA", "A A", "AAA")
                .layer("A A", " B ", "A A")
                .layer("AAA", "A A", "AAA")
                .symbol('A', Blocks.SPONGE)
                .symbol('B', ModBlocks.VOID_MATTER_BLOCK)
                .save(provider);

        MultiblockRecipe.builder(Blocks.DIAMOND_BLOCK)
                .layer("AAA", "AAA", "AAA")
                .layer("AAA", "AAA", "AAA")
                .layer("AAA", "AAA", "AAA")
                .symbol('A', Blocks.COAL_BLOCK)
                .save(provider);

        MultiblockRecipe.builder(ModBlocks.LARGE_CAKE)
                .layer("AAA", "AAA", "AAA")
                .layer(" B ", "BBB", " B ")
                .layer("   ", " C ", "   ")
                .symbol('A', ModBlocks.CAKE_BLOCK)
                .symbol('B', ModBlocks.BERRY_CAKE_BLOCK)
                .symbol('C', ModBlocks.CHOCOLATE_CAKE_BLOCK)
                .save(provider);

        MultiblockRecipe.builder(ModBlocks.MENGER_SPONGE)
                .layer(
                        "AAAAAAAAA",
                        "A AA AA A",
                        "AAAAAAAAA",
                        "AAA   AAA",
                        "A A   A A",
                        "AAA   AAA",
                        "AAAAAAAAA",
                        "A AA AA A",
                        "AAAAAAAAA")
                .layer(
                        "A AA AA A",
                        "         ",
                        "A AA AA A",
                        "A A   A A",
                        "         ",
                        "A A   A A",
                        "A AA AA A",
                        "         ",
                        "A AA AA A")
                .layer(
                        "AAAAAAAAA",
                        "A AA AA A",
                        "AAAAAAAAA",
                        "AAA   AAA",
                        "A A   A A",
                        "AAA   AAA",
                        "AAAAAAAAA",
                        "A AA AA A",
                        "AAAAAAAAA")
                .layer(
                        "AAA   AAA",
                        "A A   A A",
                        "AAA   AAA",
                        "         ",
                        "         ",
                        "         ",
                        "AAA   AAA",
                        "A A   A A",
                        "AAA   AAA")
                .layer(
                        "A A   A A",
                        "         ",
                        "A A   A A",
                        "         ",
                        "         ",
                        "         ",
                        "A A   A A",
                        "         ",
                        "A A   A A")
                .layer(
                        "AAA   AAA",
                        "A A   A A",
                        "AAA   AAA",
                        "         ",
                        "         ",
                        "         ",
                        "AAA   AAA",
                        "A A   A A",
                        "AAA   AAA")
                .layer(
                        "AAAAAAAAA",
                        "A AA AA A",
                        "AAAAAAAAA",
                        "AAA   AAA",
                        "A A   A A",
                        "AAA   AAA",
                        "AAAAAAAAA",
                        "A AA AA A",
                        "AAAAAAAAA")
                .layer(
                        "A AA AA A",
                        "         ",
                        "A AA AA A",
                        "A A   A A",
                        "         ",
                        "A A   A A",
                        "A AA AA A",
                        "         ",
                        "A AA AA A")
                .layer(
                        "AAAAAAAAA",
                        "A AA AA A",
                        "AAAAAAAAA",
                        "AAA   AAA",
                        "A A   A A",
                        "AAA   AAA",
                        "AAAAAAAAA",
                        "A AA AA A",
                        "AAAAAAAAA")
                .symbol('A', ModBlocks.MENGER_SPONGE)
                .save(provider, AnvilCraft.of("multiblock/menger_sponge_2"));

        MultiblockRecipe.builder(ModBlocks.ACCELERATION_RING, 1)
            .layer("ABA", "B B", "ABA")
            .layer("CDC", "D D", "CDC")
            .layer("ABA", "B B", "ABA")
            .symbol('A', Blocks.COPPER_BLOCK)
            .symbol('B', ModBlocks.HEAVY_IRON_BLOCK)
            .symbol('C', ModBlocks.MAGNETO_ELECTRIC_CORE_BLOCK)
            .symbol('D', ModBlocks.TUNGSTEN_BLOCK)
            .save(provider);
    }
}
