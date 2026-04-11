package net.thefluffycart.loe.data.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.CollectionPredicate;
import net.minecraft.advancements.criterion.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.network.Filterable;
import net.thefluffycart.loe.data.BlankLetterContent;
import net.thefluffycart.loe.data.LOEDataComponents;

import java.util.Optional;
import java.util.function.Predicate;

public record BlankLetterPredicate(
        Optional<CollectionPredicate<Filterable<String>, BlankLetterPredicate.PagePredicate>> pages) implements SingleComponentItemPredicate<BlankLetterContent> {
    public static final Codec<BlankLetterPredicate> CODEC = RecordCodecBuilder.create((i) -> i.group(CollectionPredicate.codec(BlankLetterPredicate.PagePredicate.CODEC).optionalFieldOf("pages").forGetter(BlankLetterPredicate::pages)).apply(i, BlankLetterPredicate::new));

    public BlankLetterPredicate(Optional<CollectionPredicate<Filterable<String>, BlankLetterPredicate.PagePredicate>> pages) {
        this.pages = pages;
    }

    public DataComponentType<BlankLetterContent> componentType() {
        return LOEDataComponents.BLANK_LETTER_CONTENT;
    }

    public boolean matches(final BlankLetterContent value) {
        return !this.pages.isPresent() || ((CollectionPredicate)this.pages.get()).test(value.pages());
    }

    public Optional<CollectionPredicate<Filterable<String>, BlankLetterPredicate.PagePredicate>> pages() {
        return this.pages;
    }

    public static record PagePredicate(String contents) implements Predicate<Filterable<String>> {
        public static final Codec<BlankLetterPredicate.PagePredicate> CODEC;

        public PagePredicate(String contents) {
            this.contents = contents;
        }

        public boolean test(final Filterable<String> value) {
            return ((String)value.raw()).equals(this.contents);
        }

        public String contents() {
            return this.contents;
        }

        static {
            CODEC = Codec.STRING.xmap(BlankLetterPredicate.PagePredicate::new, BlankLetterPredicate.PagePredicate::contents);
        }
    }
}
