package net.thefluffycart.loe.data;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.thefluffycart.loe.LettersOfEffection;

import java.util.function.UnaryOperator;

public class LOEDataComponents {
    public static final DataComponentType<BlankLetterContent> BLANK_LETTER_CONTENT = register("blank_letter_content", (b) -> b.persistent(BlankLetterContent.CODEC).networkSynchronized(BlankLetterContent.STREAM_CODEC).cacheEncoding());
    public static final DataComponentType<SealedLetterContent> SEALED_LETTER_CONTENT = register("sealed_letter_content", (b) -> b.persistent(SealedLetterContent.CODEC).networkSynchronized(SealedLetterContent.STREAM_CODEC).cacheEncoding());

    public LOEDataComponents() {
    }

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, name, (builder.apply(DataComponentType.builder())).build());

    }

    public static void initialize() {
        LettersOfEffection.LOGGER.info("Registering Components for LOE");
    }
}
