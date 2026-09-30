package io.github.harri29.kamenrider.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.harri29.kamenrider.RiderForm;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

/**
 * Shared undersuit/fallback renderer.
 * Riders with dedicated armor geometry use a dark undersuit; other Riders keep
 * the colored shell until their own model is implemented.
 */
public final class RiderSuitLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final PlayerModel<AbstractClientPlayer> suitModel;

    public RiderSuitLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, ModelPart root, boolean slim) {
        super(parent);
        this.suitModel = new PlayerModel<>(root, slim);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        RiderForm form = RiderClientState.form(player.getId()).orElse(null);
        if (form == null || player.isInvisible()) return;

        getParentModel().copyPropertiesTo(suitModel);
        suitModel.prepareMobModel(player, limbSwing, limbSwingAmount, partialTick);
        suitModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        boolean dedicatedModel = form.seriesId().equals("kuuga")
                || form.seriesId().equals("decade")
                || form.seriesId().equals("double")
                || form.seriesId().equals("geats");
        float progress = RiderClientState.henshinProgress(player.getId());
        float pulseAmount = dedicatedModel ? 0.035F : 0.09F;
        float pulse = 1.0F + (1.0F - progress) * pulseAmount;
        int suitRgb = dedicatedModel ? 0x17191D : form.suitColor();
        int argb = 0xFF000000 | suitRgb;

        poseStack.pushPose();
        poseStack.scale(pulse, pulse, pulse);
        renderColoredCutoutModel(suitModel, getTextureLocation(player), poseStack, bufferSource, packedLight, player, argb);
        poseStack.popPose();
    }
}
