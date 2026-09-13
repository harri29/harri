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

/** Kuuga weapons with form-locked right-click techniques and Shift finishers. */
public final class KuugaWeaponItem extends Item {
    public enum Style {
        DRAGON_ROD,
        TITAN_SWORD
    }

    private static final int FINISHER_COOLDOWN = 20 * 6;

    private final RiderForm requiredForm;
    private final Style style;

    public KuugaWeaponItem(RiderForm requiredForm, Style style, Properties properties) {
        super(properties);
        this.requiredForm = requiredForm;
        this.style = style;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            RiderForm active = RiderTransformation.currentForm(player).orElse(null);
            if (active != requiredForm) {
                player.displayClientMessage(
                        Component.translatable(
                                "message.kamenrider.weapon_wrong_form",
                                Component.translatable(requiredForm.translationKey())
                        ),
                        true
                );
                return InteractionResultHolder.success(stack);
            }

            if (player.getCooldowns().isOnCooldown(this)) {
                player.displayClientMessage(Component.translatable("message.kamenrider.weapon_cooldown"), true);
                return InteractionResultHolder.success(stack);
            }

            if (player.isShiftKeyDown()) {
                performFinisher(serverLevel, player, stack);
            } else {
                performTechnique(serverLevel, player, stack);
            }
        }

        return InteractionResultHolder.success(stack);
    }

    private void performTechnique(ServerLevel level, Player player, ItemStack stack) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.position().add(0.0D, 1.0D, 0.0D);

        double range = style == Style.DRAGON_ROD ? 4.8D : 3.6D;
        double facingThreshold = style == Style.DRAGON_ROD ? -0.05D : 0.28D;
        float damage = style == Style.DRAGON_ROD ? 12.0F : 18.0F;
        double knockbackStrength = style == Style.DRAGON_ROD ? 0.85D : 1.45D;
        int cooldown = style == Style.DRAGON_ROD ? 32 : 52;
        int hits = hitCone(level, player, look, origin, range, facingThreshold, damage, knockbackStrength);

        if (style == Style.DRAGON_ROD) {
            player.setDeltaMovement(player.getDeltaMovement().add(look.scale(0.36D)));
            player.hurtMarked = true;
            level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 0.72F);
            level.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    player.getX() + look.x * 2.2D,
                    player.getY() + 1.0D,
                    player.getZ() + look.z * 2.2D,
                    42, 1.15D, 0.55D, 1.15D, 0.12D
            );
        } else {
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.75F, 0.78F);
            level.sendParticles(
                    ParticleTypes.CRIT,
                    player.getX() + look.x * 1.8D,
                    player.getY() + 1.0D,
                    player.getZ() + look.z * 1.8D,
                    58, 0.75D, 0.85D, 0.75D, 0.08D
            );
        }

        player.getCooldowns().addCooldown(this, cooldown);
        player.displayClientMessage(
                Component.translatable("message.kamenrider.weapon_attack", stack.getHoverName(), hits),
                true
        );
    }

    private void performFinisher(ServerLevel level, Player player, ItemStack stack) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 origin = player.position().add(0.0D, 1.0D, 0.0D);

        double range = style == Style.DRAGON_ROD ? 6.4D : 5.0D;
        double threshold = style == Style.DRAGON_ROD ? 0.02D : 0.20D;
        float damage = style == Style.DRAGON_ROD ? 30.0F : 39.0F;
        double knockback = style == Style.DRAGON_ROD ? 1.45D : 2.35D;
        int hits = hitCone(level, player, look, origin, range, threshold, damage, knockback);

        if (style == Style.DRAGON_ROD) {
            player.setDeltaMovement(player.getDeltaMovement().add(look.scale(0.82D)).add(0.0D, 0.12D, 0.0D));
            player.hurtMarked = true;
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    player.getX() + look.x * 2.7D, player.getY() + 1.0D, player.getZ() + look.z * 2.7D,
                    96, 1.25D, 0.8D, 1.25D, 0.16D);
            level.sendParticles(ParticleTypes.END_ROD,
                    player.getX() + look.x * 2.7D, player.getY() + 1.0D, player.getZ() + look.z * 2.7D,
                    38, 0.9D, 0.7D, 0.9D, 0.08D);
            level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.2F, 1.25F);
        } else {
            level.sendParticles(ParticleTypes.CRIT,
                    player.getX() + look.x * 2.0D, player.getY() + 0.9D, player.getZ() + look.z * 2.0D,
                    120, 1.05D, 0.9D, 1.05D, 0.14D);
            level.sendParticles(ParticleTypes.LARGE_SMOKE,
                    player.getX() + look.x * 1.8D, player.getY() + 0.7D, player.getZ() + look.z * 1.8D,
                    34, 0.8D, 0.5D, 0.8D, 0.06D);
            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.1F, 0.70F);
        }

        player.getCooldowns().addCooldown(this, FINISHER_COOLDOWN);
        RiderNetworking.presentation(player, "kuuga_weapon_finisher");
        player.displayClientMessage(
                Component.translatable("message.kamenrider.kuuga_weapon_finisher", stack.getHoverName(), hits),
                true
        );
    }

    private int hitCone(
            ServerLevel level,
            Player player,
            Vec3 look,
            Vec3 origin,
            double range,
            double facingThreshold,
            float damage,
            double knockbackStrength
    ) {
        int hits = 0;
        AABB searchBox = player.getBoundingBox().inflate(range);
        for (LivingEntity target : level.getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                entity -> entity != player && entity.isAlive()
        )) {
            Vec3 targetCenter = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D);
            Vec3 toTarget = targetCenter.subtract(origin);
            if (toTarget.lengthSqr() < 0.01D || toTarget.length() > range) {
                continue;
            }
            if (toTarget.normalize().dot(look) < facingThreshold) {
                continue;
            }

            if (target.hurt(player.damageSources().playerAttack(player), damage)) {
                Vec3 push = look.scale(knockbackStrength).add(0.0D, style == Style.TITAN_SWORD ? 0.30D : 0.18D, 0.0D);
                target.push(push.x, push.y, push.z);
                hits++;
            }
        }
        return hits;
    }
}
