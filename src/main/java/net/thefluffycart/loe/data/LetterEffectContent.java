package net.thefluffycart.loe.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record LetterEffectContent (
        List<LetterEffectContent.Entry> effects) implements ConsumableListener, TooltipProvider {
    public static final LetterEffectContent EMPTY = new LetterEffectContent(List.of());
    public static final int DEFAULT_DURATION = 160;
    public static final Codec<LetterEffectContent> CODEC = LetterEffectContent.Entry.CODEC
            .listOf()
            .xmap(LetterEffectContent::new, LetterEffectContent::effects);
    public static final StreamCodec<RegistryFriendlyByteBuf, LetterEffectContent> STREAM_CODEC = LetterEffectContent.Entry.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(LetterEffectContent::new, LetterEffectContent::effects);

    public LetterEffectContent withEffectAdded(final LetterEffectContent.Entry entry) {
        return new LetterEffectContent(Util.copyAndAdd(this.effects, entry));
    }

    @Override
    public void onConsume(final Level level, final LivingEntity user, final ItemStack stack, final Consumable consumable) {
        for (LetterEffectContent.Entry effect : this.effects) {
            user.addEffect(effect.createEffectInstance());
        }
    }

    @Override
    public void addToTooltip(final Item.TooltipContext context, final Consumer<Component> consumer, final TooltipFlag flag, final DataComponentGetter components) {
        if (flag.isCreative()) {
            List<MobEffectInstance> effectInstances = new ArrayList();

            for (LetterEffectContent.Entry effect : this.effects) {
                effectInstances.add(effect.createEffectInstance());
            }

            PotionContents.addPotionTooltip(effectInstances, consumer, 1.0F, context.tickRate());
        }
    }

    public record Entry(Holder<MobEffect> effect, int duration) {
        public static final Codec<LetterEffectContent.Entry> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                MobEffect.CODEC.fieldOf("id").forGetter(LetterEffectContent.Entry::effect),
                                Codec.INT.lenientOptionalFieldOf("duration", 160).forGetter(LetterEffectContent.Entry::duration)
                        )
                        .apply(i, LetterEffectContent.Entry::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, LetterEffectContent.Entry> STREAM_CODEC = StreamCodec.composite(
                MobEffect.STREAM_CODEC, LetterEffectContent.Entry::effect, ByteBufCodecs.VAR_INT, LetterEffectContent.Entry::duration, LetterEffectContent.Entry::new
        );

        public MobEffectInstance createEffectInstance() {
            return new MobEffectInstance(this.effect, this.duration);
        }
    }
}