package org.modogthedev.superposition.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.modogthedev.superposition.Superposition;
import org.modogthedev.superposition.block.SignalGeneratorBlock;
import org.modogthedev.superposition.blockentity.SignalGeneratorBlockEntity;
import org.modogthedev.superposition.core.SuperpositionRenderTypes;

public class SignalGeneratorBlockEntityRenderer implements BlockEntityRenderer<SignalGeneratorBlockEntity> {

    public SignalGeneratorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }
    @Override
    public void render(SignalGeneratorBlockEntity be, float pPartialTick, PoseStack ms, MultiBufferSource bufferSource, int light, int pPackedOverlay) {
        if (isInvalid(be))
            return;
        VertexConsumer buffer = bufferSource.getBuffer(SuperpositionRenderTypes.polygonOffset(Superposition.id("textures/block/signal_generator/front.png")));

        float min = getMinPlaneExtent(be);
        float max = getMaxPlaneExtent(be);

            ms.translate(0.5, 0.5, 0.5);
            ms.mulPose(be.getBlockState().getValue(SignalGeneratorBlock.FACING).getRotation());


        Matrix4f m = ms.last().pose();
        Matrix3f n = ms.last().normal();

        float uvMin = (.5f + min);
        float uvMax = (.5f + max);

        float alpha = 1 /*Mth.clamp(be.remainingPolishAmount / UnpolishedComponentBlockEntity.DEFAULT_POLISHING_AMOUNT, 0f, 1f)*/ ;
        float stage = Math.round(be.dial);
        float stages = 25;

        float offset = (stage / stages);
        float uvOffsetx = 0f;

        light = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().relative(be.getBlockState().getValue(SignalGeneratorBlock.FACING),1));

        buffer
                .vertex(m, min, 0.5001f, min)
                .color(1f, 1f, 1f, alpha)
                .uv(uvMin+uvOffsetx, (uvMin/stages)+offset)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(n, 0, 1, 0)
                .endVertex();

        buffer
                .vertex(m, min, 0.5001f, max)
                .color(1f, 1f, 1f, alpha)
                .uv(uvMin+uvOffsetx, (uvMax/stages)+offset)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(n, 0, 1, 0)
                .endVertex();

        buffer
                .vertex(m, max, 0.5001f, max)
                .color(1f, 1f, 1f, alpha)
                .uv(uvMax+uvOffsetx, (uvMax/stages)+offset)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(n, 0, 1, 0)
                .endVertex();

        buffer
                .vertex(m, max, 0.5001f, min)
                .color(1f, 1f, 1f, alpha)
                .uv(uvMax+uvOffsetx, (uvMin/stages)+offset)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(n, 0, 1, 0)
                .endVertex();

    }
    private float getMaxPlaneExtent(SignalGeneratorBlockEntity be) {
        return -(0.5f);
    }

    private float getMinPlaneExtent(SignalGeneratorBlockEntity be) {
        return 0.5f;
    }
    public boolean isInvalid(SignalGeneratorBlockEntity be) {
        return !be.hasLevel() || be.getBlockState()
                .getBlock() == Blocks.AIR;
    }

    @Override
    public boolean shouldRender(SignalGeneratorBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return (BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos) && (pBlockEntity.getLevel().getBlockState(pBlockEntity.getBlockPos().relative(pBlockEntity.getBlockState().getValue(SignalGeneratorBlock.FACING),1)).is(Blocks.AIR) || !pBlockEntity.getLevel().getBlockState(pBlockEntity.getBlockPos().relative(pBlockEntity.getBlockState().getValue(SignalGeneratorBlock.FACING),1)).canOcclude()));
    }
}
