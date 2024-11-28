//package org.modogthedev.superposition.platform.forge;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.server.level.ServerPlayer;
//import net.neoforged.bus.api.IEventBus;
//import org.modogthedev.superposition.client.renderer.ui.SuperpositionUITooltipRenderer;
//
//public class PlatformHelperImpl {
//    public static void init() {
//        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        modEventBus.addListener(PlatformHelperImpl::registerGuiOverlays);
//    }
//
//    public static void setScreen(Screen screen) {
//        Minecraft.getInstance().setScreen(screen);
//    }
//
//    private static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
//        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "uitooltip", SuperpositionUITooltipRenderer::renderOverlay);
//    }
//
//    public static void register() {
//        PlatformHelperImpl.init();
//    }
//
//    public static double getPlayerReach(ServerPlayer player) {
//        return player.getBlockReach();
//    }
//}
