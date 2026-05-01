package net.thefluffycart.loe.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.thefluffycart.loe.LettersOfEffection;
import net.thefluffycart.loe.data.BlankLetterContent;
import net.thefluffycart.loe.data.LOEDataComponents;
import net.thefluffycart.loe.data.SealedLetterContent;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class LetterViewScreen extends Screen {
    public static final int PAGE_INDICATOR_TEXT_Y_OFFSET = 16;
    public static final int PAGE_TEXT_X_OFFSET = 36;
    public static final int PAGE_TEXT_Y_OFFSET = 30;
    private static final int BACKGROUND_TEXTURE_WIDTH = 256;
    private static final int BACKGROUND_TEXTURE_HEIGHT = 256;
    private static final Component TITLE = Component.translatable("letter.view.title");
    private static final Style PAGE_TEXT_STYLE;
    public static final LetterViewScreen.LetterAccess EMPTY_ACCESS;
    public static final Identifier LETTER_LOCATION;
    protected static final int TEXT_WIDTH = 114;
    protected static final int TEXT_HEIGHT = 128;
    protected static final int IMAGE_WIDTH = 192;
    private static final int PAGE_INDICATOR_X_OFFSET = 148;
    protected static final int IMAGE_HEIGHT = 192;
    private static final int PAGE_BUTTON_Y = 157;
    private static final int PAGE_BACK_BUTTON_X = 43;
    private static final int PAGE_FORWARD_BUTTON_X = 116;
    private LetterViewScreen.LetterAccess letterAccess;
    private int currentPage;
    private List<FormattedCharSequence> cachedPageComponents;
    private int cachedPage;
    private Component pageMsg;
    private PageButton forwardButton;
    private PageButton backButton;
    private final boolean playTurnSound;

    public LetterViewScreen(final LetterViewScreen.LetterAccess letterAccess) {
        this(letterAccess, true);
    }

    public LetterViewScreen() {
        this(EMPTY_ACCESS, false);
    }

    private LetterViewScreen(final LetterViewScreen.LetterAccess letterAccess, final boolean playTurnSound) {
        super(TITLE);
        this.cachedPageComponents = Collections.emptyList();
        this.cachedPage = -1;
        this.pageMsg = CommonComponents.EMPTY;
        this.letterAccess = letterAccess;
        this.playTurnSound = playTurnSound;
    }

    public void setLetterAccess(final LetterViewScreen.LetterAccess letterAccess) {
        this.letterAccess = letterAccess;
        this.currentPage = Mth.clamp(this.currentPage, 0, letterAccess.getPageCount());
        this.updateButtonVisibility();
        this.cachedPage = -1;
    }

    public boolean setPage(final int page) {
        int clampedPage = Mth.clamp(page, 0, this.letterAccess.getPageCount() - 1);
        if (clampedPage != this.currentPage) {
            this.currentPage = clampedPage;
            this.updateButtonVisibility();
            this.cachedPage = -1;
            return true;
        } else {
            return false;
        }
    }

    protected boolean forcePage(final int page) {
        return this.setPage(page);
    }

    protected void init() {
        this.createMenuControls();
        this.createPageControlButtons();
    }

    public Component getNarrationMessage() {
        return CommonComponents.joinLines(new Component[]{super.getNarrationMessage(), this.getPageNumberMessage(), this.letterAccess.getPage(this.currentPage)});
    }

    private Component getPageNumberMessage() {
        return Component.translatable("letter.pageIndicator", new Object[]{this.currentPage + 1, Math.max(this.getNumPages(), 1)}).withStyle(PAGE_TEXT_STYLE);
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).pos((this.width - 200) / 2, this.menuControlsTop()).width(200).build());
    }

    protected void createPageControlButtons() {
        int left = this.backgroundLeft();
        int top = this.backgroundTop();
        this.forwardButton = (PageButton)this.addRenderableWidget(new PageButton(left + 116, top + 157, true, (button) -> this.pageForward(), this.playTurnSound));
        this.backButton = (PageButton)this.addRenderableWidget(new PageButton(left + 43, top + 157, false, (button) -> this.pageBack(), this.playTurnSound));
        this.updateButtonVisibility();
    }

    private int getNumPages() {
        return this.letterAccess.getPageCount();
    }

    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < this.getNumPages() - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    public boolean keyPressed(final KeyEvent event) {
        if (super.keyPressed(event)) {
            return true;
        } else {
            boolean var10000;
            switch (event.key()) {
                case 266:
                    this.backButton.onPress(event);
                    var10000 = true;
                    break;
                case 267:
                    this.forwardButton.onPress(event);
                    var10000 = true;
                    break;
                default:
                    var10000 = false;
            }

            return var10000;
        }
    }

    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.visitText(graphics.textRenderer(GuiGraphicsExtractor.HoveredTextEffects.TOOLTIP_AND_CURSOR), false);
    }

    private void visitText(final ActiveTextCollector collector, final boolean clickableOnly) {
        if (this.cachedPage != this.currentPage) {
            FormattedText pageText = ComponentUtils.mergeStyles(this.letterAccess.getPage(this.currentPage), PAGE_TEXT_STYLE);
            this.cachedPageComponents = this.font.split(pageText, 114);
            this.pageMsg = this.getPageNumberMessage();
            this.cachedPage = this.currentPage;
        }

        int left = this.backgroundLeft();
        int top = this.backgroundTop();
        if (!clickableOnly) {
            collector.accept(TextAlignment.RIGHT, left + 148, top + 16, this.pageMsg);
        }

        Objects.requireNonNull(this.font);
        int shownLines = Math.min(210 / 30, this.cachedPageComponents.size());

        for(int i = 0; i < shownLines; ++i) {
            FormattedCharSequence component = (FormattedCharSequence)this.cachedPageComponents.get(i);
            int var10001 = left + 36;
            int var10002 = top + 30;
            Objects.requireNonNull(this.font);
            collector.accept(var10001, var10002 + i * 9, component);
        }

    }

    public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, LETTER_LOCATION, this.backgroundLeft(), this.backgroundTop(), 0.0F, 0.0F, 192, 192, 256, 256);
    }

    private int backgroundLeft() {
        return (this.width - 192) / 2;
    }

    private int backgroundTop() {
        return 12;
    }

    protected int menuControlsTop() {
        return this.backgroundTop() + 148 + 2;
    }

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

    protected boolean handleClickEvent(final @Nullable ClickEvent event) {
        if (event == null) {
            return false;
        } else {
            LocalPlayer player = (LocalPlayer)Objects.requireNonNull(this.minecraft.player, "Player not available");

            switch (event) {
                case ClickEvent.ChangePage(int page) when true:
                    this.forcePage(page - 1);
                    return true;
                case ClickEvent.RunCommand(String command):
                    this.closeContainerOnServer();
                    clickCommandAction(player, command, (Screen)null);
                    return true;
                default:
                    defaultHandleGameClickEvent(event, this.minecraft, this);
                    return true;
            }
        }
    }

    protected void closeContainerOnServer() {
    }

    public boolean isInGameUi() {
        return true;
    }

    static {
        PAGE_TEXT_STYLE = Style.EMPTY.withoutShadow().withColor(-16777216);
        EMPTY_ACCESS = new LetterViewScreen.LetterAccess(List.of());
        LETTER_LOCATION = Identifier.fromNamespaceAndPath(LettersOfEffection.MOD_ID, "textures/gui/letter.png");
    }

    @Environment(EnvType.CLIENT)
    public static record LetterAccess(List<Component> pages) {
        public LetterAccess(List<Component> pages) {
            this.pages = pages;
        }

        public int getPageCount() {
            return this.pages.size();
        }

        public Component getPage(final int page) {
            return page >= 0 && page < this.getPageCount() ? (Component)this.pages.get(page) : CommonComponents.EMPTY;
        }
        public static LetterViewScreen.LetterAccess fromItem(final ItemStack itemStack) {
            boolean filterEnabled = Minecraft.getInstance().isTextFilteringEnabled();
            SealedLetterContent writtenContent = itemStack.get(LOEDataComponents.SEALED_LETTER_CONTENT);
            return new LetterViewScreen.LetterAccess(writtenContent.getPages(filterEnabled));
        }
        public List<Component> pages() {
            return this.pages;
        }
    }
}