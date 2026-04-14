package net.thefluffycart.loe.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.ResolutionContext;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.network.Filterable;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.BookContent;
import net.minecraft.world.item.component.WritableBookContent;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public record BlankLetterContent(List<Filterable<String>> pages) implements BookContent<String, BlankLetterContent> {
    public static final BlankLetterContent EMPTY = new BlankLetterContent(List.of());
    public static final int PAGE_EDIT_LENGTH = 1024;
    public static final int MAX_PAGES = 100;
    private static final Codec<Filterable<String>> PAGE_CODEC = Filterable.codec(Codec.string(0, 1024));
    public static final Codec<List<Filterable<String>>> PAGES_CODEC;
    public static final Codec<BlankLetterContent> CODEC;
    public static final StreamCodec<ByteBuf, BlankLetterContent> STREAM_CODEC;

    public BlankLetterContent(List<Filterable<String>> pages) {
        if (pages.size() > 100) {
            throw new IllegalArgumentException("Got " + pages.size() + " pages, but maximum is 100");
        } else {
            this.pages = pages;
        }
    }

    public Stream<String> getPages(final boolean filterEnabled) {
        return this.pages.stream().map((page) -> (String)page.get(filterEnabled));
    }

    public BlankLetterContent withReplacedPages(final List<Filterable<String>> newPages) {
        return new BlankLetterContent(newPages);
    }

    public List<Filterable<String>> pages() {
        return this.pages;
    }

    static {
        PAGES_CODEC = PAGE_CODEC.sizeLimitedListOf(100);
        CODEC = RecordCodecBuilder.create((i) -> i.group(PAGES_CODEC.optionalFieldOf("pages", List.of()).forGetter(BlankLetterContent::pages)).apply(i, BlankLetterContent::new));
        STREAM_CODEC = Filterable.streamCodec(ByteBufCodecs.stringUtf8(1024)).apply(ByteBufCodecs.list(100)).map(BlankLetterContent::new, BlankLetterContent::pages);
    }
}