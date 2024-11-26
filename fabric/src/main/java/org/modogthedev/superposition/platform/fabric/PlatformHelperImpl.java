package org.modogthedev.superposition.platform.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.player.Player;
import org.modogthedev.superposition.client.renderer.ui.SuperpositionUITooltipRenderer;

import java.util.Objects;

public class PlatformHelperImpl {
    public static void register() {
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            Minecraft client = Minecraft.getInstance();

            SuperpositionUITooltipRenderer.renderOverlay(client.gui, matrices, tickDelta, client.getWindow().getGuiScaledWidth(), client.getWindow().getGuiScaledHeight());
        });
    }

    public static double getPlayerReach(ServerPlayer player) {
        return 5; //TODO: find actual value
    }
}
