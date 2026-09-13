package io.github.harri29.kamenrider;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Two-slot Raise Buckle system for Geats. Magnum/Ninja occupy upper; Boost/Zombie occupy lower. */
public final class GeatsBuckleItem extends Item {
    public enum Slot { UPPER, LOWER }
    public enum Buckle {
        MAGNUM(Slot.UPPER),
        NINJA(Slot.UPPER),
        BOOST(Slot.LOWER),
        ZOMBIE(Slot.LOWER);

        private final Slot slot;
        Buckle(Slot slot) { this.slot = slot; }
        public Slot slot() { return slot; }
    }

    private final Buckle buckle;

    public GeatsBuckleItem(Buckle buckle, Properties properties) {
        super(properties);
        this.buckle = buckle;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            RiderForm active = RiderTransformation.currentForm(player).orElse(null);
            if (active == null || !active.seriesId().equals("geats")) {
                player.displayClientMessage(Component.translatable("message.kamenrider.buckle_requires_geats"), true);
                return InteractionResultHolder.success(stack);
            }

            Buckle upper = upperOf(active);
            Buckle lower = lowerOf(active);
            if (buckle.slot() == Slot.UPPER) upper = buckle;
            else lower = buckle;

            RiderForm next = resolve(upper, lower);
            RiderTransformation.changeForm(serverLevel, player, next);
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 0.8F, next.soundPitch());
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY() + 1.0D, player.getZ(),
                    34, 0.55D, 0.85D, 0.55D, 0.07D);
            player.displayClientMessage(Component.translatable("message.kamenrider.buckle_set", stack.getHoverName(), Component.translatable(next.translationKey())), true);
        }
        return InteractionResultHolder.success(stack);
    }

    public static boolean hasUpper(RiderForm form, Buckle buckle) { return upperOf(form) == buckle; }
    public static boolean hasLower(RiderForm form, Buckle buckle) { return lowerOf(form) == buckle; }

    private static Buckle upperOf(RiderForm form) {
        return switch (form) {
            case GEATS_NINJA_BOOST, GEATS_NINJA_ZOMBIE, GEATS_NINJA -> Buckle.NINJA;
            default -> Buckle.MAGNUM;
        };
    }

    private static Buckle lowerOf(RiderForm form) {
        return switch (form) {
            case GEATS_MAGNUM_ZOMBIE, GEATS_NINJA_ZOMBIE, GEATS_ZOMBIE -> Buckle.ZOMBIE;
            default -> Buckle.BOOST;
        };
    }

    private static RiderForm resolve(Buckle upper, Buckle lower) {
        if (upper == Buckle.NINJA) {
            return lower == Buckle.ZOMBIE ? RiderForm.GEATS_NINJA_ZOMBIE : RiderForm.GEATS_NINJA_BOOST;
        }
        return lower == Buckle.ZOMBIE ? RiderForm.GEATS_MAGNUM_ZOMBIE : RiderForm.GEATS_MAGNUM_BOOST;
    }
}
