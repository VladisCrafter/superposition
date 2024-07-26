package org.modogthedev.superposition;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.TickEvent;
import foundry.veil.api.client.color.Color;
import foundry.veil.api.client.color.ColorTheme;
import net.minecraft.resources.ResourceLocation;
import org.modogthedev.superposition.core.*;
import org.modogthedev.superposition.system.signal.SignalManager;
import org.slf4j.Logger;

public class Superposition {
    public static final String MODID = "superposition";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ColorTheme SUPERPOSITION_THEME = new ColorTheme();

    public static void init() {
        LOGGER.info("Superposition is initializing!");

        initTheme();
        SuperpositionBlocks.BLOCKS.register();
        SuperpositionTabs.TABS.register();
        SuperpositionItems.ITEMS.register();
        SuperpositionSounds.SOUNDS.register();
        SuperpositionMessages.register();
        TickEvent.SERVER_LEVEL_POST.register(SignalManager::tick);

        LOGGER.info("Superposition has been initialized!");
    }

    public static void initTheme() {
        SUPERPOSITION_THEME.addColor(new Color(50, 168, 82,200));
        SUPERPOSITION_THEME.addColor(new Color(60, 186, 94,255));
        SUPERPOSITION_THEME.addColor(new Color(44, 150, 72,255));
        SUPERPOSITION_THEME.addColor("background",new Color(50, 168, 82,200));
        SUPERPOSITION_THEME.addColor("topBorder",new Color(60, 186, 94,255));
        SUPERPOSITION_THEME.addColor("bottomBorder",new Color(44, 150, 72,255));
    }

    public static ResourceLocation id(String loc) {
        return new ResourceLocation(MODID, loc);
    }
}
