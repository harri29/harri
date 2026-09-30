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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Craftable Survival entry point for the four Rider progression trials. */
public final class RiderTrialItem extends Item {
    public enum Trial {
        KUUGA("kuuga"),
        DECADE("decade"),
        DOUBLE("double"),
        GEATS("geats");

        private final String id;
        Trial(String id) { this.id = id; }
        public String id() { return id; }
    }

    private final Trial trial;

    public RiderTrialItem(Trial trial, Properties properties) {
        super(properties);
        this.trial = trial;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && level instanceof ServerLevel server) {
            if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.success(stack);

            Mob boss = createBoss(server);
            if (boss == null) return InteractionResultHolder.fail(stack);

            Vec3 look = player.getLookAngle().normalize();
            boss.moveTo(player.getX() + look.x * 4.0D, player.getY(), player.getZ() + look.z * 4.0D,
                    player.getYRot() + 180.0F, 0.0F);
            boss.setCustomName(Component.translatable("boss.kamenrider.trial_" + trial.id()));
            boss.setCustomNameVisible(true);
            boss.setPersistenceRequired();
            boss.addTag("kamenrider.trial." + trial.id());
            tuneBoss(boss);
            server.addFreshEntity(boss);

            player.getCooldowns().addCooldown(this, 20 * 15);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            player.displayClientMessage(Component.translatable("message.kamenrider.trial_begin", boss.getDisplayName()), true);
            server.playSound(null, player.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.75F, 1.15F);
            server.sendParticles(ParticleTypes.PORTAL, boss.getX(), boss.getY() + 1.0D, boss.getZ(),
                    90, 0.8D, 1.1D, 0.8D, 0.25D);
        }
        return InteractionResultHolder.success(stack);
    }

    private Mob createBoss(ServerLevel level) {
        return switch (trial) {
            case KUUGA -> EntityType.HUSK.create(level);
            case DECADE -> EntityType.VINDICATOR.create(level);
            case DOUBLE -> EntityType.ENDERMAN.create(level);
            case GEATS -> EntityType.RAVAGER.create(level);
        };
    }

    private void tuneBoss(Mob boss) {
        double maxHealth = switch (trial) {
            case KUUGA -> 72.0D;
            case DECADE -> 82.0D;
            case DOUBLE -> 94.0D;
            case GEATS -> 118.0D;
        };
        var health = boss.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) health.setBaseValue(maxHealth);
        boss.setHealth((float) maxHealth);

        boss.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60 * 20, trial == Trial.GEATS ? 2 : 1, false, false));
        boss.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 20, 0, false, false));
        if (trial == Trial.KUUGA || trial == Trial.DOUBLE) {
            boss.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 60 * 20, 1, false, false));
        }
    }
}
