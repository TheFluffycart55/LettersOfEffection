package net.thefluffycart.loe.datagen.villager;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;
import net.thefluffycart.loe.util.LOETags;

import java.util.concurrent.CompletableFuture;

public class LOEVillagerTradeTags extends FabricTagsProvider<VillagerTrade> {
    public LOEVillagerTradeTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.VILLAGER_TRADE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(LOETags.Trades.POSTMASTER_LEVEL_1)
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_1_EMERALD_INK.identifier()))
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_1_EMERALD_SHELF.identifier()))
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_1_BOOK_EMERALD.identifier()));

        getOrCreateRawBuilder(LOETags.Trades.POSTMASTER_LEVEL_2)
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_2_EMERALD_COMPASS.identifier()))
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_2_EMERALD_POSTBENCH.identifier()))
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_2_RESIN_EMERALD.identifier()));

        getOrCreateRawBuilder(LOETags.Trades.POSTMASTER_LEVEL_3)
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_3_EMERALD_BLANKLETTER.identifier()))
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_3_EMERALD_BUNDLE.identifier()));

        getOrCreateRawBuilder(LOETags.Trades.POSTMASTER_LEVEL_4)
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_4_EMERALD_LODESTONE.identifier()));

        getOrCreateRawBuilder(LOETags.Trades.POSTMASTER_LEVEL_5)
                .add(TagEntry.element(LOEVillagerTrades.POSTMASTER_5_EMERALD_COPPERGOLEM.identifier()));
    }
}