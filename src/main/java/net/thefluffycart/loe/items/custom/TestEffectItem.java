package net.thefluffycart.loe.items.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.network.Filterable;
import net.minecraft.server.network.FilteredText;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.thefluffycart.loe.LettersOfEffection;
import net.thefluffycart.loe.data.LOEDataComponents;
import net.thefluffycart.loe.data.LetterEffectContent;
import net.thefluffycart.loe.data.SealedLetterContent;
import net.thefluffycart.loe.items.LOEItems;

import javax.xml.crypto.Data;
import java.util.List;

public class TestEffectItem extends Item {

    public TestEffectItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack offhand = player.getOffhandItem();
        ItemStack mainhand = player.getItemInHand(hand);

        if (offhand.is(ItemTags.FLOWERS) && mainhand.is(this)) {
            var stewEffects = offhand.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
            if (stewEffects != null) {
                List<LetterEffectContent.Entry> converted = stewEffects.effects().stream()
                        .map(effects -> new LetterEffectContent.Entry(effects.effect(), effects.duration())).toList();
                mainhand.set(LOEDataComponents.LETTER_EFFECT_CONTENT, new LetterEffectContent(converted));
                LettersOfEffection.LOGGER.info(LOEDataComponents.LETTER_EFFECT_CONTENT.toString());
            }
        }


        return super.use(level, player, hand);
    }
}