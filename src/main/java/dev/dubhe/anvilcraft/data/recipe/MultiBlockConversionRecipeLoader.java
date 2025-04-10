package dev.dubhe.anvilcraft.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.block.AccelerationRingBlock;
import dev.dubhe.anvilcraft.block.GiantAnvilBlock;
import dev.dubhe.anvilcraft.block.HeavyIronBeamBlock;
import dev.dubhe.anvilcraft.block.LargeCakeBlock;
import dev.dubhe.anvilcraft.block.state.Cube3x3PartHalf;
import dev.dubhe.anvilcraft.block.state.DirectionCube3x3PartHalf;
import dev.dubhe.anvilcraft.block.state.GiantAnvilCube;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.recipe.multiblock.BlockPredicateWithState;
import dev.dubhe.anvilcraft.recipe.multiblock.ModifySpawnerAction;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockConversionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

import static net.minecraft.core.Direction.Axis;

public class MultiBlockConversionRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        MultiblockConversionRecipe.builder()
            .inputLayer("ABA", "CDC", "ABA")
            .inputLayer("E E", " F ", "E E")
            .inputLayer("ABA", "C C", "ABA")
            .outputLayer("ABA", "C C", "ABA")
            .outputLayer("E E", " F ", "E E")
            .outputLayer("ABA", "C C", "ABA")
            .inputSymbol('A', ModBlocks.CURSED_GOLD_BLOCK)
            .outputSymbol('A', Blocks.SCULK)
            .inputSymbol('B', BlockPredicateWithState.of(Blocks.CHAIN)
                .hasState("axis", "x")
            )
            .outputSymbol('B', Blocks.COBWEB)
            .inputSymbol('C', BlockPredicateWithState.of(Blocks.CHAIN)
                .hasState("axis", "z")
            )
            .outputSymbol('C', Blocks.COBWEB)
            .inputSymbol('D', Blocks.SOUL_FIRE)
            .inputSymbol('E', BlockPredicateWithState.of(Blocks.CHAIN)
                .hasState("axis", "y")
            )
            .outputSymbol('E', Blocks.COBWEB)
            .inputSymbol('F', ModBlocks.RESENTFUL_AMBER_BLOCK)
            .outputSymbol('F', Blocks.SPAWNER)
            .modifySpawnerAction(new ModifySpawnerAction(
                new BlockPos(1, 1, 1),
                new BlockPos(1, 1, 1)))
            .save(provider, AnvilCraft.of("multiblock_conversion/spawner"));

        MultiblockConversionRecipe.builder()
            .inputLayer("AAA", "AAA", "AAA")
            .inputLayer(" B ", "BBB", " B ")
            .inputLayer("   ", " C ", "   ")
            .inputSymbol('A', ModBlocks.CAKE_BLOCK)
            .inputSymbol('B', ModBlocks.BERRY_CAKE_BLOCK)
            .inputSymbol('C', ModBlocks.CHOCOLATE_CAKE_BLOCK)
            .outputLayer("ABC", "DEF", "GHI")
            .outputLayer("JKL", "MNO", "PQR")
            .outputLayer("STU", "VWX", "YZ[")
            .outputSymbol('A', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.BOTTOM_WN)
            )
            .outputSymbol('B', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.BOTTOM_N)
            )
            .outputSymbol('C', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.BOTTOM_EN)
            )
            .outputSymbol('D', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.BOTTOM_W)
            )
            .outputSymbol('E', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.BOTTOM_CENTER)
            )
            .outputSymbol('F', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.BOTTOM_E)
            )
            .outputSymbol('G', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.BOTTOM_WS)
            )
            .outputSymbol('H', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.BOTTOM_S)
            )
            .outputSymbol('I', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.BOTTOM_ES)
            )
            .outputSymbol('J', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.MID_WN)
            )
            .outputSymbol('K', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.MID_N)
            )
            .outputSymbol('L', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.MID_EN)
            )
            .outputSymbol('M', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.MID_W)
            )
            .outputSymbol('N', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.MID_CENTER)
            )
            .outputSymbol('O', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.MID_E)
            )
            .outputSymbol('P', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.MID_WS)
            )
            .outputSymbol('Q', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.MID_S)
            )
            .outputSymbol('R', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.MID_ES)
            )
            .outputSymbol('S', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.TOP_WN)
            )
            .outputSymbol('T', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.TOP_N)
            )
            .outputSymbol('U', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.TOP_EN)
            )
            .outputSymbol('V', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.TOP_W)
            )
            .outputSymbol('W', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.TOP_CENTER)
            )
            .outputSymbol('X', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.TOP_E)
            )
            .outputSymbol('Y', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.TOP_WS)
            )
            .outputSymbol('Z', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.TOP_S)
            )
            .outputSymbol('[', BlockPredicateWithState.of(ModBlocks.LARGE_CAKE)
                .hasState(LargeCakeBlock.HALF, Cube3x3PartHalf.TOP_ES)
            )
            .save(provider, AnvilCraft.of("multiblock_conversion/large_cake"));

        MultiblockConversionRecipe.builder()
            .inputLayer("ABA", "CDE", "AFA")
            .inputLayer("GGG", "GDG", "GGG")
            .inputLayer("HIH", "JHJ", "HIH")
            .inputSymbol('A', BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_SLAB)
                .hasState(BlockStateProperties.WATERLOGGED, false)
                .hasState(SlabBlock.TYPE, SlabType.BOTTOM)
            )
            .inputSymbol('B', BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_STAIRS)
                .hasState(StairBlock.FACING, Direction.SOUTH)
                .hasState(BlockStateProperties.WATERLOGGED, false)
                .hasState(StairBlock.HALF, Half.BOTTOM)
            )
            .inputSymbol('C', BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_STAIRS)
                .hasState(StairBlock.FACING, Direction.EAST)
                .hasState(BlockStateProperties.WATERLOGGED, false)
                .hasState(StairBlock.HALF, Half.BOTTOM)
            )
            .inputSymbol('D', ModBlocks.HEAVY_IRON_COLUMN)
            .inputSymbol('E', BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_STAIRS)
                .hasState(StairBlock.FACING, Direction.WEST)
                .hasState(BlockStateProperties.WATERLOGGED, false)
                .hasState(StairBlock.HALF, Half.BOTTOM)
            )
            .inputSymbol('F', BlockPredicateWithState.of(ModBlocks.CUT_HEAVY_IRON_STAIRS)
                .hasState(StairBlock.FACING, Direction.NORTH)
                .hasState(BlockStateProperties.WATERLOGGED, false)
                .hasState(StairBlock.HALF, Half.BOTTOM)
            )
            .inputSymbol('G', ModBlocks.HEAVY_IRON_PLATE)
            .inputSymbol('H', ModBlocks.POLISHED_HEAVY_IRON_BLOCK)
            .inputSymbol('I', BlockPredicateWithState.of(ModBlocks.HEAVY_IRON_BEAM)
                .hasState(HeavyIronBeamBlock.AXIS, Axis.Z)
            )
            .inputSymbol('J', BlockPredicateWithState.of(ModBlocks.HEAVY_IRON_BEAM)
                .hasState(HeavyIronBeamBlock.AXIS, Axis.X)
            )
            .outputLayer("ABC", "DEF", "GHI")
            .outputLayer("JKL", "MNO", "PQR")
            .outputLayer("STU", "VWX", "YZ[")
            .outputSymbol('A', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_WN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('B', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_N)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('C', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_EN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('D', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_W)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('E', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_CENTER)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('F', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_E)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('G', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_WS)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('H', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_S)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('I', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_ES)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('J', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_WN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('K', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_N)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('L', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_EN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('M', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_W)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('N', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_CENTER)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CENTER)
            )
            .outputSymbol('O', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_E)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('P', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_WS)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('Q', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_S)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('R', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_ES)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('S', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_WN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('T', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_N)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('U', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_EN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('V', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_W)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('W', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_CENTER)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('X', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_E)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('Y', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_WS)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('Z', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_S)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('[', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_ES)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .save(provider, AnvilCraft.of("multiblock_conversion/giant_anvil_1"));

        MultiblockConversionRecipe.builder()
            .inputLayer("AAA", "AAA", "AAA")
            .inputLayer("BBB", "BCB", "BBB")
            .inputLayer("DDD", "DDD", "DDD")
            .inputSymbol('A', ModBlocks.CUT_HEAVY_IRON_BLOCK)
            .inputSymbol('B', ModBlocks.HEAVY_IRON_PLATE)
            .inputSymbol('C', ModBlocks.HEAVY_IRON_COLUMN)
            .inputSymbol('D', ModBlocks.POLISHED_HEAVY_IRON_BLOCK)
            .outputLayer("ABC", "DEF", "GHI")
            .outputLayer("JKL", "MNO", "PQR")
            .outputLayer("STU", "VWX", "YZ[")
            .outputSymbol('A', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_WN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('B', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_N)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('C', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_EN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('D', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_W)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('E', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_CENTER)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('F', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_E)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('G', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_WS)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('H', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_S)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('I', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.BOTTOM_ES)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('J', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_WN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('K', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_N)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('L', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_EN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('M', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_W)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('N', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_CENTER)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CENTER)
            )
            .outputSymbol('O', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_E)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('P', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_WS)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('Q', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_S)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('R', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.MID_ES)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('S', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_WN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('T', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_N)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('U', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_EN)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('V', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_W)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('W', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_CENTER)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('X', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_E)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('Y', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_WS)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('Z', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_S)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .outputSymbol('[', BlockPredicateWithState.of(ModBlocks.GIANT_ANVIL)
                .hasState(GiantAnvilBlock.HALF, Cube3x3PartHalf.TOP_ES)
                .hasState(GiantAnvilBlock.CUBE, GiantAnvilCube.CORNER)
            )
            .save(provider, AnvilCraft.of("multiblock_conversion/giant_anvil_2"));

        MultiblockConversionRecipe.builder()
            .inputLayer("ABA", "B B", "ABA")
            .inputLayer("CDC", "D D", "CDC")
            .inputLayer("ABA", "B B", "ABA")
            .inputSymbol('A', Blocks.COPPER_BLOCK)
            .inputSymbol('B', ModBlocks.HEAVY_IRON_BLOCK)
            .inputSymbol('C', ModBlocks.MAGNETO_ELECTRIC_CORE_BLOCK)
            .inputSymbol('D', ModBlocks.TUNGSTEN_BLOCK)
            .outputLayer("ABC", "DEF", "GHI")
            .outputLayer("JKL", "MNO", "PQR")
            .outputLayer("STU", "VWX", "YZ[")
            .outputSymbol('A', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.BOTTOM_WN)
            )
            .outputSymbol('B', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.BOTTOM_N)
            )
            .outputSymbol('C', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.BOTTOM_EN)
            )
            .outputSymbol('D', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.BOTTOM_W)
            )
            .outputSymbol('E', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.BOTTOM_CENTER)
            )
            .outputSymbol('F', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.BOTTOM_E)
            )
            .outputSymbol('G', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.BOTTOM_WS)
            )
            .outputSymbol('H', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.BOTTOM_S)
            )
            .outputSymbol('I', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.BOTTOM_ES)
            )
            .outputSymbol('J', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.MID_WN)
            )
            .outputSymbol('K', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.MID_N)
            )
            .outputSymbol('L', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.MID_EN)
            )
            .outputSymbol('M', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.MID_W)
            )
            .outputSymbol('N', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.MID_CENTER)
            )
            .outputSymbol('O', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.MID_E)
            )
            .outputSymbol('P', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.MID_WS)
            )
            .outputSymbol('Q', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.MID_S)
            )
            .outputSymbol('R', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.MID_ES)
            )
            .outputSymbol('S', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.TOP_WN)
            )
            .outputSymbol('T', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.TOP_N)
            )
            .outputSymbol('U', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.TOP_EN)
            )
            .outputSymbol('V', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.TOP_W)
            )
            .outputSymbol('W', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.TOP_CENTER)
            )
            .outputSymbol('X', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.TOP_E)
            )
            .outputSymbol('Y', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.TOP_WS)
            )
            .outputSymbol('Z', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.TOP_S)
            )
            .outputSymbol('[', BlockPredicateWithState.of(ModBlocks.ACCELERATION_RING)
                .hasState(AccelerationRingBlock.FACING, Direction.UP)
                .hasState(AccelerationRingBlock.HALF, DirectionCube3x3PartHalf.TOP_ES)
            )
            .save(provider, AnvilCraft.of("multiblock_conversion/acceleration_ring"));
    }
}
