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

/** Geats weapons gated by the currently equipped Raise Buckles. */
public final class GeatsWeaponItem extends Item {
    public enum Style { MAGNUM_SHOOTER, NINJA_DUALER, ZOMBIE_BREAKER }
    private final Style style;

    public GeatsWeaponItem(Style style, Properties properties) {
        super(properties);
        this.style = style;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && level instanceof ServerLevel server) {
            RiderForm form = RiderTransformation.currentForm(player).orElse(null);
            if (form == null || !form.seriesId().equals("geats") || !allowed(form)) {
                player.displayClientMessage(Component.translatable("message.kamenrider.geats_weapon_wrong_buckle"), true);
                return InteractionResultHolder.success(stack);
            }
            if (player.getCooldowns().isOnCooldown(this)) {
                player.displayClientMessage(Component.translatable("message.kamenrider.weapon_cooldown"), true);
                return InteractionResultHolder.success(stack);
            }
            int hits = switch (style) {
                case MAGNUM_SHOOTER -> fireMagnum(server, player);
                case NINJA_DUALER -> ninjaRush(server, player);
                case ZOMBIE_BREAKER -> zombieSmash(server, player);
            };
            player.displayClientMessage(Component.translatable("message.kamenrider.weapon_attack", stack.getHoverName(), hits), true);
        }
        return InteractionResultHolder.success(stack);
    }

    private boolean allowed(RiderForm form) {
        return switch (style) {
            case MAGNUM_SHOOTER -> GeatsBuckleItem.hasUpper(form, GeatsBuckleItem.Buckle.MAGNUM);
            case NINJA_DUALER -> GeatsBuckleItem.hasUpper(form, GeatsBuckleItem.Buckle.NINJA);
            case ZOMBIE_BREAKER -> GeatsBuckleItem.hasLower(form, GeatsBuckleItem.Buckle.ZOMBIE);
        };
    }

    private int fireMagnum(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.getEyePosition();
        int hits = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(16.0D), e -> e != player && e.isAlive())) {
            Vec3 to = target.getEyePosition().subtract(origin);
            double d = to.length();
            if (d < 0.01D || d > 16.0D || to.normalize().dot(look) < 0.94D) continue;
            if (target.hurt(player.damageSources().playerAttack(player), 16.0F)) hits++;
        }
        for (int i = 1; i <= 22; i++) {
            double d = i * 0.7D;
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, origin.x + look.x*d, origin.y + look.y*d, origin.z + look.z*d, 1, 0.01D, 0.01D, 0.01D, 0.0D);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.0F, 1.35F);
        player.getCooldowns().addCooldown(this, 34);
        return hits;
    }

    private int ninjaRush(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        player.setDeltaMovement(player.getDeltaMovement().add(look.scale(0.9D)));
        player.hurtMarked = true;
        int hits = hitCone(level, player, 4.6D, 0.15D, 14.0F, 0.9D);
        level.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX()+look.x*1.8D, player.getY()+1.0D, player.getZ()+look.z*1.8D, 18, 0.7D, 0.6D, 0.7D, 0.02D);
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.45F);
        player.getCooldowns().addCooldown(this, 28);
        return hits;
    }

    private int zombieSmash(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        int hits = hitCone(level, player, 4.0D, 0.28D, 22.0F, 1.7D);
        level.sendParticles(ParticleTypes.CRIT, player.getX()+look.x*1.6D, player.getY()+0.9D, player.getZ()+look.z*1.6D, 64, 0.8D, 0.7D, 0.8D, 0.08D);
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.8F, 0.78F);
        player.getCooldowns().addCooldown(this, 52);
        return hits;
    }

    private int hitCone(ServerLevel level, Player player, double range, double threshold, float damage, double knockback) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.position().add(0.0D, 1.0D, 0.0D);
        AABB box = player.getBoundingBox().inflate(range);
        int hits = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
            Vec3 to = target.position().add(0.0D, target.getBbHeight()*0.5D, 0.0D).subtract(origin);
            if (to.lengthSqr() < 0.01D || to.length() > range || to.normalize().dot(look) < threshold) continue;
            if (target.hurt(player.damageSources().playerAttack(player), damage)) {
                target.push(look.x*knockback, 0.22D, look.z*knockback);
                hits++;
            }
        }
        return hits;
    }
}
