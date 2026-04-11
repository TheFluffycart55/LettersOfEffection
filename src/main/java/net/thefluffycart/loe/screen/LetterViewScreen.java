package net.thefluffycart.loe.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import net.thefluffycart.loe.LettersOfEffection;
import net.thefluffycart.loe.data.BlankLetterContent;
import net.thefluffycart.loe.data.LOEDataComponents;
import net.thefluffycart.loe.data.SealedLetterContent;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LetterViewScreen extends Screen {
    public static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;
    private static final int BACKGROUND_TEXTURE_WIDTH = 256;
    private static final int BACKGROUND_TEXTURE_HEIGHT = 256;
    private static final Component TITLE = Component.translatable("book.view.title");
    private static final Style PAGE_TEXT_STYLE = Style.EMPTY.withoutShadow().withColor(-16777216);
    public static final LetterViewScreen.LetterAccess EMPTY_ACCESS = new LetterViewScreen.LetterAccess(List.of());
    public static final Identifier BOOK_LOCATION = Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, "textures/gui/blank_letter.png");
    protected static final int TEXT_WIDTH = 114;
    protected static final int TEXT_HEIGHT = 128;
    protected static final int IMAGE_WIDTH = 192;
    private static final int PAGE_INDICATOR_X_OFFSET = 148;
    protected static final int IMAGE_HEIGHT = 192;
    private static final int PAGE_BUTTON_Y = 157;
    private LetterViewScreen.LetterAccess LetterAccess;
    private int currentPage;
    private List<FormattedCharSequence> cachedPageComponents = Collections.emptyList();
    private int cachedPage = -1;
    private Component pageMsg = CommonComponents.EMPTY;
    private PageButton forwardButton;
    private PageButton backButton;
    private final boolean playTurnSound;

    public LetterViewScreen(final LetterViewScreen.LetterAccess LetterAccess) {
        this(LetterAccess, true);
    }

    public LetterViewScreen() {
        this(EMPTY_ACCESS, false);
    }

    private LetterViewScreen(final LetterViewScreen.LetterAccess LetterAccess, final boolean playTurnSound) {
        super(TITLE);
        this.LetterAccess = LetterAccess;
        this.playTurnSound = playTurnSound;
    }

    public void setLetterAccess(final LetterViewScreen.LetterAccess LetterAccess) {
        this.LetterAccess = LetterAccess;
        this.currentPage = Mth.clamp(this.currentPage, 0, LetterAccess.getPageCount());
        this.cachedPage = -1;
    }

    public boolean setPage(final int page) {
        int clampedPage = Mth.clamp(page, 0, this.LetterAccess.getPageCount() - 1);
        if (clampedPage != this.currentPage) {
            this.currentPage = clampedPage;
            this.cachedPage = -1;
            return true;
        } else {
            return false;
        }
    }

    protected boolean forcePage(final int page) {
        return this.setPage(page);
    }

    @Override
    protected void init() {
        this.createMenuControls();
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinLines(super.getNarrationMessage(), this.getPageNumberMessage(), this.LetterAccess.getPage(this.currentPage));
    }

    private Component getPageNumberMessage() {
        return Component.translatable("book.pageIndicator", this.currentPage + 1, Math.max(this.getNumPages(), 1)).withStyle(PAGE_TEXT_STYLE);
    }

    protected void createMenuControls() {
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).pos((this.width - 200) / 2, this.menuControlsTop()).width(200).build()
        );
    }

    private int getNumPages() {
        return this.LetterAccess.getPageCount();
    }

    @Override
    public boolean keyPressed(final KeyEvent event) {
        if (super.keyPressed(event)) {
            return true;
        } else {
            return switch (event.key()) {
                case 266 -> {
                    this.backButton.onPress(event);
                    yield true;
                }
                case 267 -> {
                    this.forwardButton.onPress(event);
                    yield true;
                }
                default -> false;
            };
        }
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.visitText(graphics.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.TOOLTIP_AND_CURSOR), false);
    }

    private void visitText(final ActiveTextCollector collector, final boolean clickableOnly) {
        if (this.cachedPage != this.currentPage) {
            FormattedText pageText = ComponentUtils.mergeStyles(this.LetterAccess.getPage(this.currentPage), PAGE_TEXT_STYLE);
            this.cachedPageComponents = this.font.split(pageText, 114);
            this.pageMsg = this.getPageNumberMessage();
            this.cachedPage = this.currentPage;
        }

        int left = this.backgroundLeft();
        int top = this.backgroundTop();
        if (!clickableOnly) {
            collector.accept(TextAlignment.RIGHT, left + 148, top + 16, this.pageMsg);
        }

        int shownLines = Math.min(54 / 9, this.cachedPageComponents.size());

        for (int i = 0; i < shownLines; i++) {
            FormattedCharSequence component = (FormattedCharSequence)this.cachedPageComponents.get(i);
            collector.accept(left + 36, top + 30 + i * 9, component);
        }
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BOOK_LOCATION, this.backgroundLeft(), this.backgroundTop(), 0.0F, 0.0F, 192, 192, 256, 256);
    }

    private int backgroundLeft() {
        return (this.width - 192) / 2;
    }

    private int backgroundTop() {
        return 2;
    }

    protected int menuControlsTop() {
        return this.backgroundTop() + 192 + 2;
    }

    @Override
    public boolean mouseClicked(final MouseButtonEvent event, final boolean doubleClick) {
        if (event.button() == 0) {
            ActiveTextCollector.ClickableStyleFinder finder = new ActiveTextCollector.ClickableStyleFinder(this.font, (int)event.x(), (int)event.y());
            this.visitText(finder, true);
            Style clickedStyle = finder.result();
            if (clickedStyle != null && this.handleClickEvent(clickedStyle.getClickEvent())) {
                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    protected boolean handleClickEvent(@Nullable final ClickEvent event) {
        if (event == null) {
            return false;
        } else {
            LocalPlayer player = (LocalPlayer) Objects.requireNonNull(this.minecraft.player, "Player not available");
            Objects.requireNonNull(event);
            switch (event) {
                case ClickEvent.ChangePage(int var15):
                    int var12 = var15;
                    if (true) {
                        this.forcePage(var12 - 1);
                        break;
                    }

                    byte var4 = 1;
                    break;
                case ClickEvent.RunCommand(String var9):
                    this.closeContainerOnServer();
                    clickCommandAction(player, var9, null);
                    break;
                default:
                    defaultHandleGameClickEvent(event, this.minecraft, this);
            }

            return true;
        }
    }

    protected void closeContainerOnServer() {
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    public record LetterAccess(List<Component> pages) {
        public int getPageCount() {
            return this.pages.size();
        }

        public Component getPage(final int page) {
            return page >= 0 && page < this.getPageCount() ? (Component)this.pages.get(page) : CommonComponents.EMPTY;
        }

        public static LetterViewScreen.LetterAccess fromItem(final ItemStack itemStack) {
            boolean filterEnabled = Minecraft.getInstance().isTextFilteringEnabled();
            BlankLetterContent writtenContent = itemStack.get(LOEDataComponents.BLANK_LETTER_CONTENT);
            if (writtenContent != null && writtenContent.title() != null) {
                return new LetterViewScreen.LetterAccess(writtenContent.getPages(filterEnabled));
            } else {
                BlankLetterContent writableContent = itemStack.get(LOEDataComponents.BLANK_LETTER_CONTENT);
                return writableContent != null ? new LetterViewScreen.LetterAccess(writableContent.getPages(filterEnabled)) : null;
            }
        }
    }
}