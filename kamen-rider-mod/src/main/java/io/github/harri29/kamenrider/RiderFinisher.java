package io.github.harri29.kamenrider;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Rider-specific finishers shared by Gaia Memories and Raise Buckles.
 * The caller owns the activation item; this service owns targeting, damage,
 * movement, FX and cooldown behavior so future finishers can reuse one path.
 */
public final class RiderFinisher {
    private static final int MAXIMUM_DRIVE_COOLDOWN = 20 * 6;
    private static final int BUCKLE_FINISHER_COOLDOWN = 20 * 7;

    private RiderFinisher() {
    }

    public static void maximumDrive(
            ServerLevel level,
            Player player,
            RiderForm form,
            DoubleMemoryItem.Memory memory,
            Item sourceItem
    ) {
        if (player.getCooldowns().isOnCooldown(sourceItem)) {
            player.displayClientMessage(Component.translatable("message.kamenrider.finisher_cooldown"), true);
            return;
        }

        int hits = switch (memory) {
            case CYCLONE -> cycloneDrive(level, player);
            case HEAT -> heatDrive(level, player);
            case LUNA -> lunaDrive(level, player);
            case JOKER -> jokerDrive(level, player);
            case METAL -> metalDrive(level, player);
            case TRIGGER -> triggerDrive(level, player);
        };

        player.getCooldowns().addCooldown(sourceItem, MAXIMUM_DRIVE_COOLDOWN);
        player.displayClientMessage(
                Component.translatable(
                        "message.kamenrider.maximum_drive",
                        Component.translatable(memory.translationKey()),
                        Component.translatable(form.translationKey()),
                        hits
                ),
                true
        );
    }

    public static void buckleFinisher(
            ServerLevel level,
            Player player,
            RiderForm form,
            GeatsBuckleItem.Buckle buckle,
            Item sourceItem
    ) {
        if (player.getCooldowns().isOnCooldown(sourceItem)) {
            player.displayClientMessage(Component.translatable("message.kamenrider.finisher_cooldown"), true);
            return;
        }

        int hits = switch (buckle) {
            case MAGNUM -> magnumStrike(level, player);
            case NINJA -> ninjaStrike(level, player);
            case BOOST -> boostGrandStrike(level, player);
            case ZOMBIE -> zombieStrike(level, player);
        };

        player.getCooldowns().addCooldown(sourceItem, BUCKLE_FINISHER_COOLDOWN);
        player.displayClientMessage(
                Component.translatable(
                        "message.kamenrider.buckle_finisher",
                        buckleName(buckle),
                        Component.translatable(form.translationKey()),
                        hits
                ),
                true
        );
    }

    private static int cycloneDrive(ServerLevel level, Player player) {
        int hits = radial(level, player, 5.0D, 20.0F, 1.45D);
        level.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getY() + 1.0D, player.getZ(),
                80, 1.4D, 0.8D, 1.4D, 0.12D);
        level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1.0D, player.getZ(),
                36, 1.0D, 0.8D, 1.0D, 0.08D);
        level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.45F);
        return hits;
    }

    private static int heatDrive(ServerLevel level, Player player) {
        int hits = cone(level, player, 5.5D, 0.18D, 29.0F, 1.25D);
        Vec3 look = player.getLookAngle().normalize();
        level.sendParticles(ParticleTypes.FLAME, player.getX() + look.x * 2.0D, player.getY() + 1.0D,
                player.getZ() + look.z * 2.0D, 72, 1.0D, 0.8D, 1.0D, 0.08D);
        level.sendParticles(ParticleTypes.LAVA, player.getX() + look.x * 1.8D, player.getY() + 0.8D,
                player.getZ() + look.z * 1.8D, 18, 0.7D, 0.5D, 0.7D, 0.03D);
        level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.0F, 0.9F);
        return hits;
    }

    private static int lunaDrive(ServerLevel level, Player player) {
        int hits = beam(level, player, 14.0D, 0.88D, 25.0F);
        trace(level, player, ParticleTypes.END_ROD, 14.0D, 0.65D);
        level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.4F);
        return hits;
    }

    private static int jokerDrive(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        player.setDeltaMovement(player.getDeltaMovement().add(look.scale(1.35D)).add(0.0D, 0.22D, 0.0D));
        player.hurtMarked = true;
        int hits = cone(level, player, 5.6D, 0.25D, 35.0F, 1.7D);
        level.sendParticles(ParticleTypes.CRIT, player.getX() + look.x * 2.2D, player.getY() + 0.9D,
                player.getZ() + look.z * 2.2D, 85, 0.9D, 0.7D, 0.9D, 0.13D);
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.9F, 1.28F);
        return hits;
    }

    private static int metalDrive(ServerLevel level, Player player) {
        int hits = cone(level, player, 4.6D, 0.08D, 33.0F, 2.2D);
        Vec3 look = player.getLookAngle().normalize();
        level.sendParticles(ParticleTypes.CRIT, player.getX() + look.x * 1.5D, player.getY() + 1.0D,
                player.getZ() + look.z * 1.5D, 96, 1.0D, 0.8D, 1.0D, 0.1D);
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.9F, 0.72F);
        return hits;
    }

    private static int triggerDrive(ServerLevel level, Player player) {
        int hits = beam(level, player, 18.0D, 0.94D, 31.0F);
        trace(level, player, ParticleTypes.ELECTRIC_SPARK, 18.0D, 0.7D);
        level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.0F, 1.5F);
        return hits;
    }

    private static int magnumStrike(ServerLevel level, Player player) {
        int hits = beam(level, player, 20.0D, 0.95D, 33.0F);
        trace(level, player, ParticleTypes.ELECTRIC_SPARK, 20.0D, 0.7D);
        level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.0F, 1.2F);
        return hits;
    }

    private static int ninjaStrike(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        player.setDeltaMovement(player.getDeltaMovement().add(look.scale(1.15D)));
        player.hurtMarked = true;
        int hits = cone(level, player, 5.4D, 0.12D, 29.0F, 1.1D);
        level.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + look.x * 1.8D, player.getY() + 1.0D,
                player.getZ() + look.z * 1.8D, 44, 0.9D, 0.7D, 0.9D, 0.04D);
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX() + look.x * 2.0D, player.getY() + 1.0D,
                player.getZ() + look.z * 2.0D, 34, 0.8D, 0.6D, 0.8D, 0.08D);
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.55F);
        return hits;
    }

    private static int boostGrandStrike(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        player.setDeltaMovement(player.getDeltaMovement().add(look.scale(1.55D)).add(0.0D, 0.3D, 0.0D));
        player.hurtMarked = true;
        int hits = cone(level, player, 6.8D, 0.32D, 41.0F, 2.0D);
        level.sendParticles(ParticleTypes.FLAME, player.getX() + look.x * 2.2D, player.getY() + 0.8D,
                player.getZ() + look.z * 2.2D, 90, 1.0D, 0.7D, 1.0D, 0.12D);
        level.sendParticles(ParticleTypes.CRIT, player.getX() + look.x * 2.4D, player.getY() + 1.0D,
                player.getZ() + look.z * 2.4D, 60, 0.9D, 0.8D, 0.9D, 0.1D);
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.1F, 1.3F);
        return hits;
    }

    private static int zombieStrike(ServerLevel level, Player player) {
        int hits = radial(level, player, 4.3D, 38.0F, 2.25D);
        level.sendParticles(ParticleTypes.CRIT, player.getX(), player.getY() + 0.7D, player.getZ(),
                110, 1.1D, 0.65D, 1.1D, 0.12D);
        level.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY() + 0.5D, player.getZ(),
                45, 1.0D, 0.4D, 1.0D, 0.06D);
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0F, 0.68F);
        return hits;
    }

    private static int cone(ServerLevel level, Player player, double range, double threshold, float damage, double knockback) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.position().add(0.0D, 1.0D, 0.0D);
        AABB box = player.getBoundingBox().inflate(range);
        int hits = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
            Vec3 to = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D).subtract(origin);
            if (to.lengthSqr() < 0.01D || to.length() > range || to.normalize().dot(look) < threshold) continue;
            if (target.hurt(player.damageSources().playerAttack(player), damage)) {
                target.push(look.x * knockback, 0.28D, look.z * knockback);
                hits++;
            }
        }
        return hits;
    }

    private static int radial(ServerLevel level, Player player, double range, float damage, double knockback) {
        AABB box = player.getBoundingBox().inflate(range);
        int hits = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
            Vec3 away = target.position().subtract(player.position());
            double distance = away.length();
            if (distance < 0.01D || distance > range) continue;
            Vec3 push = away.normalize().scale(knockback);
            if (target.hurt(player.damageSources().playerAttack(player), damage)) {
                target.push(push.x, 0.32D, push.z);
                hits++;
            }
        }
        return hits;
    }

    private static int beam(ServerLevel level, Player player, double range, double threshold, float damage) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.getEyePosition();
        int hits = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(range), e -> e != player && e.isAlive())) {
            Vec3 to = target.getEyePosition().subtract(origin);
            double distance = to.length();
            if (distance < 0.01D || distance > range || to.normalize().dot(look) < threshold) continue;
            if (target.hurt(player.damageSources().playerAttack(player), damage)) hits++;
        }
        return hits;
    }

    private static void trace(ServerLevel level, Player player, net.minecraft.core.particles.SimpleParticleType particle,
                              double range, double step) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.getEyePosition();
        int points = Math.max(1, (int) (range / step));
        for (int i = 1; i <= points; i++) {
            double d = i * step;
            level.sendParticles(particle,
                    origin.x + look.x * d,
                    origin.y + look.y * d,
                    origin.z + look.z * d,
                    1, 0.01D, 0.01D, 0.01D, 0.0D);
        }
    }

    private static Component buckleName(GeatsBuckleItem.Buckle buckle) {
        return Component.translatable("buckle.kamenrider." + buckle.name().toLowerCase());
    }
}
