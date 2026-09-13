package io.github.harri29.kamenrider.client;

import com.mojang.math.Axis;
import io.github.harri29.kamenrider.DoubleMemoryItem;
import io.github.harri29.kamenrider.DriverItem;
import io.github.harri29.kamenrider.GeatsBuckleItem;
import io.github.harri29.kamenrider.KuugaWeaponItem;
import io.github.harri29.kamenrider.RiderForm;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

/**
 * First-person-only Rider presentation. This never changes gameplay state;
 * it only animates the rendered hand/item and camera when the server tells the
 * client that a Henshin/form change or finisher presentation has started.
 */
public final class RiderFirstPersonPresentation {
    private RiderFirstPersonPresentation() {
    }

    public static void onRenderHand(RenderHandEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        int playerId = minecraft.player.getId();
        float henshin = RiderClientState.henshinProgress(playerId);
        float presentation = RiderClientState.presentationProgress(playerId);
        RiderForm form = RiderClientState.form(playerId).orElse(null);

        if (henshin < 1.0F) {
            applyHenshinPose(event, form, henshin);
        }
        if (presentation < 1.0F) {
            applyFinisherPose(event, presentation);
        }
    }

    public static void onComputeFov(ViewportEvent.ComputeFov event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        int playerId = minecraft.player.getId();
        float henshin = RiderClientState.henshinProgress(playerId);
        float presentation = RiderClientState.presentationProgress(playerId);
        double fov = event.getFOV();

        if (henshin < 1.0F) {
            float pulse = Mth.sin(henshin * Mth.PI);
            fov -= 4.5D * pulse;
        }
        if (presentation < 1.0F) {
            float shock = Mth.sin(presentation * Mth.PI);
            fov += 6.0D * shock * shock;
        }
        event.setFOV(fov);
    }

    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        int playerId = minecraft.player.getId();
        float henshin = RiderClientState.henshinProgress(playerId);
        float presentation = RiderClientState.presentationProgress(playerId);

        if (henshin < 1.0F) {
            float wave = Mth.sin(henshin * Mth.PI * 2.0F);
            event.setRoll(event.getRoll() + wave * 0.65F);
        }
        if (presentation < 1.0F) {
            float kick = (1.0F - presentation) * Mth.sin(presentation * Mth.PI * 3.0F);
            event.setRoll(event.getRoll() + kick * 0.9F);
        }
    }

    private static void applyHenshinPose(RenderHandEvent event, RiderForm form, float progress) {
        float eased = smooth(progress);
        float remaining = 1.0F - eased;
        float side = event.getHand() == InteractionHand.MAIN_HAND ? 1.0F : -1.0F;
        Item item = event.getItemStack().getItem();

        if (item instanceof DriverItem) {
            event.getPoseStack().translate(-side * 0.18D * remaining, 0.11D * remaining, -0.24D * remaining);
            event.getPoseStack().mulPose(Axis.YP.rotationDegrees(side * 28.0F * remaining));
            event.getPoseStack().mulPose(Axis.XP.rotationDegrees(-10.0F * remaining));
        } else if (item instanceof DoubleMemoryItem) {
            float insert = Mth.sin(progress * Mth.PI);
            event.getPoseStack().translate(-side * 0.10D * insert, -0.05D * insert, -0.28D * insert);
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(side * 16.0F * insert));
        } else if (item instanceof GeatsBuckleItem) {
            float lock = Mth.sin(progress * Mth.PI);
            event.getPoseStack().translate(-side * 0.13D * lock, -0.04D * lock, -0.24D * lock);
            event.getPoseStack().mulPose(Axis.YP.rotationDegrees(side * 22.0F * lock));
        } else {
            float lift = remaining * 0.08F;
            event.getPoseStack().translate(0.0D, lift, -remaining * 0.08D);
        }

        if (form != null) {
            float identity = switch (form.seriesId()) {
                case "kuuga" -> -4.0F;
                case "decade" -> 5.0F;
                case "double" -> side * 4.0F;
                case "geats" -> -2.5F;
                default -> 0.0F;
            };
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(identity * remaining));
        }
    }

    private static void applyFinisherPose(RenderHandEvent event, float progress) {
        float side = event.getHand() == InteractionHand.MAIN_HAND ? 1.0F : -1.0F;
        float attack = Mth.sin(progress * Mth.PI);
        float recoil = (1.0F - progress) * Mth.sin(progress * Mth.PI * 2.0F);
        Item item = event.getItemStack().getItem();

        if (item instanceof DoubleMemoryItem || item instanceof GeatsBuckleItem) {
            event.getPoseStack().translate(-side * 0.08D * attack, -0.04D * attack, -0.18D * attack);
            event.getPoseStack().mulPose(Axis.YP.rotationDegrees(side * 18.0F * attack));
        } else if (item instanceof KuugaWeaponItem) {
            event.getPoseStack().translate(side * 0.06D * attack, 0.02D * attack, -0.22D * attack);
            event.getPoseStack().mulPose(Axis.XP.rotationDegrees(-16.0F * attack));
        } else {
            event.getPoseStack().translate(0.0D, 0.0D, -0.10D * attack);
        }

        event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(side * recoil * 4.0F));
    }

    private static float smooth(float value) {
        float t = Mth.clamp(value, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }
}
