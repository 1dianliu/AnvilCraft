package dev.dubhe.anvilcraft.data.generator;

import dev.dubhe.anvilcraft.init.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class MyBlockTagGenerator extends FabricTagProvider<Block> {
    public MyBlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        this.getOrCreateTagBuilder(ModBlockTags.REDSTONE_TORCH).setReplace(false)
                .add(Blocks.REDSTONE_WALL_TORCH)
                .add(Blocks.REDSTONE_TORCH);
        this.getOrCreateTagBuilder(ModBlockTags.MUSHROOM_BLOCK).setReplace(false)
                .add(Blocks.BROWN_MUSHROOM_BLOCK)
                .add(Blocks.RED_MUSHROOM_BLOCK)
                .add(Blocks.MUSHROOM_STEM);
    }
}
