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

/** First-person-only Rider presentation; gameplay remains server authoritative. */
public final class RiderFirstPersonPresentation {
    private RiderFirstPersonPresentation() {
    }

    public static void onRenderHand(RenderHandEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.options.getCameraType().isFirstPerson()) return;

        int playerId = minecraft.player.getId();
        float henshin = RiderClientState.henshinProgress(playerId);
        float presentation = RiderClientState.presentationProgress(playerId);
        RiderForm form = RiderClientState.form(playerId).orElse(null);

        if (henshin < 1.0F) applyHenshinPose(event, form, henshin);
        if (presentation < 1.0F) applyFinisherPose(event, presentation);
    }

    public static void onComputeFov(ViewportEvent.ComputeFov event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.options.getCameraType().isFirstPerson()) return;

        int playerId = minecraft.player.getId();
        float henshin = RiderClientState.henshinProgress(playerId);
        float presentation = RiderClientState.presentationProgress(playerId);
        double fov = event.getFOV();

        if (henshin < 1.0F) {
            float focus = stage(henshin, 0.08F, 0.48F) * (1.0F - stage(henshin, 0.64F, 1.0F));
            float flash = Mth.sin(stage(henshin, 0.72F, 1.0F) * Mth.PI);
            fov -= 5.0D * focus;
            fov += 2.0D * flash;
        }
        if (presentation < 1.0F) {
            float shock = Mth.sin(presentation * Mth.PI);
            fov += 6.0D * shock * shock;
        }
        event.setFOV(fov);
    }

    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.options.getCameraType().isFirstPerson()) return;

        int playerId = minecraft.player.getId();
        float henshin = RiderClientState.henshinProgress(playerId);
        float presentation = RiderClientState.presentationProgress(playerId);

        if (henshin < 1.0F) {
            float prepare = stage(henshin, 0.0F, 0.30F);
            float lock = stage(henshin, 0.46F, 0.76F);
            float settle = stage(henshin, 0.76F, 1.0F);
            float wave = Mth.sin(prepare * Mth.PI) * 0.45F + Mth.sin(lock * Mth.PI) * 0.65F;
            event.setRoll(event.getRoll() + wave * (1.0F - 0.75F * settle));
        }
        if (presentation < 1.0F) {
            float kick = (1.0F - presentation) * Mth.sin(presentation * Mth.PI * 3.0F);
            event.setRoll(event.getRoll() + kick * 0.9F);
        }
    }

    private static void applyHenshinPose(RenderHandEvent event, RiderForm form, float progress) {
        float side = event.getHand() == InteractionHand.MAIN_HAND ? 1.0F : -1.0F;
        Item item = event.getItemStack().getItem();

        float prepare = smooth(stage(progress, 0.00F, 0.28F));
        float present = smooth(stage(progress, 0.20F, 0.52F));
        float lock = smooth(stage(progress, 0.48F, 0.76F));
        float finish = smooth(stage(progress, 0.74F, 1.00F));
        float hold = present * (1.0F - finish);

        event.getPoseStack().translate(-side * 0.06D * prepare, 0.07D * prepare, -0.10D * prepare);

        if (item instanceof DriverItem) {
            event.getPoseStack().translate(-side * 0.14D * hold, -0.02D * lock, -0.24D * hold);
            event.getPoseStack().mulPose(Axis.YP.rotationDegrees(side * (18.0F * present - 14.0F * lock)));
            event.getPoseStack().mulPose(Axis.XP.rotationDegrees(-9.0F * hold));
        } else if (item instanceof DoubleMemoryItem) {
            event.getPoseStack().translate(-side * 0.12D * hold, -0.08D * lock, -0.31D * present);
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(side * (18.0F * present - 12.0F * lock)));
            event.getPoseStack().mulPose(Axis.XP.rotationDegrees(-12.0F * lock));
        } else if (item instanceof GeatsBuckleItem) {
            event.getPoseStack().translate(-side * 0.15D * hold, -0.06D * lock, -0.28D * present);
            event.getPoseStack().mulPose(Axis.YP.rotationDegrees(side * (24.0F * present - 20.0F * lock)));
            event.getPoseStack().mulPose(Axis.XP.rotationDegrees(-8.0F * lock));
        } else {
            event.getPoseStack().translate(0.0D, 0.05D * prepare, -0.08D * hold);
        }

        if (form != null) {
            float identity = switch (form.seriesId()) {
                case "kuuga" -> -4.0F;
                case "decade" -> 5.0F;
                case "double" -> side * 4.0F;
                case "geats" -> -2.5F;
                default -> 0.0F;
            };
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(identity * prepare * (1.0F - finish)));
        }

        float snap = Mth.sin(lock * Mth.PI);
        event.getPoseStack().translate(0.0D, -0.025D * snap, -0.035D * snap);
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

    private static float stage(float progress, float start, float end) {
        if (progress <= start) return 0.0F;
        if (progress >= end) return 1.0F;
        return (progress - start) / (end - start);
    }

    private static float smooth(float value) {
        float t = Mth.clamp(value, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }
}
