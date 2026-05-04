package net.thefluffycart.loe.datagen.villager;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.block.Blocks;
import net.thefluffycart.loe.LettersOfEffection;
import net.thefluffycart.loe.block.LOEBlocks;
import net.thefluffycart.loe.items.LOEItems;

import java.util.List;
import java.util.Optional;

public class LOEVillagerTrades {
    public static final ResourceKey<VillagerTrade> POSTMASTER_1_EMERALD_INK = createKey("postmaster/1/paper");
    public static final ResourceKey<VillagerTrade> POSTMASTER_1_EMERALD_SHELF = createKey("postmaster/1/emerald_shelf");
    public static final ResourceKey<VillagerTrade> POSTMASTER_1_BOOK_EMERALD = createKey("postmaster/1/book_emerald");

    public static final ResourceKey<VillagerTrade> POSTMASTER_2_EMERALD_COMPASS = createKey("postmaster/2/emerald_compass");
    public static final ResourceKey<VillagerTrade> POSTMASTER_2_EMERALD_POSTBENCH = createKey("postmaster/2/emerald_postbench");
    public static final ResourceKey<VillagerTrade> POSTMASTER_2_RESIN_EMERALD = createKey("postmaster/2/resin_emerald");

    public static final ResourceKey<VillagerTrade> POSTMASTER_3_EMERALD_BLANKLETTER = createKey("postmaster/3/emerald_blankletter");
    public static final ResourceKey<VillagerTrade> POSTMASTER_3_EMERALD_BUNDLE = createKey("postmaster/3/emerald_bundle");

    public static final ResourceKey<VillagerTrade> POSTMASTER_4_EMERALD_LODESTONE = createKey("postmaster/4/emerald_lodestone");

    public static final ResourceKey<VillagerTrade> POSTMASTER_5_EMERALD_COPPERGOLEM = createKey("postmaster/5/emerald_coppergolem");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        register(context, POSTMASTER_1_EMERALD_INK, new VillagerTrade(
                new TradeCost(Items.EMERALD, 4),
                new ItemStackTemplate(Items.INK_SAC, 12),
                12, 10, 0.025f,
                Optional.empty(), List.of()));
        register(context, POSTMASTER_1_EMERALD_SHELF, new VillagerTrade(
                new TradeCost(Items.EMERALD, 6),
                new ItemStackTemplate(Blocks.OAK_SHELF.asItem(), 1),
                6, 18, 0.05f,
                Optional.empty(), List.of()));
        register(context, POSTMASTER_1_BOOK_EMERALD, new VillagerTrade(
                new TradeCost(Items.BOOK, 6),
                new ItemStackTemplate(Items.EMERALD, 1),
                9, 12, 0.05f,
                Optional.empty(), List.of()));

        register(context, POSTMASTER_2_EMERALD_COMPASS, new VillagerTrade(
                new TradeCost(Items.EMERALD, 12),
                new ItemStackTemplate(Items.COMPASS, 1),
                6, 18, 0.05f,
                Optional.empty(), List.of()));
        register(context, POSTMASTER_2_EMERALD_POSTBENCH, new VillagerTrade(
                new TradeCost(Items.EMERALD, 6),
                new ItemStackTemplate(LOEBlocks.POSTBENCH.asItem(), 1),
                6, 12, 0.015f,
                Optional.empty(), List.of()));
        register(context, POSTMASTER_2_RESIN_EMERALD, new VillagerTrade(
                new TradeCost(Items.RESIN_CLUMP, 8),
                new ItemStackTemplate(LOEBlocks.POSTBENCH.asItem(), 2),
                12, 10, 0.045f,
                Optional.empty(), List.of()));

        register(context, POSTMASTER_3_EMERALD_BLANKLETTER, new VillagerTrade(
                new TradeCost(Items.EMERALD, 5),
                new ItemStackTemplate(LOEItems.BLANK_LETTER),
                16, 12, 0.065f,
                Optional.empty(), List.of()));
        register(context, POSTMASTER_3_EMERALD_BUNDLE, new VillagerTrade(
                new TradeCost(Items.EMERALD, 7),
                new ItemStackTemplate(Items.BUNDLE),
                8, 12, 0.025f,
                Optional.empty(), List.of()));

        register(context, POSTMASTER_4_EMERALD_LODESTONE, new VillagerTrade(
                new TradeCost(Items.EMERALD, 7),
                new ItemStackTemplate(Blocks.LODESTONE.asItem()),
                12, 20, 0.025f,
                Optional.empty(), List.of()));

        register(context, POSTMASTER_5_EMERALD_COPPERGOLEM, new VillagerTrade(
                new TradeCost(Items.EMERALD, 7),
                new ItemStackTemplate(Blocks.OXIDIZED_COPPER_GOLEM_STATUE.asItem()),
                4, 20, 0.015f,
                Optional.empty(), List.of()));
    }


    private static ResourceKey<VillagerTrade> createKey(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, name));
    }

    private static void register(BootstrapContext<VillagerTrade> context, ResourceKey<VillagerTrade> resourceKey, VillagerTrade trade) {
        context.register(resourceKey, trade);
    }
}