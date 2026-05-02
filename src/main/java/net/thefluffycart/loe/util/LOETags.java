package net.thefluffycart.loe.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.VillagerTrade;
import net.thefluffycart.loe.LettersOfEffection;

public class LOETags {
    public static class Trades {
        public static final TagKey<VillagerTrade> POSTMASTER_LEVEL_1 = createTag("postmaster/level_1");
        public static final TagKey<VillagerTrade> POSTMASTER_LEVEL_2 = createTag("postmaster/level_2");
        public static final TagKey<VillagerTrade> POSTMASTER_LEVEL_3 = createTag("postmaster/level_3");
        public static final TagKey<VillagerTrade> POSTMASTER_LEVEL_4 = createTag("postmaster/level_4");
        public static final TagKey<VillagerTrade> POSTMASTER_LEVEL_5 = createTag("postmaster/level_5");


        private static TagKey<VillagerTrade> createTag(String name) {
            return TagKey.create(Registries.VILLAGER_TRADE, Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, name));
        }
    }
}
