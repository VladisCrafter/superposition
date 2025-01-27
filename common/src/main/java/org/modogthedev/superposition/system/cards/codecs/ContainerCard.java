package org.modogthedev.superposition.system.cards.codecs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.modogthedev.superposition.blockentity.AnalyserBlockEntity;
import org.modogthedev.superposition.system.cards.Card;
import org.modogthedev.superposition.system.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class ContainerCard extends Card {
    public ContainerCard(ResourceLocation card) {
        super(card);
    }

    public ContainerCard(Card card) {
        super(card);
    }

    @Override
    public void modulateSignal(Signal signal, Signal periphrealSignal) {
        if (periphrealBlockEntity instanceof AnalyserBlockEntity analyserBlockEntity) { //TODO: fix
            BlockEntity blockEntity1 = periphrealBlockEntity.getLevel().getBlockEntity(analyserBlockEntity.getAnalysisPosition());
            if (blockEntity1 instanceof BaseContainerBlockEntity container) {
                List<ItemStack> stacks = new ArrayList<>();
                CompoundTag tag = new CompoundTag();
                for (int i = 0; i < container.getContainerSize(); i++) {
                    ItemStack stack = container.getItem(i);
                    if (!stack.is(Items.AIR)) {
                        String key = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
                        if (!tag.contains(key)) {
                            tag.putInt(key, stack.getCount());
                        } else {
                            tag.putInt(key, stack.getCount() + tag.getInt(key));
                        }
                    }
                }
                signal.encode(tag);
            }
        }
    }

    @Override
    public boolean requiresPeriphreal() {
        return true;
    }

    @Override
    public Card copy() {
        return new ContainerCard(this);
    }
}
