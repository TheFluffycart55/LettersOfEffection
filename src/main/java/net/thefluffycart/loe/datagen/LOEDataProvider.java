package net.thefluffycart.loe.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class LOEDataProvider extends FabricDynamicRegistryProvider {
    public LOEDataProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.VILLAGER_TRADE));
        entries.addAll(registries.lookupOrThrow(Registries.TRADE_SET));
    }

    @Override
    public String getName() {
        return "MCCourse Dynamic Registry";
    }
}