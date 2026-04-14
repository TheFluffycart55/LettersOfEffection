package net.thefluffycart.loe.screen;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.thefluffycart.loe.data.BlankLetterContent;
import net.thefluffycart.loe.data.LOEDataComponents;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class LetterEditScreen extends Screen {
    public static final int TEXT_WIDTH = 114;
    public static final int TEXT_HEIGHT = 126;
    public static final int IMAGE_WIDTH = 192;
    public static final int IMAGE_HEIGHT = 192;
    public static final int BACKGROUND_TEXTURE_WIDTH = 256;
    public static final int BACKGROUND_TEXTURE_HEIGHT = 256;
    private static final int MENU_BUTTON_MARGIN = 4;
    private static final int MENU_BUTTON_SIZE = 98;
    private static final int PAGE_BUTTON_Y = 157;
    private static final int PAGE_BACK_BUTTON_X = 43;
    private static final int PAGE_FORWARD_BUTTON_X = 116;
    private static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;
    private static final int PAGE_INDICATOR_X_OFFSET = 148;
    private static final Component TITLE = Component.translatable("book.edit.title");
    private static final Component SIGN_BOOK_LABEL = Component.translatable("book.signButton");
    private final Player owner;
    private final ItemStack book;
    private final LetterSignScreen signScreen;
    private int currentPage;
    private final List<String> pages = Lists.newArrayList();
    private final InteractionHand hand;
    private Component numberOfPages;
    private MultiLineEditBox page;

    public LetterEditScreen(final Player owner, final ItemStack book, final InteractionHand hand, final BlankLetterContent content) {
        super(TITLE);
        this.numberOfPages = CommonComponents.EMPTY;
        this.owner = owner;
        this.book = book;
        this.hand = hand;
        Stream var10000 = content.getPages(Minecraft.getInstance().isTextFilteringEnabled());
        List var10001 = this.pages;
        Objects.requireNonNull(var10001);
        var10000.forEach(var10001::add);
        if (this.pages.isEmpty()) {
            this.pages.add("");
        }

        this.signScreen = new LetterSignScreen(this, owner, hand, this.pages);
    }

    private int getNumPages() {
        return this.pages.size();
    }

    protected void init() {
        int left = this.backgroundLeft();
        int top = this.backgroundTop();
        int padding = 8;
        this.page = MultiLineEditBox.builder().setShowDecorations(false).setTextColor(-16777216).setCursorColor(-16777216).setShowBackground(false).setTextShadow(false).setX((this.width - 114) / 2 - 8).setY(28).build(this.font, 122, 134, CommonComponents.EMPTY);
        this.page.setCharacterLimit(1024);
        MultiLineEditBox var10000 = this.page;
        Objects.requireNonNull(this.font);
        var10000.setLineLimit(126 / 9);
        this.page.setValueListener((value) -> this.pages.set(this.currentPage, value));
        this.addRenderableWidget(this.page);
        this.updatePageContent();
        this.numberOfPages = this.getPageNumberMessage();
        this.addRenderableWidget(Button.builder(SIGN_BOOK_LABEL, (button) -> this.minecraft.setScreen(this.signScreen)).pos(this.width / 2 - 98 - 2, this.menuControlsTop()).width(98).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.minecraft.setScreen((Screen)null);
            this.saveChanges();
        }).pos(this.width / 2 + 2, this.menuControlsTop()).width(98).build());
    }

    private int backgroundLeft() {
        return (this.width - 192) / 2;
    }

    private int backgroundTop() {
        return 2;
    }

    private int menuControlsTop() {
        return this.backgroundTop() + 192 + 2;
    }

    protected void setInitialFocus() {
        this.setInitialFocus(this.page);
    }

    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(new Component[]{super.getNarrationMessage(), this.getPageNumberMessage()});
    }

    private Component getPageNumberMessage() {
        return Component.translatable("book.pageIndicator", new Object[]{this.currentPage + 1, this.getNumPages()}).withColor(-16777216).withoutShadow();
    }

    private void updatePageContent() {
        this.page.setValue((String)this.pages.get(this.currentPage), true);
        this.numberOfPages = this.getPageNumberMessage();
    }

    private void eraseEmptyTrailingPages() {
        ListIterator<String> pagesIt = this.pages.listIterator(this.pages.size());

        while(pagesIt.hasPrevious() && ((String)pagesIt.previous()).isEmpty()) {
            pagesIt.remove();
        }

    }

    private void saveChanges() {
        this.eraseEmptyTrailingPages();
        this.updateLocalCopy();
        int slot = this.hand == InteractionHand.MAIN_HAND ? this.owner.getInventory().getSelectedSlot() : 40;
        this.minecraft.getConnection().send(new ServerboundEditBookPacket(slot, this.pages, Optional.empty()));
    }

    private void updateLocalCopy() {
        this.book.set(LOEDataComponents.BLANK_LETTER_CONTENT, new BlankLetterContent(this.pages.stream().map(Filterable::passThrough).toList()));
    }

    public boolean isInGameUi() {
        return true;
    }

    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.visitText(graphics.textRenderer());
    }

    private void visitText(final ActiveTextCollector collector) {
        int left = this.backgroundLeft();
        int top = this.backgroundTop();
        collector.accept(TextAlignment.RIGHT, left + 148, top + 16, this.numberOfPages);
    }

    public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, LetterViewScreen.LETTER_LOCATION, this.backgroundLeft(), this.backgroundTop(), 0.0F, 0.0F, 192, 192, 256, 256);
    }
}
