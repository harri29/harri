package io.github.harri29.kamenrider;

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

/** Ride Booker with sword mode normally and gun mode while sneaking. */
public final class DecadeWeaponItem extends Item {
    public DecadeWeaponItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            if (RiderTransformation.currentForm(player).orElse(null) != RiderForm.DECADE) {
                player.displayClientMessage(
                        Component.translatable("message.kamenrider.weapon_wrong_form", Component.translatable(RiderForm.DECADE.translationKey())),
                        true
                );
                return InteractionResultHolder.success(stack);
            }
            if (player.getCooldowns().isOnCooldown(this)) {
                player.displayClientMessage(Component.translatable("message.kamenrider.weapon_cooldown"), true);
                return InteractionResultHolder.success(stack);
            }

            int hits = player.isShiftKeyDown() ? gunMode(serverLevel, player) : swordMode(serverLevel, player);
            player.displayClientMessage(Component.translatable("message.kamenrider.weapon_attack", stack.getHoverName(), hits), true);
        }
        return InteractionResultHolder.success(stack);
    }

    private int swordMode(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.position().add(0.0D, 1.0D, 0.0D);
        int hits = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(4.2D),
                entity -> entity != player && entity.isAlive())) {
            Vec3 toTarget = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D).subtract(origin);
            if (toTarget.length() > 4.2D || toTarget.lengthSqr() < 0.01D || toTarget.normalize().dot(look) < 0.15D) continue;
            if (target.hurt(player.damageSources().playerAttack(player), 15.0F)) {
                target.push(look.x * 1.05D, 0.22D, look.z * 1.05D);
                hits++;
            }
        }
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 0.88F);
        level.sendParticles(ParticleTypes.CRIT,
                player.getX() + look.x * 2.0D, player.getY() + 1.0D, player.getZ() + look.z * 2.0D,
                44, 0.85D, 0.65D, 0.85D, 0.06D);
        player.getCooldowns().addCooldown(this, 30);
        return hits;
    }

    private int gunMode(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.getEyePosition();
        LivingEntity best = null;
        double bestScore = Double.MAX_VALUE;

        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(12.0D),
                entity -> entity != player && entity.isAlive())) {
            Vec3 toTarget = target.getEyePosition().subtract(origin);
            double distance = toTarget.length();
            if (distance < 0.01D || distance > 12.0D) continue;
            double alignment = toTarget.normalize().dot(look);
            if (alignment < 0.94D) continue;
            double score = distance + (1.0D - alignment) * 20.0D;
            if (score < bestScore) {
                best = target;
                bestScore = score;
            }
        }

        int hits = 0;
        if (best != null && best.hurt(player.damageSources().playerAttack(player), 11.0F)) {
            best.push(look.x * 0.55D, 0.08D, look.z * 0.55D);
            hits = 1;
        }

        for (int i = 1; i <= 14; i++) {
            double d = i * 0.72D;
            level.sendParticles(ParticleTypes.END_ROD,
                    origin.x + look.x * d, origin.y + look.y * d, origin.z + look.z * d,
                    1, 0.015D, 0.015D, 0.015D, 0.0D);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 0.95F, 1.28F);
        player.getCooldowns().addCooldown(this, 24);
        return hits;
    }
}
