package net.thefluffycart.loe.items.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thefluffycart.loe.data.LOEDataComponents;
import net.thefluffycart.loe.screen.BlankLetterViewScreen;
import net.thefluffycart.loe.screen.LetterViewScreen;

public class SealedLetterItem extends Item {
    public SealedLetterItem(final Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide() && stack.get(LOEDataComponents.SEALED_LETTER_CONTENT) != null)
        {
            Minecraft.getInstance().setScreen(new LetterViewScreen(LetterViewScreen.LetterAccess.fromItem(stack)));
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS;
    }
}
