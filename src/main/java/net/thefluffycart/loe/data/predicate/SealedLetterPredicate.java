package net.thefluffycart.loe.data.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.CollectionPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.SingleComponentItemPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.WrittenBookPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.component.WrittenBookContent;
import net.thefluffycart.loe.data.LOEDataComponents;
import net.thefluffycart.loe.data.SealedLetterContent;

import java.util.Optional;
import java.util.function.Predicate;


public record SealedLetterPredicate(
        Optional<CollectionPredicate<Filterable<Component>, SealedLetterPredicate.PagePredicate>> pages,
        Optional<String> author,
        Optional<String> title,
        MinMaxBounds.Ints generation,
        Optional<Boolean> resolved
) implements SingleComponentItemPredicate<SealedLetterContent> {
    public static final Codec<SealedLetterPredicate> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                            CollectionPredicate.codec(SealedLetterPredicate.PagePredicate.CODEC).optionalFieldOf("pages").forGetter(SealedLetterPredicate::pages),
                            Codec.STRING.optionalFieldOf("author").forGetter(SealedLetterPredicate::author),
                            Codec.STRING.optionalFieldOf("title").forGetter(SealedLetterPredicate::title),
                            MinMaxBounds.Ints.CODEC.optionalFieldOf("generation", MinMaxBounds.Ints.ANY).forGetter(SealedLetterPredicate::generation),
                            Codec.BOOL.optionalFieldOf("resolved").forGetter(SealedLetterPredicate::resolved)
                    )
                    .apply(i, SealedLetterPredicate::new)
    );

    @Override
    public DataComponentType<SealedLetterContent> componentType() {
        return LOEDataComponents.SEALED_LETTER_CONTENT;
    }

    public boolean matches(final SealedLetterContent value) {
        if (this.author.isPresent() && !((String)this.author.get()).equals(value.author())) {
            return false;
        } else if (this.title.isPresent() && !((String)this.title.get()).equals(value.title().raw())) {
            return false;
        } else if (!this.generation.matches(value.generation())) {
            return false;
        } else {
            return this.resolved.isPresent() && this.resolved.get() != value.resolved()
                    ? false
                    : !this.pages.isPresent() || ((CollectionPredicate)this.pages.get()).test((Iterable)value.pages());
        }
    }

    public record PagePredicate(Component contents) implements Predicate<Filterable<Component>> {
        public static final Codec<SealedLetterPredicate.PagePredicate> CODEC = ComponentSerialization.CODEC
                .xmap(SealedLetterPredicate.PagePredicate::new, SealedLetterPredicate.PagePredicate::contents);

        public boolean test(final Filterable<Component> value) {
            return value.raw().equals(this.contents);
        }
    }
}
