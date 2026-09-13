package io.github.harri29.kamenrider.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.harri29.kamenrider.RiderForm;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/** Dedicated staged armor renderer for Kamen Rider Decade. */
public final class DecadeSuitLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation SOLID_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "minecraft", "textures/block/white_concrete.png"
    );
    private static final int BLACK = 0xFF17171A;
    private static final int MAGENTA = 0xFFDB3D93;
    private static final int CARD_WHITE = 0xFFE9E9EC;
    private static final int EYE_CYAN = 0xFF58E8FF;

    private final DecadeSuitModel armorModel;

    public DecadeSuitLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
            ModelPart root,
            boolean slim
    ) {
        super(parent);
        armorModel = new DecadeSuitModel(root, slim);
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
        if (form != RiderForm.DECADE || player.isInvisible()) {
            return;
        }

        getParentModel().copyPropertiesTo(armorModel);
        armorModel.prepareMobModel(player, limbSwing, limbSwingAmount, partialTick);
        armorModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        float progress = RiderClientState.henshinProgress(player.getId());

        armorModel.configure(DecadeSuitModel.Pass.BASE, progress);
        renderColoredCutoutModel(armorModel, SOLID_TEXTURE, poseStack, bufferSource, packedLight, player, BLACK);

        armorModel.configure(DecadeSuitModel.Pass.ACCENT, progress);
        renderColoredCutoutModel(armorModel, SOLID_TEXTURE, poseStack, bufferSource, packedLight, player, MAGENTA);

        armorModel.configure(DecadeSuitModel.Pass.CARD, progress);
        renderColoredCutoutModel(armorModel, SOLID_TEXTURE, poseStack, bufferSource, packedLight, player, CARD_WHITE);

        armorModel.configure(DecadeSuitModel.Pass.CORE, progress);
        renderColoredCutoutModel(armorModel, SOLID_TEXTURE, poseStack, bufferSource, LightTexture.FULL_BRIGHT, player, EYE_CYAN);
    }
}
