package net.thefluffycart.loe.mixin;

import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.thefluffycart.loe.data.LOEDataComponents;
import net.thefluffycart.loe.items.custom.BlankLetterItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerIMPLMixin {
    @Shadow
    public ServerPlayer player;

    @Inject(method = "handleEditBook", at = @At("HEAD"))
    private void onEditBook(ServerboundEditBookPacket packet, CallbackInfo ci) {
        int slot = packet.slot();
        List<String> pages = packet.pages();
        Optional<String> title = packet.title();
        ItemStack stack = player.getInventory().getItem(slot);
        if (stack.getItem() instanceof BlankLetterItem item && !title.isEmpty() && stack.has(LOEDataComponents.BLANK_LETTER_CONTENT)) {
            item.signItem(FilteredText.passThrough(title.get()), pages.stream().map(FilteredText::passThrough).toList(), player, slot);
        }
    }
}
