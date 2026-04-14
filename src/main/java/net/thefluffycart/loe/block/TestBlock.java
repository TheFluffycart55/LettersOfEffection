package net.thefluffycart.loe.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.thefluffycart.loe.items.LOEItems;

public class TestBlock extends Block {
    public TestBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (itemStack.is(LOEItems.SEALED_LETTER))
        {
            player.getMainHandItem().shrink(1);
            player.addItem(LOEItems.BLANK_LETTER.getDefaultInstance());
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }
}
