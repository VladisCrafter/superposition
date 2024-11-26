package org.modogthedev.superposition.core;

import dev.architectury.registry.registries.DeferredRegister;
import foundry.veil.platform.registry.RegistrationProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.modogthedev.superposition.Superposition;
import org.modogthedev.superposition.system.cable.Cable;
import org.modogthedev.superposition.system.cards.Card;
import org.modogthedev.superposition.system.filter.Filter;

public class SuperpositionRegistries {
    protected static final ResourceKey<Registry<Filter>> FILTER_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Superposition.MODID,"filter"));
    protected static final ResourceKey<Registry<Card>> CARD_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Superposition.MODID,"card"));
    public static void register() {
        Superposition.LOGGER.info("Registering Registries!");
        RegistrationProvider.get(FILTER_REGISTRY, Superposition.MODID);
        RegistrationProvider.get(CARD_REGISTRY, Superposition.MODID);
    }
}