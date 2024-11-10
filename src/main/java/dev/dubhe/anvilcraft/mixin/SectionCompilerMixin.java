package dev.dubhe.anvilcraft.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.client.renderer.laser.LaserCompiler;
import dev.dubhe.anvilcraft.client.renderer.laser.LaserRenderStatus;
import dev.dubhe.anvilcraft.client.renderer.laser.LaserState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(SectionCompiler.class)
public abstract class SectionCompilerMixin {

    @Shadow
    protected abstract BufferBuilder getOrBeginLayer(Map<RenderType, BufferBuilder> bufferLayers, SectionBufferBuilderPack sectionBufferBuilderPack, RenderType renderType);

    @Inject(
        method = "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/SectionCompiler;handleBlockEntity(Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;Lnet/minecraft/world/level/block/entity/BlockEntity;)V")
    )
    void compile(
        SectionPos sectionPos,
        RenderChunkRegion region,
        VertexSorting vertexSorting,
        SectionBufferBuilderPack sectionBufferBuilderPack,
        List<AddSectionGeometryEvent.AdditionalSectionRenderer> additionalRenderers,
        CallbackInfoReturnable<SectionCompiler.Results> cir,
        @Local PoseStack poseStack,
        @Local(index = 16) BlockEntity blockEntity,
        @Local(index = 11) Map<RenderType, BufferBuilder> map
    ) {
        if (!LaserRenderStatus.isEnhancedRenderingAvailable()) return;
        if (!(blockEntity instanceof BaseLaserBlockEntity baseLaserBlockEntity)) return;
        poseStack.pushPose();
        BlockPos pos = blockEntity.getBlockPos();
        poseStack.translate(
            (float) SectionPos.sectionRelative(pos.getX()),
            (float) SectionPos.sectionRelative(pos.getY()),
            (float) SectionPos.sectionRelative(pos.getZ())
        );
        LaserState laserState = LaserState.create(baseLaserBlockEntity, poseStack);
        if (laserState != null) {
            LaserCompiler.compile(
                laserState,
                renderType -> this.getOrBeginLayer(
                    map,
                    sectionBufferBuilderPack,
                    renderType
                )
            );
        }
        poseStack.popPose();
    }
}
