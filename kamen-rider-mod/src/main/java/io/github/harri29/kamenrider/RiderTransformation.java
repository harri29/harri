package io.github.harri29.kamenrider;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.Set;

/**
 * Central transformation state/service for every Rider item.
 * Keeping this logic outside individual Drivers makes form changes and future
 * animation/network hooks much easier to add without duplicating code.
 */
public final class RiderTransformation {
    private static final String TRANSFORMED_TAG = "kamenrider.transformed";
    private static final String FORM_TAG_PREFIX = "kamenrider.form.";
    private static final int EFFECT_DURATION = 20 * 60 * 60;

    private RiderTransformation() {
    }

    public static void henshin(ServerLevel level, Player player, RiderForm form) {
        setForm(player, form);
        player.displayClientMessage(
                Component.translatable("message.kamenrider.henshin", Component.translatable(form.translationKey())),
                true
        );
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, form.soundPitch());
        level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1.0D, player.getZ(),
                40, 0.65D, 1.0D, 0.65D, 0.06D);
    }

    public static void changeForm(ServerLevel level, Player player, RiderForm form) {
        setForm(player, form);
        player.displayClientMessage(
                Component.translatable("message.kamenrider.form_change", Component.translatable(form.translationKey())),
                true
        );
        level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.9F, form.soundPitch());
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY() + 1.0D, player.getZ(),
                28, 0.55D, 0.9D, 0.55D, 0.08D);
    }

    public static void dehenshin(ServerLevel level, Player player) {
        clearTransformation(player);
        player.displayClientMessage(Component.translatable("message.kamenrider.dehenshin"), true);
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 0.8F, 1.0F);
        level.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY() + 1.0D, player.getZ(),
                24, 0.5D, 0.8D, 0.5D, 0.03D);
    }

    public static boolean isTransformed(Player player) {
        return player.getTags().contains(TRANSFORMED_TAG);
    }

    public static Optional<RiderForm> currentForm(Player player) {
        if (!isTransformed(player)) {
            return Optional.empty();
        }
        for (RiderForm form : RiderForm.values()) {
            if (player.getTags().contains(FORM_TAG_PREFIX + form.id())) {
                return Optional.of(form);
            }
        }
        return Optional.empty();
    }

    public static boolean isSeries(Player player, String seriesId) {
        return currentForm(player).map(form -> form.seriesId().equals(seriesId)).orElse(false);
    }

    private static void setForm(Player player, RiderForm form) {
        clearTransformation(player);
        player.addTag(TRANSFORMED_TAG);
        player.addTag(FORM_TAG_PREFIX + form.id());
        applyEffects(player, form);
    }

    private static void applyEffects(Player player, RiderForm form) {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EFFECT_DURATION, form.speedAmplifier(), false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, EFFECT_DURATION, form.strengthAmplifier(), false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, EFFECT_DURATION, form.resistanceAmplifier(), false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, EFFECT_DURATION, form.jumpAmplifier(), false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, EFFECT_DURATION, 0, false, false, false));
    }

    private static void clearTransformation(Player player) {
        clearRiderTags(player);
        player.removeEffect(MobEffects.MOVEMENT_SPEED);
        player.removeEffect(MobEffects.DAMAGE_BOOST);
        player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        player.removeEffect(MobEffects.JUMP);
        player.removeEffect(MobEffects.NIGHT_VISION);
    }

    private static void clearRiderTags(Player player) {
        Set<String> tags = Set.copyOf(player.getTags());
        for (String tag : tags) {
            if (tag.equals(TRANSFORMED_TAG) || tag.startsWith(FORM_TAG_PREFIX)) {
                player.removeTag(tag);
            }
        }
    }
}
