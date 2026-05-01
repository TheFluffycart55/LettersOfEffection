package net.thefluffycart.loe;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.thefluffycart.loe.datagen.LOEDataProvider;
import net.thefluffycart.loe.datagen.LOEModelProvider;
import net.thefluffycart.loe.datagen.villager.LOEPoiTags;
import net.thefluffycart.loe.datagen.villager.LOETradeSets;
import net.thefluffycart.loe.datagen.villager.LOEVillagerTradeTags;
import net.thefluffycart.loe.datagen.villager.LOEVillagerTrades;

public class LettersOfEffectionDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(LOEModelProvider::new);
		pack.addProvider(LOEVillagerTradeTags::new);
		pack.addProvider(LOEPoiTags::new);
		pack.addProvider(LOEDataProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.VILLAGER_TRADE, LOEVillagerTrades::bootstrap);
		registryBuilder.add(Registries.TRADE_SET, LOETradeSets::bootstrap);
	}
}
