package org.modogthedev.superposition.util;

import foundry.veil.api.client.color.ColorTheme;
import foundry.veil.api.client.tooltip.VeilUIItemTooltipDataHolder;
import foundry.veil.api.client.tooltip.anim.TooltipTimeline;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface SPTooltipable {
    List<Component> getTooltip();

    boolean isTooltipEnabled();
    boolean isSuperpositionTooltipEnabled();

    CompoundTag saveTooltipData();

    void loadTooltipData(CompoundTag var1);

    void setTooltip(List<Component> var1);

    void addTooltip(Component var1);

    void addTooltip(List<Component> var1);

    void addTooltip(String var1);

    ColorTheme getTheme();

    void setTheme(ColorTheme var1);

    void setBackgroundColor(int var1);

    void setTopBorderColor(int var1);

    void setBottomBorderColor(int var1);
    void drawExtra();

    boolean getWorldspace();

    TooltipTimeline getTimeline();

    ItemStack getStack();

    int getTooltipWidth();

    int getTooltipHeight();

    int getTooltipXOffset();

    int getTooltipYOffset();

    List<VeilUIItemTooltipDataHolder> getItems();
}
