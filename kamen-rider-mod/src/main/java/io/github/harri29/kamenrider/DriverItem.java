package io.github.harri29.kamenrider;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class DriverItem extends Item {
    private static final String TRANSFORMED_TAG = "kamenrider.transformed";
    private static final String FORM_TAG_PREFIX = "kamenrider.form.";
    private static final int EFFECT_DURATION = 20 * 60 * 60;
    private static final int FINISHER_COOLDOWN = 20 * 5;

    private final RiderForm form;

    public DriverItem(RiderForm form, Properties properties) {
        super(properties);
        this.form = form;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            if (player.isShiftKeyDown() && isUsingForm(player, form)) {
                riderKick(serverLevel, player);
            } else if (isUsingForm(player, form)) {
                dehenshin(serverLevel, player);
            } else {
                if (isTransformed(player)) {
                    clearTransformation(player);
                }
                henshin(serverLevel, player);
            }
        }

        return InteractionResultHolder.success(stack);
    }

    private void henshin(ServerLevel level, Player player) {
        clearRiderTags(player);
        player.addTag(TRANSFORMED_TAG);
        player.addTag(FORM_TAG_PREFIX + form.id());

        applyEffects(player);
        player.displayClientMessage(
                Component.translatable("message.kamenrider.henshin", Component.translatable(form.translationKey())),
                true
        );

        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, form.soundPitch());
        level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1.0D, player.getZ(),
                40, 0.65D, 1.0D, 0.65D, 0.06D);
    }

    private void dehenshin(ServerLevel level, Player player) {
        clearTransformation(player);
        player.displayClientMessage(Component.translatable("message.kamenrider.dehenshin"), true);
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 0.8F, 1.0F);
        level.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY() + 1.0D, player.getZ(),
                24, 0.5D, 0.8D, 0.5D, 0.03D);
    }

    private void riderKick(ServerLevel level, Player player) {
        if (player.getCooldowns().isOnCooldown(this)) {
            player.displayClientMessage(Component.translatable("message.kamenrider.finisher_cooldown"), true);
            return;
        }

        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.position().add(0.0D, 1.0D, 0.0D);
        AABB searchBox = player.getBoundingBox().inflate(5.0D);
        int hits = 0;

        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, searchBox,
                entity -> entity != player && entity.isAlive())) {
            Vec3 toTarget = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D).subtract(origin);
            if (toTarget.lengthSqr() > 0.01D && toTarget.normalize().dot(look) > 0.35D) {
                if (target.hurt(player.damageSources().playerAttack(player), form.kickDamage())) {
                    Vec3 knockback = look.scale(1.4D).add(0.0D, 0.35D, 0.0D);
                    target.push(knockback.x, knockback.y, knockback.z);
                    hits++;
                }
            }
        }

        player.setDeltaMovement(player.getDeltaMovement().add(look.scale(1.15D)).add(0.0D, 0.22D, 0.0D));
        player.hurtMarked = true;
        player.getCooldowns().addCooldown(this, FINISHER_COOLDOWN);

        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0F, 1.25F);
        level.sendParticles(ParticleTypes.CRIT, player.getX() + look.x * 2.0D, player.getY() + 1.0D,
                player.getZ() + look.z * 2.0D, 55, 0.8D, 0.8D, 0.8D, 0.12D);
        level.sendParticles(ParticleTypes.FLAME, player.getX() + look.x * 2.0D, player.getY() + 0.8D,
                player.getZ() + look.z * 2.0D, 24, 0.45D, 0.45D, 0.45D, 0.05D);

        player.displayClientMessage(
                Component.translatable("message.kamenrider.rider_kick", Component.translatable(form.translationKey()), hits),
                true
        );
    }

    private void applyEffects(Player player) {
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

    private static boolean isTransformed(Player player) {
        return player.getTags().contains(TRANSFORMED_TAG);
    }

    private static boolean isUsingForm(Player player, RiderForm form) {
        return isTransformed(player) && player.getTags().contains(FORM_TAG_PREFIX + form.id());
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
