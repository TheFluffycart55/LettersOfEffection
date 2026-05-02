package net.thefluffycart.loe.items.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ResolutionContext;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.server.network.FilteredText;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thefluffycart.loe.data.BlankLetterContent;
import net.thefluffycart.loe.data.LOEDataComponents;
import net.thefluffycart.loe.data.SealedLetterContent;
import net.thefluffycart.loe.screen.LetterEditScreen;
import net.thefluffycart.loe.items.LOEItems;
import net.thefluffycart.loe.util.LOEClientHelper;

import java.util.List;

public class BlankLetterItem extends Item {
    public BlankLetterItem(final Item.Properties properties) {
        super(properties);
    }

    public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
        if (level.isClientSide())
        {
            ItemStack itemStack = player.getItemInHand(hand);
            BlankLetterContent content = itemStack.get(LOEDataComponents.BLANK_LETTER_CONTENT);
            if (content != null) {
                LOEClientHelper.openBlank(level, player, hand);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResult.SUCCESS;
        }
        else
        {
            ItemStack itemStack = player.getItemInHand(hand);
            if (itemStack.has(LOEDataComponents.BLANK_LETTER_CONTENT)) {
                if (SealedLetterContent.resolveForItem(itemStack, ResolutionContext.create(((ServerPlayer) player).createCommandSourceStack()), (player).registryAccess())) {
                    (player).containerMenu.broadcastChanges();
                }
                ((ServerPlayer) player).connection.send(new ClientboundOpenBookPacket(hand));
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResult.SUCCESS_SERVER;
        }
    }

    public void signItem(FilteredText title, List<FilteredText> contents, Player player, int slot)
    {
        ItemStack carried = player.getInventory().getItem(slot);
        BlankLetterContent isSigned = carried.get(LOEDataComponents.BLANK_LETTER_CONTENT);
        if (!player.level().isClientSide() && title != null)
        {
            ItemStack sealedLetter = carried.transmuteCopy(LOEItems.SEALED_LETTER);
            sealedLetter.remove(LOEDataComponents.SEALED_LETTER_CONTENT);
            List<Filterable<Component>> pages = contents.stream().map(page -> this.filterableFromOutgoing(page, player).map(text -> (Component) Component.literal(text))).toList();
            sealedLetter.set(LOEDataComponents.SEALED_LETTER_CONTENT, new SealedLetterContent(this.filterableFromOutgoing(title, player), player.getPlainTextName(), 0, pages, true));
            player.getInventory().setItem(slot, sealedLetter);
        }
    }

    private Filterable<String> filterableFromOutgoing(final FilteredText text, Player player) {
        return player.isTextFilteringEnabled() ? Filterable.passThrough(text.filteredOrEmpty()) : Filterable.from(text);
    }
}