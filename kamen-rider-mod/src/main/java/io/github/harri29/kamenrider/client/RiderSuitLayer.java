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
 * Copyright-safe placeholder suit renderer.
 * It reuses the player's own skin as a tinted outer shell and can later be
 * replaced by original Blockbench Rider geometry without changing networking.
 */
public final class RiderSuitLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final PlayerModel<AbstractClientPlayer> suitModel;

    public RiderSuitLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
            ModelPart root,
            boolean slim
    ) {
        super(parent);
        this.suitModel = new PlayerModel<>(root, slim);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        RiderForm form = RiderClientState.form(player.getId()).orElse(null);
        if (form == null || player.isInvisible()) {
            return;
        }

        getParentModel().copyPropertiesTo(suitModel);
        suitModel.prepareMobModel(player, limbSwing, limbSwingAmount, partialTick);
        suitModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        float progress = RiderClientState.henshinProgress(player.getId());
        float pulse = 1.0F + (1.0F - progress) * 0.09F;
        int argb = 0xFF000000 | form.suitColor();

        poseStack.pushPose();
        poseStack.scale(pulse, pulse, pulse);
        renderColoredCutoutModel(
                suitModel,
                getTextureLocation(player),
                poseStack,
                bufferSource,
                packedLight,
                player,
                argb
        );
        poseStack.popPose();
    }
}
