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

/** W weapons keyed to the active right-side Gaia Memory. */
public final class DoubleWeaponItem extends Item {
    public enum Style { METAL_SHAFT, TRIGGER_MAGNUM }

    private final Style style;

    public DoubleWeaponItem(Style style, Properties properties) {
        super(properties);
        this.style = style;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            RiderForm form = RiderTransformation.currentForm(player).orElse(null);
            if (form == null || !form.seriesId().equals("double")) {
                player.displayClientMessage(Component.translatable("message.kamenrider.double_weapon_requires_w"), true);
                return InteractionResultHolder.success(stack);
            }

            DoubleMemoryItem.Memory right = DoubleMemoryItem.rightMemory(form);
            boolean correct = (style == Style.METAL_SHAFT && right == DoubleMemoryItem.Memory.METAL)
                    || (style == Style.TRIGGER_MAGNUM && right == DoubleMemoryItem.Memory.TRIGGER);
            if (!correct) {
                player.displayClientMessage(
                        Component.translatable(
                                "message.kamenrider.double_weapon_wrong_memory",
                                Component.translatable(style == Style.METAL_SHAFT
                                        ? DoubleMemoryItem.Memory.METAL.translationKey()
                                        : DoubleMemoryItem.Memory.TRIGGER.translationKey())
                        ),
                        true
                );
                return InteractionResultHolder.success(stack);
            }

            if (player.getCooldowns().isOnCooldown(this)) {
                player.displayClientMessage(Component.translatable("message.kamenrider.weapon_cooldown"), true);
                return InteractionResultHolder.success(stack);
            }

            int hits = style == Style.METAL_SHAFT
                    ? metalShaft(serverLevel, player)
                    : triggerMagnum(serverLevel, player);
            player.displayClientMessage(
                    Component.translatable("message.kamenrider.weapon_attack", stack.getHoverName(), hits),
                    true
            );
        }
        return InteractionResultHolder.success(stack);
    }

    private int metalShaft(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.position().add(0.0D, 1.0D, 0.0D);
        int hits = 0;

        AABB box = player.getBoundingBox().inflate(5.6D);
        for (LivingEntity target : level.getEntitiesOfClass(
                LivingEntity.class,
                box,
                entity -> entity != player && entity.isAlive()
        )) {
            Vec3 center = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
            Vec3 toTarget = center.subtract(origin);
            if (toTarget.lengthSqr() < 0.01D || toTarget.length() > 5.6D) continue;
            if (toTarget.normalize().dot(look) < -0.05D) continue;

            if (target.hurt(player.damageSources().playerAttack(player), 16.0F)) {
                target.push(look.x * 1.20D, 0.22D, look.z * 1.20D);
                hits++;
            }
        }

        level.sendParticles(ParticleTypes.CRIT,
                player.getX() + look.x * 2.0D,
                player.getY() + 1.0D,
                player.getZ() + look.z * 2.0D,
                68, 1.4D, 0.75D, 1.4D, 0.10D);
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS, 1.15F, 0.72F);
        player.getCooldowns().addCooldown(this, 42);
        return hits;
    }

    private int triggerMagnum(ServerLevel level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.getEyePosition();
        int hits = 0;

        for (LivingEntity target : level.getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(16.0D),
                entity -> entity != player && entity.isAlive()
        )) {
            Vec3 toTarget = target.getEyePosition().subtract(origin);
            double distance = toTarget.length();
            if (distance < 0.01D || distance > 16.0D) continue;
            if (toTarget.normalize().dot(look) < 0.94D) continue;

            if (target.hurt(player.damageSources().playerAttack(player), 13.0F)) {
                target.push(look.x * 0.55D, 0.08D, look.z * 0.55D);
                hits++;
            }
        }

        for (int i = 1; i <= 24; i++) {
            double d = i * 0.65D;
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    origin.x + look.x * d,
                    origin.y + look.y * d,
                    origin.z + look.z * d,
                    1, 0.015D, 0.015D, 0.015D, 0.0D);
        }
        level.playSound(null, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_BLAST,
                SoundSource.PLAYERS, 0.85F, 1.38F);
        player.getCooldowns().addCooldown(this, 38);
        return hits;
    }
}
