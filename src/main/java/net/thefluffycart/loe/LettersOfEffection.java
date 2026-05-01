package net.thefluffycart.loe;

import net.fabricmc.api.ModInitializer;

import net.thefluffycart.loe.block.LOEBlocks;
import net.thefluffycart.loe.items.LOEItems;
import net.thefluffycart.loe.data.LOEDataComponents;
import net.thefluffycart.loe.villagers.LOEVillagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LettersOfEffection implements ModInitializer {
	public static final String MOD_ID = "loe";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOEDataComponents.initialize();
		LOEItems.initialize();
		LOEBlocks.initialize();
		LOEVillagers.registerVillagers();
		LOGGER.info("Hello Fabric world!");
	}
}