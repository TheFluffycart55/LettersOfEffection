package net.thefluffycart.loe.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.component.BookContent;

import java.util.List;
import java.util.stream.Stream;

public record LetterContent (List<Filterable<String>> pages) implements BookContent<String, LetterContent> {
    public static final LetterContent EMPTY = new LetterContent(List.of());
    public static final int PAGE_EDIT_LENGTH = 1024;
    public static final int MAX_PAGES = 100;
    private static final Codec<Filterable<String>> PAGE_CODEC = Filterable.codec(Codec.string(0, 1024));
    public static final Codec<List<Filterable<String>>> PAGES_CODEC = PAGE_CODEC.sizeLimitedListOf(100);
    public static final Codec<LetterContent> CODEC = RecordCodecBuilder.create(
            i -> i.group(PAGES_CODEC.optionalFieldOf("pages", List.of()).forGetter(LetterContent::pages)).apply(i, LetterContent::new)
    );
    public static final StreamCodec<ByteBuf, LetterContent> STREAM_CODEC = Filterable.streamCodec(ByteBufCodecs.stringUtf8(1024))
            .apply(ByteBufCodecs.list(100))
            .map(LetterContent::new, LetterContent::pages);

    public LetterContent(List<Filterable<String>> pages) {
        if (pages.size() > 100) {
            throw new IllegalArgumentException("Got " + pages.size() + " pages, but maximum is 100");
        } else {
            this.pages = pages;
        }
    }

    public Stream<String> getPages(final boolean filterEnabled) {
        return this.pages.stream().map(page -> (String)page.get(filterEnabled));
    }

    public LetterContent withReplacedPages(final List<Filterable<String>> newPages) {
        return new LetterContent(newPages);
    }
}
