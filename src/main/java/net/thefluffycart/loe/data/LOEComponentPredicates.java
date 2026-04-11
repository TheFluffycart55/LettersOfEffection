package net.thefluffycart.loe.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.thefluffycart.loe.LettersOfEffection;
import net.thefluffycart.loe.data.predicate.BlankLetterPredicate;
import net.thefluffycart.loe.data.predicate.SealedLetterPredicate;

public class LOEComponentPredicates {
    public static final DataComponentPredicate.Type<BlankLetterPredicate> BLANK_LETTER_PREDICATE;
    public static final DataComponentPredicate.Type<SealedLetterPredicate> SEALED_LETTER_PREDICATE;

    private static <T extends DataComponentPredicate> DataComponentPredicate.Type<T> register(String id, Codec<T> codec) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_PREDICATE_TYPE, id, new DataComponentPredicate.ConcreteType<>(codec));
    }

    public static void initialize() {
        LettersOfEffection.LOGGER.info("Registering Predicates for LOE");
    }

    static {
        BLANK_LETTER_PREDICATE = register("blank_letter_content", BlankLetterPredicate.CODEC);
        SEALED_LETTER_PREDICATE = register("sealed_letter_content", SealedLetterPredicate.CODEC);
    }
}
