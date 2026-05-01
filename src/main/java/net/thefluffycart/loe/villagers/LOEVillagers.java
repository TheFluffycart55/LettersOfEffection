package net.thefluffycart.loe.villagers;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.thefluffycart.loe.LettersOfEffection;
import net.thefluffycart.loe.block.LOEBlocks;
import net.thefluffycart.loe.datagen.villager.LOETradeSets;

public class LOEVillagers {
    public static final ResourceKey<PoiType> POSTBENCH_POI_KEY = registerPoiKey("postbench_poi");
    public static final PoiType POSTBENCH_POI = registerPOI("postbench_poi", LOEBlocks.POST_STATION);

    public static final ResourceKey<VillagerProfession> POSTMASTER_KEY =
            ResourceKey.create(Registries.VILLAGER_PROFESSION, Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, "postmaster"));
    public static final VillagerProfession POSTMASTER = registerVillagerProfession("postmaster", new VillagerProfession(
            Component.literal("Postmaster"), holder -> holder.is(POSTBENCH_POI_KEY), holder -> holder.is(POSTBENCH_POI_KEY),
            ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_LIBRARIAN, Int2ObjectMap.ofEntries(
            Int2ObjectMap.entry(1, LOETradeSets.POSTMASTER_LEVEL_1),
            Int2ObjectMap.entry(2, LOETradeSets.POSTMASTER_LEVEL_2))));


    private static VillagerProfession registerVillagerProfession(String name, VillagerProfession profession) {
        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, name), profession);
    }

    private static PoiType registerPOI(String name, Block block) {
        return PoiHelper.register(Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, name),
                1, 1, block);
    }

    private static ResourceKey<PoiType> registerPoiKey(String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, name));
    }

    public static void registerVillagers() {
        LettersOfEffection.LOGGER.info("Registering Villagers and POIs for " + LettersOfEffection.MOD_ID);
    }
}