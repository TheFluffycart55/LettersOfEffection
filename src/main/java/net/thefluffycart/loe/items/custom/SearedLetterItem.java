package net.thefluffycart.loe.items.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.Optional;
import java.util.function.Function;

public class SearedLetterItem extends Item {

    public SearedLetterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide())
        {
            {
                ItemStack itemStack = player.getItemInHand(hand);
                itemStack.consume(1, player);
                level.explode(null, null, (ExplosionDamageCalculator)null, player.getX(), player.getY(), player.getZ(), 1.5F, false, Level.ExplosionInteraction.TRIGGER);
                player.getCooldowns().addCooldown(itemStack, 60);
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(level, player, hand);
    }
}
