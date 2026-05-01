package net.thefluffycart.loe.datagen.villager;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.thefluffycart.loe.LettersOfEffection;
import net.thefluffycart.loe.block.LOEBlocks;

import java.util.List;
import java.util.Optional;

public class LOEVillagerTrades {
    public static final ResourceKey<VillagerTrade> POSTMASTER_1_EMERALD_BISMUTH = createKey("postmaster/1/idamon");
    public static final ResourceKey<VillagerTrade> POSTMASTER_1_EMERALD_RAW_BISMUTH = createKey("postmaster/1/emerald_raw_bismuth");
    public static final ResourceKey<VillagerTrade> POSTMASTER_2_EMERALD_CHAIR = createKey("postmaster/2/emerald_chair");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        register(context, POSTMASTER_1_EMERALD_BISMUTH, new VillagerTrade(
                new TradeCost(Items.EMERALD, 6),
                new ItemStackTemplate(Items.EMERALD, 4),
                12, 19, 0.05f,
                Optional.empty(), List.of()));
        register(context, POSTMASTER_1_EMERALD_RAW_BISMUTH, new VillagerTrade(
                new TradeCost(Items.DIAMOND, 5),
                new ItemStackTemplate(Items.EMERALD, 12),
                12, 23, 0.05f,
                Optional.empty(), List.of()));

        register(context, POSTMASTER_2_EMERALD_CHAIR, new VillagerTrade(
                new TradeCost(Items.EMERALD, 24),
                new ItemStackTemplate(LOEBlocks.LETTER_OPENER.asItem()),
                12, 24, 0.05f,
                Optional.empty(), List.of()));
    }


    private static ResourceKey<VillagerTrade> createKey(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, name));
    }

    private static void register(BootstrapContext<VillagerTrade> context, ResourceKey<VillagerTrade> resourceKey, VillagerTrade trade) {
        context.register(resourceKey, trade);
    }
}