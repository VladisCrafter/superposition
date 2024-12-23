package org.modogthedev.superposition.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import org.modogthedev.superposition.SuperpositionClient;
import org.modogthedev.superposition.client.renderer.ui.SuperpositionUITooltipRenderer;
import org.modogthedev.superposition.core.SuperpositionBlocks;
import org.modogthedev.superposition.system.cable.CableManager;
import org.modogthedev.superposition.system.cable.CarabinerManager;
import org.modogthedev.superposition.system.signal.ClientSignalManager;

public class SuperpositionFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SuperpositionClient.init();
        SuperpositionClient.registerBlockEntityRenderers(BlockEntityRenderers::register);

        ClientTickEvents.END_WORLD_TICK.register(level -> {
            ClientSignalManager.tick(level);
            CableManager.clientTick(level);
            CarabinerManager.tick(level);
            SuperpositionUITooltipRenderer.clientTick(level);
        });

        HudRenderCallback.EVENT.register(SuperpositionUITooltipRenderer::renderOverlay);

        this.registerBlockRenderLayers();
    }

    private void registerBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlock(SuperpositionBlocks.SIGNAL_READOUT.get(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SuperpositionBlocks.ANTENNA.get(), RenderType.cutout());
    }
}
