package net.thefluffycart.loe.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thefluffycart.loe.data.BlankLetterContent;
import net.thefluffycart.loe.data.LOEDataComponents;
import net.thefluffycart.loe.items.LOEItems;
import net.thefluffycart.loe.screen.LetterEditScreen;
import net.thefluffycart.loe.screen.LetterViewScreen;

@Environment(EnvType.CLIENT)
public class LOEClientHelper {

    public static void openSealed(final Level level, final Player player, final InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide() && stack.get(LOEDataComponents.SEALED_LETTER_CONTENT) != null && stack.is(LOEItems.SEALED_LETTER))
        {
            Minecraft.getInstance().setScreen(new LetterViewScreen(LetterViewScreen.LetterAccess.fromItem(stack)));
        }
    }

    public static void openBlank(final Level level, final Player player, final InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        BlankLetterContent content = stack.get(LOEDataComponents.BLANK_LETTER_CONTENT);
        if (level.isClientSide() && stack.get(LOEDataComponents.BLANK_LETTER_CONTENT) != null && stack.is(LOEItems.BLANK_LETTER))
        {
            Minecraft.getInstance().setScreen(new LetterEditScreen((player), stack, hand, content));
        }
    }
}
