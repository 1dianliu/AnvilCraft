package dev.dubhe.anvilcraft.util;

import dev.dubhe.anvilcraft.AnvilCraft;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class DangerUtil {
    private DangerUtil() {}

    public static @NotNull Supplier<ConfiguredModel[]> genConfiguredModel(String path) {
        return () -> new ConfiguredModel[] {new ConfiguredModel(new ModelFile.UncheckedModelFile(AnvilCraft.of(path)))};
    }

    public static @NotNull Supplier<ModelFile.UncheckedModelFile> genUncheckedModelFile(String path) {
        return () -> new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace(path));
    }

    public static @NotNull Supplier<ModelFile.UncheckedModelFile> genUncheckedModelFile(String namespace, String path) {
        return () -> new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
