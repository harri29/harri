package io.github.harri29.kamenrider;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/** Rewards a player for defeating one of the tagged Survival trial bosses. */
public final class RiderTrialSystem {
    private static final String TRIAL_PREFIX = "kamenrider.trial.";
    private static final String UNLOCK_PREFIX = "kamenrider.unlock.";

    private RiderTrialSystem() {
    }

    public static void onLivingDeath(LivingDeathEvent event) {
        String series = trialSeries(event.getEntity());
        if (series == null) return;

        Entity sourceEntity = event.getSource().getEntity();
        if (!(sourceEntity instanceof ServerPlayer player)) return;

        player.addTag(UNLOCK_PREFIX + series);
        reward(player, series);
        player.displayClientMessage(
                Component.translatable("message.kamenrider.trial_clear", Component.translatable("series.kamenrider." + series)),
                false
        );
    }

    private static String trialSeries(Entity entity) {
        for (String tag : entity.getTags()) {
            if (tag.startsWith(TRIAL_PREFIX)) return tag.substring(TRIAL_PREFIX.length());
        }
        return null;
    }

    private static void reward(ServerPlayer player, String series) {
        switch (series) {
            case "kuuga" -> {
                give(player, new ItemStack(KamenRiderMod.KUUGA_ARCLE.get()));
                give(player, new ItemStack(KamenRiderMod.KUUGA_FORM_CHANGER.get()));
                give(player, new ItemStack(KamenRiderMod.KUUGA_DRAGON_ROD.get()));
            }
            case "decade" -> {
                give(player, new ItemStack(KamenRiderMod.DECADE_DRIVER.get()));
                give(player, new ItemStack(KamenRiderMod.RIDE_BOOKER.get()));
                give(player, new ItemStack(KamenRiderMod.ATTACK_RIDE_SLASH.get()));
            }
            case "double" -> {
                give(player, new ItemStack(KamenRiderMod.DOUBLE_DRIVER.get()));
                give(player, new ItemStack(KamenRiderMod.CYCLONE_MEMORY.get()));
                give(player, new ItemStack(KamenRiderMod.JOKER_MEMORY.get()));
            }
            case "geats" -> {
                give(player, new ItemStack(KamenRiderMod.DESIRE_DRIVER.get()));
                give(player, new ItemStack(KamenRiderMod.MAGNUM_BUCKLE.get()));
                give(player, new ItemStack(KamenRiderMod.BOOST_BUCKLE.get()));
            }
            default -> { }
        }
    }

    private static void give(ServerPlayer player, ItemStack stack) {
        if (!player.addItem(stack)) player.drop(stack, false);
    }
}
