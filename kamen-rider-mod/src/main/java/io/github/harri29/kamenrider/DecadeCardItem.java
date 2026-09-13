package io.github.harri29.kamenrider;

import io.github.harri29.kamenrider.network.RiderNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Reusable foundation for Decade Attack Ride / Final Attack Ride cards. */
public final class DecadeCardItem extends Item {
    public enum CardAction {
        ATTACK_RIDE_SLASH,
        ATTACK_RIDE_BLAST,
        FINAL_ATTACK_RIDE
    }

    private final CardAction action;

    public DecadeCardItem(CardAction action, Properties properties) {
        super(properties);
        this.action = action;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            if (RiderTransformation.currentForm(player).orElse(null) != RiderForm.DECADE) {
                player.displayClientMessage(Component.translatable("message.kamenrider.card_requires_decade"), true);
                return InteractionResultHolder.success(stack);
            }
            if (player.getCooldowns().isOnCooldown(this)) {
                player.displayClientMessage(Component.translatable("message.kamenrider.card_cooldown"), true);
                return InteractionResultHolder.success(stack);
            }

            int hits = switch (action) {
                case ATTACK_RIDE_SLASH -> attackRideSlash(serverLevel, player);
                case ATTACK_RIDE_BLAST -> attackRideBlast(serverLevel, player);
                case FINAL_ATTACK_RIDE -> finalAttackRide(serverLevel, player);
            };
            player.displayClientMessage(
                    Component.translatable("message.kamenrider.card_activate", stack.getHoverName(), hits),
                    true
            );
        }
        return InteractionResultHolder.success(stack);
    }

    private int attackRideSlash(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        int hits = hitCone(level, player, 5.2D, 0.0D, 19.0F, 1.30D);
        level.sendParticles(ParticleTypes.CRIT,
                player.getX() + look.x * 2.4D, player.getY() + 1.0D, player.getZ() + look.z * 2.4D,
                72, 1.2D, 0.8D, 1.2D, 0.08D);
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.25F, 0.72F);
        player.getCooldowns().addCooldown(this, 70);
        return hits;
    }

    private int attackRideBlast(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.getEyePosition();
        int hits = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(14.0D),
                entity -> entity != player && entity.isAlive())) {
            Vec3 toTarget = target.getEyePosition().subtract(origin);
            double distance = toTarget.length();
            if (distance < 0.01D || distance > 14.0D || toTarget.normalize().dot(look) < 0.91D) continue;
            if (target.hurt(player.damageSources().playerAttack(player), 14.0F)) {
                target.push(look.x * 0.65D, 0.10D, look.z * 0.65D);
                hits++;
            }
        }
        for (int i = 1; i <= 20; i++) {
            double d = i * 0.70D;
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    origin.x + look.x * d, origin.y + look.y * d, origin.z + look.z * d,
                    1, 0.02D, 0.02D, 0.02D, 0.0D);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.0F, 1.15F);
        player.getCooldowns().addCooldown(this, 80);
        return hits;
    }

    private int finalAttackRide(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        player.setDeltaMovement(player.getDeltaMovement().add(look.scale(1.55D)).add(0.0D, 0.28D, 0.0D));
        player.hurtMarked = true;

        int hits = hitCone(level, player, 5.8D, 0.20D, 28.0F, 1.85D);
        level.sendParticles(ParticleTypes.END_ROD,
                player.getX() + look.x * 2.0D, player.getY() + 0.9D, player.getZ() + look.z * 2.0D,
                96, 1.0D, 0.9D, 1.0D, 0.15D);
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                player.getX(), player.getY() + 1.0D, player.getZ(),
                64, 0.8D, 1.1D, 0.8D, 0.12D);
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.9F, 1.10F);
        player.getCooldowns().addCooldown(this, 140);
        RiderNetworking.presentation(player, "final_attack_ride");
        return hits;
    }

    private int hitCone(ServerLevel level, Player player, double range, double facingThreshold, float damage, double knockback) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.position().add(0.0D, 1.0D, 0.0D);
        AABB box = player.getBoundingBox().inflate(range);
        int hits = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box,
                entity -> entity != player && entity.isAlive())) {
            Vec3 toTarget = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D).subtract(origin);
            if (toTarget.lengthSqr() < 0.01D || toTarget.length() > range || toTarget.normalize().dot(look) < facingThreshold) continue;
            if (target.hurt(player.damageSources().playerAttack(player), damage)) {
                target.push(look.x * knockback, 0.24D, look.z * knockback);
                hits++;
            }
        }
        return hits;
    }
}
