package org.modogthedev.superposition.system.filter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.modogthedev.superposition.core.SuperpositionItems;
import org.modogthedev.superposition.screens.WidgetScreen;
import org.modogthedev.superposition.system.signal.Signal;
import org.modogthedev.superposition.util.SuperpositionMth;

import java.awt.*;
import java.util.List;

public class LowPassFilter extends Filter {
    float frequency;

    public LowPassFilter(ResourceLocation filter) {
        super(filter);
    }

    public LowPassFilter() {
        super();
    }

    @Override
    public boolean passSignal(Signal signal) {
        return signal.getFrequency() < (Math.abs(158 - frequency) * 100000);
    }

    @Override
    public void save(CompoundTag tag) {
        tag.putFloat("frequency", frequency);
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag != null) {
            frequency = tag.getFloat("frequency");
        }
    }

    @Override
    public String toString() {
        return "Low Pass Filter - " + SuperpositionMth.frequencyToHzReadable(frequency);
    }

    @Override
    public String getTooltip() {
        return SuperpositionMth.frequencyToHzReadable(frequency);
    }

    @Override
    public void updateFromDials(List<WidgetScreen.Dial> dialList) {
        frequency = dialList.get(0).scrolledAmount;
    }

    @Override
    public void updateDials(List<WidgetScreen.Dial> dials) {
        dials.get(0).scrolledAmount = frequency;
    }

    @Override
    public Color getColor() {
        return new Color(40, 68, 164);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(SuperpositionItems.LOW_PASS_FILTER.get());
    }

    @Override
    public Filter create() {
        return new LowPassFilter(getSelfReference());
    }
}