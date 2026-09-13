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

public class DriverItem extends Item {
    private static final int FINISHER_COOLDOWN = 20 * 5;

    private final RiderForm baseForm;

    public DriverItem(RiderForm baseForm, Properties properties) {
        super(properties);
        this.baseForm = baseForm;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            RiderForm current = RiderTransformation.currentForm(player).orElse(null);

            if (player.isShiftKeyDown() && current != null && current.seriesId().equals(baseForm.seriesId())) {
                riderKick(serverLevel, player, current);
            } else if (current != null && current.seriesId().equals(baseForm.seriesId())) {
                RiderTransformation.dehenshin(serverLevel, player);
            } else {
                RiderTransformation.henshin(serverLevel, player, baseForm);
            }
        }

        return InteractionResultHolder.success(stack);
    }

    private void riderKick(ServerLevel level, Player player, RiderForm activeForm) {
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
                if (target.hurt(player.damageSources().playerAttack(player), activeForm.kickDamage())) {
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
                Component.translatable("message.kamenrider.rider_kick", Component.translatable(activeForm.translationKey()), hits),
                true
        );
    }
}
