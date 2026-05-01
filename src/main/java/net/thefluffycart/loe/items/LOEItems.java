package net.thefluffycart.loe.items;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.thefluffycart.loe.LettersOfEffection;
import net.thefluffycart.loe.items.custom.BlankLetterItem;
import net.thefluffycart.loe.items.custom.SealedLetterItem;
import net.thefluffycart.loe.data.BlankLetterContent;
import net.thefluffycart.loe.data.LOEDataComponents;

import java.util.function.Function;

public class LOEItems {
    public static final Item BLANK_LETTER = register("blank_letter", BlankLetterItem::new, (new Item.Properties()).stacksTo(1).component(LOEDataComponents.BLANK_LETTER_CONTENT, BlankLetterContent.EMPTY));
    public static final Item SEALED_LETTER = register("sealed_letter", SealedLetterItem::new, (new Item.Properties()).stacksTo(16).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false));

    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, name));
        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {
        LettersOfEffection.LOGGER.info("Registering Items for LOE");

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> creativeTab.accept(BLANK_LETTER));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
                .register((creativeTab) -> creativeTab.accept(SEALED_LETTER));
    }
}
